# PROG5121 Programming 1A — Part 3 Development Notes

## Overview

Part 3 of the PROG5121 Programming 1A Portfolio of Evidence extends the existing QuickChat application by introducing dynamic message arrays, JSON data retrieval, stored-message management and report generation.

The application continues to use Java 17 and the existing Maven project structure.

## 1. Dynamic Message Arrays

Five arrays are implemented in the `Message` class.

### Sent Messages

The Sent Messages array contains messages successfully sent during the application session.

Messages are added when the user selects the Send action.

### Disregarded Messages

The Disregarded Messages array contains messages that the user chooses not to send.

These messages are tracked separately from sent messages.

### Stored Messages

The Stored Messages array contains messages retrieved from the `messages.json` file.

The application loads the stored records into memory when stored-message operations are performed.

### Message Hash

The Message Hash array contains the hashes associated with messages across the message categories.

Hashes are generated using the message ID, message number, and the first and last words of the message.

### Message ID

The Message ID array contains the IDs associated with messages across the message categories.

The arrays are refreshed dynamically as message data changes.

## 2. JSON Data Storage and Retrieval

The application uses the Gson library to store and retrieve message data.

Stored messages are written to:

```text
messages.json
```

Each stored record contains:

- Message ID.
- Message number.
- Sender.
- Recipient.
- Message content.
- Message hash.
- Message status.

The application reads the JSON file and reconstructs message objects for the Stored Messages array.

The JSON file is excluded from Git version control through `.gitignore`.

## 3. Stored Messages Menu

A fourth option, Stored Messages, was added to the main application menu.

The submenu provides the following operations.

### Display Senders and Recipients

Displays the sender and recipient associated with each stored message.

### Display Longest Stored Message

Searches the stored-message array and returns the message containing the greatest number of characters.

### Search by Message ID

Searches for a message using its message ID and displays the corresponding recipient and message content.

The implementation also searches sent and disregarded messages to accommodate the supplied assignment test data.

### Search by Recipient

Searches stored messages for a specified recipient and displays all matching message contents.

### Delete by Message Hash

Locates a stored message using its hash, removes the matching record from JSON storage and reloads the stored-message array.

### Display Stored Messages Report

Generates a report containing the details of stored messages, including:

- Message ID.
- Message hash.
- Sender.
- Recipient.
- Message content.
- Message status.

## 4. Application Integration

Part 3 was integrated into the existing QuickChat application without removing the registration, login and messaging functionality developed in Parts 1 and 2.

The application retains its console-based interaction.

The main menu provides:

1. Send Messages.
2. Show recently sent messages.
3. Quit.
4. Stored Messages.

The Stored Messages submenu provides access to the additional data-management operations.

## 5. Unit Testing

A new test class was introduced:

```text
src/test/java/Part3Test.java
```

It contains six tests covering the Part 3 requirements.

| Test | Functionality |
|---|---|
| Test 1 | Sent Messages array population |
| Test 2 | Display longest stored message |
| Test 3 | Search using message ID |
| Test 4 | Search messages by recipient |
| Test 5 | Delete a message using its hash |
| Test 6 | Display stored-message report |

The existing Part 1 and Part 2 test classes were retained.

### Test Results

The complete test suite contains 29 tests:

- LoginTest: 8 tests.
- MessageTest: 15 tests.
- Part3Test: 6 tests.

Latest local execution:

```text
Tests run: 29
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## 6. Automated Testing

The project uses the existing GitHub Actions workflow:

```text
.github/workflows/TestJava.yml
```

The workflow runs Maven tests using Java 17 whenever changes are pushed or a pull request is created.

## 7. Technologies and Dependencies

- Java 17.
- Apache Maven.
- Gson 2.11.0.
- JUnit 5.10.2.
- Git and GitHub.
- GitHub Actions.

## 8. Development Branch

Part 3 was developed on the Git branch:

```text
part-3-development
```

The changes are tested locally before being pushed and submitted for integration into the main branch.
