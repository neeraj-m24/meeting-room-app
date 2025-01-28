# Automated Notification Module for Office Meeting Room Management System

This repository contains the **Automated Notification Module** for the **Office Meeting Room Management System**. The module handles the generation and dispatch of notifications related to meeting room readiness, reminders for upcoming meetings, and other notification needs.

---

## Features

- **Room Preparation Alerts**: Notifies the RoomService team to prepare the room before scheduled meetings.
- **Reminder Notifications**: Sends reminders to users about their upcoming meetings.
- **Event-Driven Architecture**: Utilizes **Kafka** for efficient, real-time message publishing and consumption.
- **Scalable Microservice**: Designed for scalability and fault tolerance using Akka Actors.

---

## Repository Structure

- **Notification Service**: Core logic for handling notification events.
- **Kafka Consumer**: Listens for meeting-related events to trigger notifications.
- **Configuration Files**: Includes configuration for Kafka, database, and notification templates.

---

## Tools and Technologies Used

1. **Programming Language**: Scala
2. **Frameworks**: Akka
3. **Message Broker**: Kafka
4. **Database**: MySQL (for storing notification logs and configurations)
5. **Containerization**: Docker

---

## How to Set Up and Run

1. **Clone the Repository**:
   ```bash
   git clone ""
   cd ""
