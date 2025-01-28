# Meeting Room Reservation Component for Office Space Management System

This repository includes the **Meeting Room Reservation Component** of the **Office Space Management System**. It provides functionality for reserving meeting rooms, checking availability, and managing meeting schedules efficiently.

---

## Key Features

- **Room Booking**: Allows users to reserve meeting rooms by specifying the date, time, and room details.
- **Availability Checking**: Validates whether the room is available within the specified time slot.
- **Conflict Resolution**: Prevents double bookings for the same room at overlapping times.
- **Role-Based Access**: Differentiates functionalities based on user roles (Admin, User).

---

## Repository Structure

- **Reservation Service**: Manages the core operations for creating, updating, and canceling reservations.
- **Availability Endpoint**: Provides an API to check room availability using parameters like `room_id`, `startTime`, and `endTime`.
- **Database Schema**:
    - **Room Table**: Stores details about the meeting rooms.
    - **Reservation Table**: Stores information on meeting reservations, including room, time, and user details.

---

## Tools and Technologies Used

1. **Programming Language**: Scala
2. **Framework**: Play Framework
3. **Database**: MySQL
4. **API Testing**: Postman or curl

---

