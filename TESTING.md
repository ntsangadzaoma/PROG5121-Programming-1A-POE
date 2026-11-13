# PROG5121 — Testing Documentation

This document describes the manual and automated testing performed for all three parts of the PROG5121 Programming 1A Portfolio of Evidence (POE).

## Part 1 — Registration and Login

### 1. Username Validation

Valid username:

```text
kyl_1
```

Expected result: Username successfully captured.

Invalid username:

```text
kyle!!!!!!!
```

Expected result: Username is rejected because it does not contain an underscore and exceeds five characters.

### 2. Password Validation

Valid password:

```text
Ch&&sec@ke99!
```

Expected result: Password successfully captured.

Invalid password:

```text
password
```

Expected result: Password is rejected because it does not meet the password complexity requirements.

### 3. Cell Phone Validation

Valid cellphone number:

```text
+27838968976
```

Expected result: Cell phone number successfully added.

Invalid cellphone number:

```text
08966553
```

Expected result: Cellphone number is rejected because it does not use the required international format.

### 4. Login Testing

The login tests verify that registered usernames and passwords match the credentials entered during login.

The tests cover:

- Successful login with correct credentials.
- Failed login with incorrect credentials.

## Part 2 — Sending Messages

### 1. Message ID Validation

The application generates a random 10-digit message ID.

Tests verify:

- A generated 10-digit ID is accepted.
- An ID that is too short is rejected.
- An ID that is too long is rejected.
- An ID containing letters is rejected.

### 2. Recipient Cellphone Validation

Valid recipient:

```text
+27718693002
```

Expected result: Cell phone number successfully captured.

Invalid recipient:

```text
08575975889
```

Expected result: Recipient number is rejected because it does not contain the required international code.

### 3. Message Length Validation

Valid message:

- A message containing 250 characters.

Expected result: Message ready to send.

Invalid message:

- A message containing 251 characters.

Expected result: Message is rejected and the number of excess characters is displayed.

### 4. Message Hash Testing

The message hash is generated using:

- The first two digits of the message ID.
- The message number.
- The first and last words of the message.

Example expected hash:

```text
00:0:HITONIGHT
```

The test verifies that the generated hash matches the expected value.

### 5. Message Actions

The application supports three message actions.

**Send**

Expected result:

```text
Message successfully sent.
```

The total number of sent messages increases.

**Disregard**

Expected result:

```text
Press 0 to delete the message.
```

The disregarded message is not counted as sent.

**Store**

Expected result:

```text
Message successfully stored.
```

The message is saved to `messages.json` and is not counted as sent.

### 6. JSON Storage Testing

The JSON storage tests verify that:

- A JSON file is created when a message is stored.
- Multiple messages can be stored.
- Message IDs and message details are preserved.
- Special characters and line breaks are correctly preserved.
- Stored messages have the correct status.

### 7. Message Printing

The tests verify that `printMessages()` returns message details, including:

- Message ID.
- Message hash.
- Recipient.
- Message content.

### 8. Total Messages Testing

The tests verify that successfully sent messages increase the total message count, while stored and disregarded messages do not increase the sent-message count.

## Part 3 — Store Data and Display Task Report

Part 3 introduces five dynamically populated arrays:

1. Sent Messages.
2. Disregarded Messages.
3. Stored Messages.
4. Message Hash.
5. Message ID.

The application also reads stored messages from `messages.json` and provides additional operations through the Stored Messages menu.

### 1. Sent Messages Array

The test verifies that sent messages are correctly added to the Sent Messages array.

The supplied test messages include:

```text
Did you get the cake?
It is dinner time!
```

Expected result: Both messages are present in the Sent Messages array.

### 2. Display Longest Stored Message

The test stores two messages and checks that the longest message is returned.

Expected result:

```text
Where are you? You are late! I have asked you to be on time.
```

### 3. Search by Message ID

The test searches for a message using its message ID.

Test message ID:

```text
0838884567
```

Expected result: The corresponding recipient and message are returned.

The supplied assignment data identifies Message 4 as a sent message, and the search implementation also checks sent and disregarded messages.

### 4. Search Messages by Recipient

The test stores messages for recipient:

```text
+27838884567
```

Expected result: Both messages associated with that recipient are returned.

### 5. Delete a Stored Message by Hash

The test stores two messages and deletes one using its message hash.

Expected result:

- The deletion confirmation is returned.
- The deleted message is removed from the JSON storage.
- The remaining stored message is still available.

### 6. Display Stored Messages Report

The test verifies that the stored-message report contains:

- Message ID.
- Message hash.
- Sender.
- Recipient.
- Message content.
- Message status.

Expected result: The report contains the details of all stored messages.

## Automated Testing

The project uses JUnit 5 for automated testing and Apache Maven to execute the test suite.

### Test Classes

| Test class | Part | Tests |
|---|---|---:|
| LoginTest | Part 1 | 8 |
| MessageTest | Part 2 | 15 |
| Part3Test | Part 3 | 6 |
| **Total** | | **29** |

### Running the Tests

Execute the following command from the project directory:

```bash
mvn clean test
```

### Latest Local Test Results

The latest completed test execution returned:

```text
Tests run: 29
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

All 29 automated tests passed.

## Continuous Integration — GitHub Actions

The project includes an automated GitHub Actions workflow:

```text
.github/workflows/TestJava.yml
```

The workflow:

- Runs on pushes.
- Runs on pull requests.
- Sets up Java 17 using Temurin.
- Uses Maven to execute the automated tests.

The workflow executes:

```bash
mvn -B test
```

This allows the project tests to be checked automatically when changes are pushed to GitHub or submitted through a pull request.
