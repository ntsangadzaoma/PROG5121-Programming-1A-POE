
import java.util.Scanner;

/**
 * QuickChat console application for Part 1, Part 2 and Part 3.
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Registration ===");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your South African cell phone number (e.g. +27838968976): ");
        String cellPhoneNumber = scanner.nextLine();

        Login login = new Login(
                username,
                password,
                cellPhoneNumber,
                firstName,
                lastName
        );

        System.out.println();
        System.out.println(login.registerUser());

        if (!login.checkUserName()
                || !login.checkPasswordComplexity()
                || !login.checkCellPhoneNumber()) {

            System.out.println();
            System.out.println("Registration could not be completed. Please correct the information and try again.");
            scanner.close();
            return;
        }

        System.out.println();
        System.out.println("=== Login ===");

        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();

        login.setLoginCredentials(loginUsername, loginPassword);

        System.out.println(login.returnLoginStatus());

        if (!login.loginUser()) {
            scanner.close();
            return;
        }

        System.out.println();
        System.out.println("Welcome to QuickChat.");

        Message.loadStoredMessages();

        System.out.print("How many messages would you like to enter? ");

        int numberOfMessages;

        try {
            numberOfMessages = Integer.parseInt(scanner.nextLine());

            if (numberOfMessages < 0) {
                System.out.println("Please enter a valid number of messages.");
                scanner.close();
                return;
            }

        } catch (NumberFormatException exception) {
            System.out.println("Please enter a valid number of messages.");
            scanner.close();
            return;
        }

        int messageNumber = 0;
        boolean running = true;

        // Keep the main menu available until the user chooses Quit.
        while (running) {

            System.out.println();
            System.out.println("=== QuickChat Main Menu ===");
            System.out.println("Messages entered: "
                    + messageNumber + "/" + numberOfMessages);
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");

            System.out.print("Select an option: ");
            String option = scanner.nextLine();

            switch (option) {

                case "1":

                    if (messageNumber >= numberOfMessages) {
                        System.out.println(
                                "You have reached the number of messages you requested."
                        );
                        break;
                    }

                    System.out.print("Enter recipient cell number: ");
                    String recipient = scanner.nextLine();

                    System.out.print("Enter message: ");
                    String text = scanner.nextLine();

                    Message message = new Message(
                            recipient,
                            text,
                            messageNumber
                    );

                    message.setSender(firstName + " " + lastName);

                    if (!message.checkMessageID()) {
                        System.out.println("Message ID is not valid.");
                        break;
                    }

                    String recipientResult = message.checkRecipientCell();
                    System.out.println(recipientResult);

                    String lengthResult = message.checkMessageLength();
                    System.out.println(lengthResult);

                    if (!recipientResult.equals(
                            "Cell phone number successfully captured.")) {
                        break;
                    }

                    if (!lengthResult.equals("Message ready to send.")) {
                        System.out.println(
                                "Please enter a message of less than 250 characters."
                        );
                        break;
                    }

                    message.createMessageHash();

                    System.out.print("Choose Send, Disregard or Store: ");
                    String action = scanner.nextLine();

                    String actionResult = message.SentMessage(action);
                    System.out.println(actionResult);

                    if (actionResult.equals("Invalid choice.")) {
                        break;
                    }

                    if (action.trim().equalsIgnoreCase("send")) {
                        System.out.println();
                        System.out.println("Message ID: "
                                + message.getMessageID());
                        System.out.println("Message Hash: "
                                + message.getMessageHash());
                        System.out.println("Sender: "
                                + message.getSender());
                        System.out.println("Recipient: "
                                + message.getRecipient());
                        System.out.println("Message: "
                                + message.getMessage());
                    }

                    messageNumber++;
                    break;

                case "2":

                    Message sentMessages = new Message("", "", 0);
                    System.out.println(sentMessages.printMessages());
                    break;

                case "3":

                    System.out.println("Goodbye.");
                    running = false;
                    break;

                case "4":

                    storedMessagesMenu(scanner);
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please select 1, 2, 3 or 4."
                    );
            }
        }

        System.out.println();
        System.out.println(
                "Total messages sent: "
                        + new Message("", "", 0).returnTotalMessagess()
        );

        scanner.close();
    }

    /**
     * Stored Messages submenu.
     */
    public static void storedMessagesMenu(Scanner scanner) {

        boolean returnToMainMenu = false;

        while (!returnToMainMenu) {

            System.out.println();
            System.out.println("=== Stored Messages ===");
            System.out.println("1) Display sender and recipient of stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message using its ID");
            System.out.println("4) Search for messages using a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display the stored messages report");
            System.out.println("7) Return to main menu");

            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    System.out.println();
                    System.out.println(
                            Message.displayStoredSendersAndRecipients()
                    );
                    break;

                case "2":
                    System.out.println();
                    System.out.println("Longest stored message:");
                    System.out.println(
                            Message.displayLongestStoredMessage()
                    );
                    break;

                case "3":
                    System.out.print("Enter the message ID: ");
                    String messageID = scanner.nextLine();

                    System.out.println(
                            Message.searchStoredMessageByID(messageID)
                    );
                    break;

                case "4":
                    System.out.print("Enter the recipient cell number: ");
                    String recipient = scanner.nextLine();

                    System.out.println(
                            Message.searchStoredMessagesByRecipient(recipient)
                    );
                    break;

                case "5":
                    System.out.print("Enter the message hash: ");
                    String messageHash = scanner.nextLine();

                    System.out.println(
                            Message.deleteStoredMessageByHash(messageHash)
                    );
                    break;

                case "6":
                    System.out.println();
                    System.out.println(
                            Message.displayStoredMessagesReport()
                    );
                    break;

                case "7":
                    returnToMainMenu = true;
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please select a number from 1 to 7."
                    );
            }
        }
    }
}
