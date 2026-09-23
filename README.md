# Egypt Meters Control System (EMCS - version 1.0.0)

**Egypt Meters Control System (EMCS)** is a desktop application designed to provide a simple and user-friendly interface for connecting to and interacting with electricity meters.

The application is intended to provide a centralized desktop interface for meter authentication, connection, and reading meter data while keeping the low-level meter communication logic separated into a dedicated meter library.

---

## Features

### Authentication

* Local user authentication.
* Secure local credential storage.
* Password hashing using PBKDF2.
* Change password functionality.
* Logout functionality.
* No online account or server is required for the application authentication.

### Meter Connection

* Connect to an electricity meter through a serial interface.
* Meter authentication before establishing a connection.
* Dedicated meter password confirmation.
* Connection status displayed directly in the application.

### Meter Readings

* Display available meter readings in a table.
* Select individual readings to retrieve.
* Support selecting multiple readings at the same time.
* Retrieve only the selected readings.
* Display the returned values in the application.

### Meter Communication

The application separates the user interface from the meter communication implementation.

The main application communicates with a dedicated meter library:

```text
EMCS JavaFX Application
        │
        ▼
   MeterService
        │
        ▼
    Meter JAR
        │
        ├── jSerialComm
        │
        └── Gurux DLMS
```

This separation allows the meter communication implementation to be developed and maintained independently from the JavaFX user interface.

---

## Technology Stack

### Application

* Java 21
* JavaFX 21
* Maven
* IntelliJ IDEA

### Meter Communication

* Gurux DLMS
* jSerialComm
* Custom Egypt Meters communication library

### Architecture

The application follows a layered structure:

```text
View
  │
  ▼
ViewModel
  │
  ▼
Service
  │
  ▼
Meter Library
```

This structure keeps UI logic separated from application logic and meter communication.

---

## Project Structure

```text
egypt-meters-control-system/
│
├── libs/
│   └── egypt-meters-library.jar
│
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── Main.java
│       │           │
│       │           ├── model/
│       │           │   └── MeterField.java
│       │           │
│       │           ├── service/
│       │           │   ├── MeterService.java
│       │           │   └── SecureStorageService.java
│       │           │
│       │           ├── view/
│       │           │   ├── HomeView.java
│       │           │   ├── LoginView.java
│       │           │   └── ChangePasswordDialog.java
│       │           │
│       │           └── viewmodel/
│       │               ├── HomeViewModel.java
│       │               └── LoginViewModel.java
│       │
│       └── resources/
│           └── css/
│               └── app.css
│
├── pom.xml
└── README.md
```

---

## Meter Library

The application does not contain the low-level DLMS meter communication directly inside the JavaFX UI.

Instead, the application uses a separate JAR library.

The JavaFX application accesses the library through `MeterService`.

For example:

```java
public boolean connect() {
    return reader.connectMeter("/dev/cu.PL2303G-USBtoUART140") != null;
}
```

The exact meter communication implementation is contained inside the meter library.

This approach makes it possible to replace or update the meter communication implementation without restructuring the JavaFX application.

---

## Dependencies

The application uses the following major dependencies:

* JavaFX Controls
* JavaFX Graphics
* jSerialComm
* Gurux DLMS
* Custom Egypt Meters Meter Library

The exact dependency versions are defined in `pom.xml`.

---

## Running the Application

### Requirements

For development, install:

* JDK 21
* Maven
* IntelliJ IDEA

The application is currently developed and tested on macOS.

### Run with Maven

From the project directory:

```bash
mvn clean javafx:run
```

---

## Meter Connection

The application currently uses a serial connection to communicate with the meter.

The serial port is passed to the meter library by `MeterService`.

For development environments, the port may look like:

```text
/dev/cu.PL2303G-USBtoUART140
```

The appropriate serial port depends on the connected USB-to-serial device and operating system.

---

## Meter Authentication

Before connecting to a meter, the application requests the meter password.

The password must be confirmed before the meter connection process is started.

The password confirmation is currently implemented as a simple application-level check.

---

## Security

The application uses secure local storage for user authentication information.

Passwords are not stored directly as plain text.

The meter password confirmation is currently implemented for the application's meter connection workflow and should be replaced with a more secure configuration mechanism if the application is deployed in a production environment.

---

## Development Status

The project is currently under active development.

Current functionality includes:

* [x] Login
* [x] Logout
* [x] Local credential storage
* [x] Change password
* [x] Home screen
* [x] Meter connection interface
* [x] Meter password confirmation
* [x] Meter reading table
* [x] Multiple reading selection
* [x] Meter JAR integration
* [x] jSerialComm integration
* [x] Gurux DLMS integration

Planned improvements include:

* [ ] Automatic serial-port detection
* [ ] Improved meter connection error handling
* [ ] More meter readings
* [ ] Meter information display
* [ ] Improved connection management
* [ ] Production Windows installer

---

## Repository

The source code and build configuration are maintained in this Git repository.

The repository contains the application source code, Maven configuration, resources, and the required meter library.

---

## License

This project is currently intended for internal development and testing for Egypt Meters Team.

License information will be added when the project is prepared for wider distribution.
