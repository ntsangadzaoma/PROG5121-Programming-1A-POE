
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class Part3Test {

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    public void setUp() throws Exception {
        Message.resetMessages();
        Files.deleteIfExists(Path.of("messages.json"));

        message1 = new Message(
                "1111111111", 1,
                "+27834557896",
                "Did you get the cake?"
        );

        message2 = new Message(
                "2222222222", 2,
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time."
        );

        message3 = new Message(
                "3333333333", 3,
                "+27834484567",
                "Yohoooo, I am at your gate."
        );

        // Assignment data identifies Message 4's recipient as Developer.
        message4 = new Message(
                "0838884567", 4,
                "Developer",
                "It is dinner time!"
        );

        message5 = new Message(
                "5555555555", 5,
                "+27838884567",
                "Ok, I am leaving without you."
        );
    }

    @AfterEach
    public void tearDown() throws Exception {
        Message.resetMessages();
        Files.deleteIfExists(Path.of("messages.json"));
    }

    // TEST 1: Sent Messages array is correctly populated.
    @Test
    public void testSentMessagesArray() {
        message1.SentMessage("send");
        message4.SentMessage("send");

        Message[] sentMessages = Message.getSentMessages();

        assertEquals(2, sentMessages.length);
        assertEquals(
                "Did you get the cake?",
                sentMessages[0].getMessage()
        );
        assertEquals(
                "It is dinner time!",
                sentMessages[1].getMessage()
        );
    }

    // TEST 2: Display the longest stored message.
    @Test
    public void testDisplayLongestStoredMessage() {
        message2.SentMessage("store");
        message5.SentMessage("store");

        String longestMessage =
                Message.displayLongestStoredMessage();

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                longestMessage
        );
    }

    // TEST 3: Search for a message using its message ID.
    @Test
    public void testSearchMessageByID() {
        message4.SentMessage("send");

        String result =
                Message.searchStoredMessageByID("0838884567");

        assertTrue(result.contains("It is dinner time!"));
        assertTrue(result.contains("Developer"));
    }

    // TEST 4: Search for all stored messages belonging to a recipient.
    @Test
    public void testSearchMessagesByRecipient() {
        message2.SentMessage("store");
        message5.SentMessage("store");

        String result =
                Message.searchStoredMessagesByRecipient(
                        "+27838884567"
                );

        assertTrue(result.contains(
                "Where are you? You are late! I have asked you to be on time."
        ));

        assertTrue(result.contains(
                "Ok, I am leaving without you."
        ));
    }

    // TEST 5: Delete a stored message using its message hash.
    @Test
    public void testDeleteMessageByHash() {
        message2.SentMessage("store");
        message5.SentMessage("store");

        String messageHash = message2.getMessageHash();

        String result =
                Message.deleteStoredMessageByHash(messageHash);

        assertTrue(result.contains(
                "Where are you? You are late! I have asked you to be on time."
        ));

        Message[] remainingMessages = Message.getStoredMessages();

        assertEquals(1, remainingMessages.length);
        assertEquals(
                "Ok, I am leaving without you.",
                remainingMessages[0].getMessage()
        );
    }

    // TEST 6: Display a report of all stored messages.
    @Test
    public void testDisplayStoredMessagesReport() {
        message2.SentMessage("store");
        message5.SentMessage("store");

        String report =
                Message.displayStoredMessagesReport();

        assertTrue(report.contains("STORED MESSAGES REPORT"));

        assertTrue(report.contains("2222222222"));
        assertTrue(report.contains("5555555555"));

        assertTrue(report.contains(message2.getMessageHash()));
        assertTrue(report.contains(message5.getMessageHash()));

        assertTrue(report.contains("+27838884567"));

        assertTrue(report.contains(
                "Where are you? You are late! I have asked you to be on time."
        ));

        assertTrue(report.contains(
                "Ok, I am leaving without you."
        ));
    }
}
