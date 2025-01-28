package models

import play.api.libs.json.{Json, OFormat}

case class Reservation(
                        id: Option[Int],
                        roomId: Int,
                        employeeName: String,
                        department: String,
                        purpose: String,
                        startTime: String,
                        endTime: String,
                        createdBy: Int
                      )

object Reservation {
  implicit val reservationFormat: OFormat[Reservation] = Json.format[Reservation]
}
