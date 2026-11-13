# PROG5121 Programming 1A — Portfolio of Evidence

## Project Overview

This project implements the PROG5121 Programming 1A Portfolio of Evidence (POE), consisting of three parts:

- **Part 1:** Registration and Login
- **Part 2:** Sending Messages
- **Part 3:** Store Data and Display Task Report

The application is a Java-based console application developed using Apache Maven. It includes user registration, login authentication, message validation, message hashing, message sending, JSON storage, message searching, deletion and reporting.

## Project Structure

### Part 1 — Registration and Login

- `Login.java` — user registration, validation and login methods.
- `LoginTest.java` — JUnit tests for registration and login validation.
- `Main.java` — console application containing registration, login and messaging functionality.

### Part 2 — Sending Messages

- `Message.java` — message ID generation, recipient validation, message length validation, message hashing, sending, disregarding, printing and JSON storage.
- `MessageTest.java` — JUnit tests covering message validation, ID generation, hashing, sending and JSON storage.

### Part 3 — Store Data and Display Task Report

- `Message.java` — dynamically populated message arrays, JSON reading, stored-message searching, deletion and report generation.
- `Main.java` — includes the Stored Messages menu and its associated operations.
- `Part3Test.java` — six JUnit tests covering the Part 3 requirements.

### Project Configuration

- `pom.xml` — Maven configuration, including JUnit 5 and Gson dependencies.
- `.gitignore` — excludes generated build files, IDE files and local message storage.
- `.github/workflows/TestJava.yml` — GitHub Actions workflow for automated testing.

## Part 1 — Required Functionality

The `Login` class implements:

- `checkUserName()`
- `checkPasswordComplexity()`
- `checkCellPhoneNumber()`
- `registerUser()`
- `loginUser()`
- `returnLoginStatus()`

The application validates usernames, passwords and cellphone numbers during registration and login.

## Part 2 — Message Functionality

The `Message` class implements:

- `checkMessageID()`
- `checkRecipientCell()`
- `createMessageHash()`
- `SentMessage()`
- `printMessages()`
- `returnTotalMessagess()`
- `storeMessage()`

The application supports sending, disregarding and storing messages. Messages selected for storage are saved in JSON format using Gson.

## Part 3 — Store Data and Display Task Report

Part 3 extends the messaging application with five dynamically populated arrays:

1. **Sent Messages** — contains all sent messages.
2. **Disregarded Messages** — contains all disregarded messages.
3. **Stored Messages** — contains messages loaded from the JSON file.
4. **Message Hash** — contains message hashes.
5. **Message ID** — contains message IDs.

The application includes a fourth main menu option called **Stored Messages**.

The Stored Messages menu provides the following functionality:

- Display the sender and recipient of all stored messages.
- Display the longest stored message.
- Search for a message using its message ID and display the corresponding recipient and message.
- Search for all stored messages belonging to a particular recipient.
- Delete a stored message using its message hash.
- Display a report containing the details of all stored messages.

## QuickChat Features

The application provides:

1. User registration and login.
2. A messaging menu with four options:
   - Send Messages.
   - Show recently sent messages.
   - Quit.
   - Stored Messages.
3. Generation and validation of 10-digit message IDs.
4. Recipient cellphone number validation.
5. Message length validation (maximum 250 characters).
6. Automatic message hash generation.
7. Send, Disregard and Store message actions.
8. JSON storage and retrieval using Gson.
9. Dynamic message arrays.
10. Stored-message searching and deletion.
11. Stored-message report generation.

## Technologies Used

- Java 17
- Apache Maven
- JUnit 5
- Gson 2.11.0
- Git and GitHub
- GitHub Actions

## Running the Application

### Requirements

- Java Development Kit (JDK) 17 or later.
- Apache Maven.
- An IDE such as Visual Studio Code or NetBeans.

### Run the application

Open the project directory in a terminal and execute:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="Main"
```

### Run automated tests

Execute:

```bash
mvn clean test
```

## JSON Message Storage

Messages selected for storage are saved in `messages.json` in the project directory.

The JSON records include:

- Message ID.
- Message number.
- Sender.
- Recipient.
- Message content.
- Message hash.
- Message status.

The application uses the Gson library for JSON processing.

The `messages.json` file is excluded from Git version control through `.gitignore`.

## Automated Testing

JUnit 5 is used for automated testing.

The project currently contains **29 automated tests**:

| Test class | Number of tests |
|---|---:|
| LoginTest | 8 |
| MessageTest | 15 |
| Part3Test | 6 |
| **Total** | **29** |

The latest local test execution completed successfully:

```text
Tests run: 29
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Run the tests using:

```bash
mvn clean test
```

## Continuous Integration

The project includes a GitHub Actions workflow:

`.github/workflows/TestJava.yml`

The workflow automatically executes Maven tests when changes are pushed or pull requests are created.

It uses Java 17 and Maven to verify the application.

## Version Control

The project uses Git and GitHub for version control.

Development is organised into separate branches for the different POE parts:

- `part-2-development`
- `part-3-development`

Completed work is merged into the main branch through GitHub pull requests after testing.

## References

1. Stack Overflow — Java regular expression for South African cellphone number validation:  
   https://stackoverflow.com/questions/33477950/java-regex-phone-number

2. Google Gson — User Guide:  
   https://github.com/google/gson/blob/main/UserGuide.md

3. JUnit 5 — User Guide:  
   https://junit.org/junit5/docs/current/user-guide/

4. Apache Maven — Documentation:  
   https://maven.apache.org/guides/
