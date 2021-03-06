package lila.api

import play.api.libs.ws.DefaultBodyWritables._
import play.api.libs.ws.StandaloneWSClient

final class InfluxEvent(
    ws: StandaloneWSClient,
    endpoint: String,
    env: String
)(implicit ec: scala.concurrent.ExecutionContext) {

  private val seed = ornicar.scalalib.Random.nextString(6)

  def start() = apply("lila_start", s"Lila starts: $seed")

  def friendListToggle(value: Boolean) = apply(s"friend_list_$value", s"Toggle friend list: $value")

  private def apply(key: String, text: String) =
    ws.url(endpoint)
      .post(s"""event,program=lila,env=$env,title=$key text="$text"""")
      .effectFold(
        err => lila.log("influxEvent").error(endpoint, err),
        res => if (res.status != 204) lila.log("influxEvent").error(s"$endpoint ${res.status}")
      )
}
