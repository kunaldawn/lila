package lila.puzzle

import org.joda.time.DateTime

case class Round(
    puzzleId: PuzzleId,
    userId: String,
    date: DateTime,
    win: Boolean,
    time: Int, // millis
    userRating: Int,
    userRatingDiff: Int) {

  def seconds = time / 1000

  def loss = !win

  def userPostRating = userRating + userRatingDiff
}

object Round {

  object BSONFields {
    val puzzleId = "p"
    val userId = "u"
    val date = "d"
    val win = "w"
    val time = "t"
    val userRating = "ur"
    val userRatingDiff = "ud"
  }

  import reactivemongo.bson._
  import lila.db.BSON
  import BSON.BSONJodaDateTimeHandler
  implicit val roundBSONHandler = new BSON[Round] {

    import BSONFields._

    def reads(r: BSON.Reader): Round = Round(
      puzzleId = r int puzzleId,
      userId = r str userId,
      date = r.get[DateTime](date),
      win = r bool win,
      time = r int time,
      userRating = r int userRating,
      userRatingDiff = r int userRatingDiff)

    def writes(w: BSON.Writer, o: Round) = BSONDocument(
      puzzleId -> o.puzzleId,
      userId -> o.userId,
      date -> o.date,
      win -> o.win,
      time -> w.int(o.time),
      userRating -> w.int(o.userRating),
      userRatingDiff -> w.int(o.userRatingDiff))
  }
}
