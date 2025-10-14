# Online Reservation System

This is a simple Java SE (Standard Edition) console-based Online Reservation System with SQLite database integration.  
The project allows users to register, log in, make train ticket reservations, and cancel reservations using a unique PNR number.  
No external frameworks (like Spring Boot, Maven, or Gradle) are used—only Java SE and the SQLite JDBC driver.

## Features

- **User Registration & Login:**  
  Users can create a new account and securely log in.
- **Train Ticket Reservation:**  
  Authenticated users can reserve train tickets by providing journey and train details.  
  A unique PNR number is generated for each reservation and displayed upon successful booking.
- **Ticket Cancellation:**  
  Users can cancel a reservation by entering their PNR number.  
  The system ensures that only the owner of the reservation can cancel it.
- **Simple, Clean Code:**  
  No frameworks or complex configuration. Suitable for learning, assignments, and demonstration.

## Folder Structure

```
Online Reservation System/
│
├── lib/
│    └── sqlite-jdbc-<version>.jar
│
└── src/
     └── reservation/
          ├── Main.java
          ├── DatabaseUtil.java
          ├── LoginForm.java
          ├── ReservationForm.java
          └── CancellationForm.java
```

## How to Compile

Open your terminal in the project root directory and run:

```sh
javac -cp "lib/*" src/reservation/*.java
```

## How to Run

After compiling, run the application with:

```sh
java -cp "lib/*;src" reservation.Main
```

> **Note:** On Mac/Linux, use a colon `:` instead of a semicolon `;` in the classpath:
> 
> ```
> java -cp "lib/*:src" reservation.Main
> ```

## Requirements

- Java JDK 8 or higher
- [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc) (already included in the `lib/` folder)
- No need for Maven/Gradle or any web frameworks

## Usage

1. Register a new user (one time only).
2. Log in with your credentials.
3. Reserve a ticket and note your PNR number.
4. Cancel tickets using your PNR number if required.
5. All data is persisted in `reservation.db` (auto-created in the project folder).

---

Happy coding!