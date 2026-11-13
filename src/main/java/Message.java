
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private static final List<Message> SENT_MESSAGES = new ArrayList<>();
    private static final List<Message> DISREGARDED_MESSAGES = new ArrayList<>();
    private static final List<Message> STORED_MESSAGES = new ArrayList<>();

    // Five dynamically populated arrays required for Part 3.
    private static Message[] sentMessages = new Message[0];
    private static Message[] disregardedMessages = new Message[0];
    private static Message[] storedMessages = new Message[0];
    private static String[] messageHashes = new String[0];
    private static String[] messageIDs = new String[0];

    private static int totalMessagesSent = 0;

    private static final String JSON_FILE = "messages.json";

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String sender;
    private String message;
    private String messageHash;
    private String status;

    /** Creates a message with a generated 10-digit ID. */
    public Message(String recipient, String message, int messageNumber) {
        this(generateMessageID(), messageNumber, recipient, message);
    }

    /** Constructor used for deterministic testing. */
    public Message(String messageID, int messageNumber,
                   String recipient, String message) {

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.sender = "Unknown";
        this.messageHash = createMessageHash();
        this.status = "";
    }

    /** Compatibility constructor. */
    public Message(String messageID, String recipient,
                   String message, int messageNumber) {

        this(messageID, messageNumber, recipient, message);
    }

    /** Generates a random 10-digit message ID. */
    private static String generateMessageID() {
        long value = ThreadLocalRandom.current().nextLong(
                1_000_000_000L,
                10_000_000_000L
        );

        return String.valueOf(value);
    }

    /** Validates that the message ID contains exactly 10 digits. */
    public Boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    /** Validates a South African international cell phone number. */
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /** Validates the 250-character message limit. */
    public String checkMessageLength() {
        if (message != null && message.length() <= 250) {
            return "Message ready to send.";
        }

        int length = message == null ? 0 : message.length();
        int excess = Math.max(0, length - 250);

        return "Message exceeds 250 characters by " + excess
                + "; please reduce the size.";
    }

    /** Creates the required message hash. */
    public String createMessageHash() {
        String idPart = messageID == null
                ? ""
                : messageID.substring(0, Math.min(2, messageID.length()));

        String firstWord = "";
        String lastWord = "";

        if (message != null) {
            String cleaned = message.trim();

            if (!cleaned.isEmpty()) {
                String[] words = cleaned.split("\\s+");

                firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
                lastWord = words[words.length - 1]
                        .replaceAll("[^A-Za-z0-9]", "");
            }
        }

        messageHash = (idPart + ":" + messageNumber
                + ":" + firstWord + lastWord).toUpperCase();

        return messageHash;
    }

    /** Records the selected message action. */
    public String SentMessage(String choice) {
        if (choice == null) {
            return "Invalid choice.";
        }

        String selected = choice.trim().toLowerCase();

        switch (selected) {
            case "send":
                status = "sent";

                if (!SENT_MESSAGES.contains(this)) {
                    SENT_MESSAGES.add(this);
                    totalMessagesSent++;
                }

                refreshArrays();
                return "Message successfully sent.";

            case "disregard":
            case "discard":
                status = "disregarded";

                if (!DISREGARDED_MESSAGES.contains(this)) {
                    DISREGARDED_MESSAGES.add(this);
                }

                refreshArrays();
                return "Press 0 to delete the message.";

            case "store":
                status = "stored";
                storeMessage();
                return "Message successfully stored.";

            default:
                return "Invalid choice.";
        }
    }

    /** Interactive version used by the console application. */
    public String SentMessage() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Choose Send, Disregard or Store: ");

        return SentMessage(scanner.nextLine());
    }

    /** Displays messages sent during the current application run. */
    public String printMessages() {
        if (SENT_MESSAGES.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder output = new StringBuilder();

        for (Message sent : SENT_MESSAGES) {
            output.append("Message ID: ")
                    .append(sent.messageID)
                    .append(System.lineSeparator());

            output.append("Message Hash: ")
                    .append(sent.messageHash)
                    .append(System.lineSeparator());

            output.append("Recipient: ")
                    .append(sent.recipient)
                    .append(System.lineSeparator());

            output.append("Message: ")
                    .append(sent.message)
                    .append(System.lineSeparator())
                    .append(System.lineSeparator());
        }

        return output.toString().trim();
    }

    /** Returns the total number of messages sent. */
    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    /** Reads the existing JSON file or creates an empty JSON array. */
    private static JsonArray readJsonArray(Path path) throws IOException {
        if (!Files.exists(path)) {
            return new JsonArray();
        }

        String existingJson = Files.readString(
                path,
                StandardCharsets.UTF_8
        ).trim();

        if (existingJson.isEmpty()) {
            return new JsonArray();
        }

        return JsonParser.parseString(existingJson).getAsJsonArray();
    }

    /** Stores a message in messages.json using Gson. */
    public void storeMessage() {
        Path path = Paths.get(JSON_FILE);

        try {
            JsonArray messages = readJsonArray(path);

            JsonObject messageObject = new JsonObject();

            messageObject.addProperty("messageID", messageID);
            messageObject.addProperty("messageNumber", messageNumber);
            messageObject.addProperty("sender", sender);
            messageObject.addProperty("recipient", recipient);
            messageObject.addProperty("message", message);
            messageObject.addProperty("messageHash", messageHash);
            messageObject.addProperty("status", status);

            messages.add(messageObject);

            Files.writeString(
                    path,
                    GSON.toJson(messages),
                    StandardCharsets.UTF_8
            );

            loadStoredMessages();

        } catch (IOException | IllegalStateException
                 | com.google.gson.JsonParseException exception) {

            throw new IllegalStateException(
                    "Unable to store message as JSON.",
                    exception
            );
        }
    }

    /** Safely reads a string property from a JSON object. */
    private static String getJsonString(JsonObject object, String key) {
        if (!object.has(key) || object.get(key).isJsonNull()) {
            return "";
        }

        return object.get(key).getAsString();
    }

    /** Reads stored messages from JSON into the stored-message array. */
    public static void loadStoredMessages() {
        Path path = Paths.get(JSON_FILE);
        STORED_MESSAGES.clear();

        try {
            JsonArray messages = readJsonArray(path);

            for (int i = 0; i < messages.size(); i++) {
                JsonObject object = messages.get(i).getAsJsonObject();

                String id = getJsonString(object, "messageID");
                String recipient = getJsonString(object, "recipient");
                String messageText = getJsonString(object, "message");

                int number = object.has("messageNumber")
                        && !object.get("messageNumber").isJsonNull()
                        ? object.get("messageNumber").getAsInt()
                        : 0;

                Message stored = new Message(
                        id, number, recipient, messageText
                );

                stored.sender = getJsonString(object, "sender");
                stored.messageHash = getJsonString(object, "messageHash");
                stored.status = getJsonString(object, "status");

                if (stored.messageHash == null
                        || stored.messageHash.isEmpty()) {
                    stored.messageHash = stored.createMessageHash();
                }

                STORED_MESSAGES.add(stored);
            }

            refreshArrays();

        } catch (IOException | IllegalStateException
                 | com.google.gson.JsonParseException exception) {

            throw new IllegalStateException(
                    "Unable to read stored messages from JSON.",
                    exception
            );
        }
    }

    /** Refreshes all five arrays dynamically. */
    private static void refreshArrays() {
        sentMessages = SENT_MESSAGES.toArray(new Message[0]);

        disregardedMessages = DISREGARDED_MESSAGES.toArray(
                new Message[0]
        );

        storedMessages = STORED_MESSAGES.toArray(new Message[0]);

        List<String> hashes = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        // Collect hashes and IDs for all message categories.
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(SENT_MESSAGES);
        allMessages.addAll(DISREGARDED_MESSAGES);
        allMessages.addAll(STORED_MESSAGES);

        for (Message current : allMessages) {
            hashes.add(current.getMessageHash());
            ids.add(current.getMessageID());
        }

        messageHashes = hashes.toArray(new String[0]);
        messageIDs = ids.toArray(new String[0]);
    }

    /** Displays the sender and recipient of every stored message. */
    public static String displayStoredSendersAndRecipients() {
        loadStoredMessages();

        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }

        StringBuilder output = new StringBuilder();

        for (Message current : storedMessages) {
            output.append("Sender: ")
                    .append(current.getSender())
                    .append(System.lineSeparator());

            output.append("Recipient: ")
                    .append(current.getRecipient())
                    .append(System.lineSeparator())
                    .append(System.lineSeparator());
        }

        return output.toString().trim();
    }

    /** Finds the longest stored message. */
    public static String displayLongestStoredMessage() {
        loadStoredMessages();

        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }

        Message longest = storedMessages[0];

        for (Message current : storedMessages) {
            if (current.getMessage().length()
                    > longest.getMessage().length()) {
                longest = current;
            }
        }

        return longest.getMessage();
    }

    /**
     * Searches sent, disregarded and stored messages using a message ID.
     * This also supports the supplied assignment test for Message 4,
     * which is marked as Sent.
     */
    public static String searchStoredMessageByID(String searchID) {
        loadStoredMessages();

        // Search sent messages.
        for (Message current : SENT_MESSAGES) {
            if (current.getMessageID().equals(searchID)) {
                return "Recipient: " + current.getRecipient()
                        + System.lineSeparator()
                        + "Message: " + current.getMessage();
            }
        }

        // Search disregarded messages.
        for (Message current : DISREGARDED_MESSAGES) {
            if (current.getMessageID().equals(searchID)) {
                return "Recipient: " + current.getRecipient()
                        + System.lineSeparator()
                        + "Message: " + current.getMessage();
            }
        }

        // Search stored messages.
        for (Message current : storedMessages) {
            if (current.getMessageID().equals(searchID)) {
                return "Recipient: " + current.getRecipient()
                        + System.lineSeparator()
                        + "Message: " + current.getMessage();
            }
        }

        return "Message not found.";
    }

    /** Searches for all stored messages belonging to a recipient. */
    public static String searchStoredMessagesByRecipient(
            String searchRecipient) {

        loadStoredMessages();

        StringBuilder output = new StringBuilder();

        for (Message current : storedMessages) {
            if (current.getRecipient().equals(searchRecipient)) {
                output.append(current.getMessage())
                        .append(System.lineSeparator());
            }
        }

        if (output.length() == 0) {
            return "No messages found for this recipient.";
        }

        return output.toString().trim();
    }

    /** Deletes a stored message using its message hash. */
    public static String deleteStoredMessageByHash(String searchHash) {
        loadStoredMessages();

        boolean deleted = false;
        String deletedMessageText = "";

        JsonArray updatedMessages = new JsonArray();

        for (Message current : storedMessages) {
            if (current.getMessageHash().equals(searchHash)) {
                deleted = true;
                deletedMessageText = current.getMessage();
            } else {
                JsonObject object = new JsonObject();

                object.addProperty("messageID", current.getMessageID());
                object.addProperty(
                        "messageNumber", current.getMessageNumber()
                );
                object.addProperty("sender", current.getSender());
                object.addProperty("recipient", current.getRecipient());
                object.addProperty("message", current.getMessage());
                object.addProperty(
                        "messageHash", current.getMessageHash()
                );
                object.addProperty("status", current.getStatus());

                updatedMessages.add(object);
            }
        }

        if (!deleted) {
            return "Message hash not found.";
        }

        try {
            Files.writeString(
                    Paths.get(JSON_FILE),
                    GSON.toJson(updatedMessages),
                    StandardCharsets.UTF_8
            );

            loadStoredMessages();

            return "Message: '" + deletedMessageText
                    + "' successfully deleted.";

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to delete stored message.",
                    exception
            );
        }
    }

    /** Displays a full report of all stored messages. */
    public static String displayStoredMessagesReport() {
        loadStoredMessages();

        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }

        StringBuilder report = new StringBuilder();

        report.append("STORED MESSAGES REPORT")
                .append(System.lineSeparator())
                .append("======================")
                .append(System.lineSeparator());

        for (Message current : storedMessages) {
            report.append("Message ID: ")
                    .append(current.getMessageID())
                    .append(System.lineSeparator());

            report.append("Message Hash: ")
                    .append(current.getMessageHash())
                    .append(System.lineSeparator());

            report.append("Sender: ")
                    .append(current.getSender())
                    .append(System.lineSeparator());

            report.append("Recipient: ")
                    .append(current.getRecipient())
                    .append(System.lineSeparator());

            report.append("Message: ")
                    .append(current.getMessage())
                    .append(System.lineSeparator());

            report.append("Status: ")
                    .append(current.getStatus())
                    .append(System.lineSeparator());

            report.append("----------------------")
                    .append(System.lineSeparator());
        }

        return report.toString().trim();
    }

    // Getters for the five Part 3 arrays.

    public static Message[] getSentMessages() {
        refreshArrays();
        return sentMessages.clone();
    }

    public static Message[] getDisregardedMessages() {
        refreshArrays();
        return disregardedMessages.clone();
    }

    public static Message[] getStoredMessages() {
        loadStoredMessages();
        return storedMessages.clone();
    }

    public static String[] getMessageHashes() {
        loadStoredMessages();
        return messageHashes.clone();
    }

    public static String[] getMessageIDs() {
        loadStoredMessages();
        return messageIDs.clone();
    }

    // Existing and additional getters.

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }

    /** Resets in-memory message state for tests. */
    public static void resetMessages() {
        SENT_MESSAGES.clear();
        DISREGARDED_MESSAGES.clear();
        STORED_MESSAGES.clear();

        sentMessages = new Message[0];
        disregardedMessages = new Message[0];
        storedMessages = new Message[0];
        messageHashes = new String[0];
        messageIDs = new String[0];

        totalMessagesSent = 0;
    }
}
