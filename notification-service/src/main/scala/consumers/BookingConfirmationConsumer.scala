package consumers

import actors.{ReleaseActor, ReminderActor}
import akka.actor.ActorSystem
import io.circe.parser.decode
import models.Reservation
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import services.{EmailService, RoomService}
import utils.JsonFormats.*

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}
import java.time.temporal.ChronoUnit
import java.util.{Collections, Properties}
import scala.concurrent.duration.*

object BookingConfirmationConsumer {
  def startConsumer()(implicit system: ActorSystem): Unit = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "booking-confirmation-group")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)

    val consumer = new KafkaConsumer[String, String](props)
    consumer.subscribe(Collections.singletonList("reservation-updated"))

    val emailService = new EmailService()  // Instantiate the EmailService
    val roomService = new RoomService()    // Instantiate the RoomService
    val reminderActor = system.actorOf(ReminderActor.props(emailService), "reminderActor") // Create ReminderActor
    val releaseActor = system.actorOf(ReleaseActor.props(emailService, roomService), "releaseActor") // Create ReleaseActor

    system.log.info("Booking Confirmation Consumer started")

    // Helper function to parse datetime with multiple formats
    def parseDateTime(dateTime: String): LocalDateTime = {
      val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME // For ISO-8601 format: 2024-11-18T21:14:00
      val customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Optional fallback format

      try {
        LocalDateTime.parse(dateTime, isoFormatter)
      } catch {
        case _: Exception =>
          system.log.warning(s"Falling back to custom date format for: $dateTime")
          LocalDateTime.parse(dateTime, customFormatter)
      }
    }

    while (true) {
      val records = consumer.poll(java.time.Duration.ofMillis(100))
      records.forEach { record =>
        system.log.info(s"Received message: ${record.value()}")

        decode[Reservation](record.value()) match {
          case Right(reservation) =>
            system.log.info(s"Decoded reservation: $reservation")

            try {
              val startTime = parseDateTime(reservation.startTime)
              val now = LocalDateTime.now(ZoneId.systemDefault())

              system.log.info(s"DEBUG: Parsed startTime=$startTime, currentTime=$now")

              // Temporary testing: Reminder 10 seconds before start time
              val reminderTime = startTime.minusSeconds(10)
              val reminderDelay = ChronoUnit.MILLIS.between(now, reminderTime)

              system.log.info(s"DEBUG: reminderDelay=$reminderDelay, reminderTime=$reminderTime")

              if (reminderDelay > 0) {
                system.scheduler.scheduleOnce(
                  reminderDelay.milliseconds,
                  reminderActor,
                  reservation
                )(system.dispatcher)
                system.log.info(s"Scheduled reminder for reservation ID: ${reservation.id} at $reminderTime")
              } else {
                system.log.warning(s"Skipping reminder for reservation ID: ${reservation.id}, too close or past start time")
              }

              // Temporary testing: Release check 10 seconds after the start time
              val releaseTime = startTime.plusSeconds(1)
              val releaseDelay = ChronoUnit.MILLIS.between(now, releaseTime)

              system.log.info(s"DEBUG: releaseDelay=$releaseDelay, releaseTime=$releaseTime")

              if (releaseDelay > 0) {
                system.scheduler.scheduleOnce(
                  releaseDelay.milliseconds,
                  releaseActor,
                  reservation
                )(system.dispatcher)
                system.log.info(s"Scheduled release check for reservation ID: ${reservation.id} at $releaseTime")
              } else {
                system.log.warning(s"Skipping release check for reservation ID: ${reservation.id}, too close or past start time")
              }
            } catch {
              case ex: Exception =>
                system.log.error(s"Unhandled exception in scheduling logic for reservation ID: ${reservation.id}, error: ${ex.getMessage}")
            }

          case Left(error) =>
            system.log.error(s"Failed to decode JSON to Reservation: ${error.getMessage}")
        }
      }
    }
  }
}
