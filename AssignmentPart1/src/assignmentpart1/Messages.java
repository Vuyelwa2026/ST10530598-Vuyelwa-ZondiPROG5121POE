/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignmentpart1;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Messages {
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String messageStatus;

    // Global fields for tracking state
    private static int messageCounter = 0;
    private static Random random = new Random();

    // Collections
    private static ArrayList<Messages> sentMessages = new ArrayList<>();
    private static ArrayList<Messages> disregardedMessages = new ArrayList<>();

    private static int totalMessages = 0;

    // Constructor
    public Messages(String messageID, int messageNumber, String recipient, String message, String messageHash, String messageStatus) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = messageHash;
        this.messageStatus = messageStatus;
    }

    // Getters (used by StoredMessages)
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public String getMessageStatus() { return messageStatus; }

    // ADDED: access to the arrays (needed by StoredMessages and the unit tests)
    public static ArrayList<Messages> getSentMessages() { return sentMessages; }

    public static ArrayList<Messages> getDisregardedMessages() { return disregardedMessages; }

    // ADDED: clears the arrays so every unit test starts clean
    public static void resetForTesting() {
        sentMessages.clear();
        disregardedMessages.clear();
        totalMessages = 0;
    }

    // Keeps message numbering going after loading stored messages
    public static void setMessageCounterAtLeast(int number) {
        if (number > messageCounter) {
            messageCounter = number;
        }
    }

    // Called from AssignmentPart1 after successful login
    public static void startQuickChatApp(Scanner input, Login userProfile) {
        StoredMessages.loadFromJSON();

        int option = 0;

        while (option != 4) {
            System.out.println("\n=============================");
            System.out.println("          MAIN MENU");
            System.out.println("=============================");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Stored messages");
            System.out.println("4. Quit");
            System.out.print("Enter your option: ");

            while (!input.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number (1, 2, 3 or 4): ");
                input.next();
            }
            option = input.nextInt();
            input.nextLine(); // consume newline

            switch (option) {
                case 1:
                    startQuickChat(input);
                    break;

                case 2:
                    System.out.println("\n--- Recently Sent Messages ---");
                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages sent yet.");
                    } else {
                        for (Messages msg : sentMessages) {
                            displayMessage(msg);
                        }
                    }
                    System.out.println("Total Messages Sent: " + returnTotalMessages());
                    break;

                case 3:
                    StoredMessages.displayStoredMessagesMenu(input);
                    break;

                case 4:
                    System.out.println("Thank you for using QuickChat, " + userProfile.getFirstName() + ".");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        input.close();
    }

    // Start QuickChat Flow Loop
    public static void startQuickChat(Scanner input) {
        System.out.println("\n==========================");
        System.out.println("    Welcome to QuickChat.");
        System.out.println("============================");

        System.out.println("How many messages would you like to enter?");
        int count = readPositiveNumber(input);

        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Entering Message " + (i + 1) + " of " + count + " ---");
            sendMessage(input);
        }
    }

    // Read a positive number
    public static int readPositiveNumber(Scanner input) {
        while (true) {
            try {
                int number = Integer.parseInt(input.nextLine());
                if (number > 0) {
                    return number;
                }
                System.out.print("Enter a number greater than zero: ");
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid whole number: ");
            }
        }
    }

    // Send a message
    public static void sendMessage(Scanner input) {
        String id = generateMessageID();

        System.out.print("Enter recipient cellphone number (e.g. +27821234567): ");
        String recipient = input.nextLine();

        if (!isValidRecipient(recipient)) {
            System.out.println("Invalid recipient cellphone number.");
            return;
        }

        System.out.print("Enter your message: ");
        String message = input.nextLine();

        if (message.trim().isEmpty() || message.length() >= 250) {
            System.out.println("Please enter a message of less than 250 characters.");
            return;
        }

        messageCounter++;

        String hash = generateMessageHash(id, messageCounter, message);

        Messages newMessage = new Messages(
                id, messageCounter, recipient,
                message, hash, "Pending");

        System.out.println("\nMessage ID: " + id);
        System.out.println("Message Hash: " + hash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + message);

        System.out.println("\nChoose an action:");
        System.out.println("1. Send Message");
        System.out.println("0. Disregard Message");
        System.out.println("2. Store Message to send later");
        System.out.print("Your choice: ");

        String action = input.nextLine();

        switch (action) {
            case "1":
                newMessage.messageStatus = "Sent";
                sentMessages.add(newMessage);
                totalMessages++;

                System.out.println("Message successfully sent.");
                displayMessage(newMessage);
                break;

            case "0":
                newMessage.messageStatus = "Disregarded";
                disregardedMessages.add(newMessage);

                System.out.println("Message disregarded successfully.");
                break;

            case "2":
                newMessage.messageStatus = "Stored";
                StoredMessages.addStoredMessage(newMessage);
                break;

            default:
                System.out.println("Invalid action. Message not sent.");
        }
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public static String generateMessageID() {
        long id = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    public static boolean isValidRecipient(String recipient) {
        return recipient.matches("\\+[1-9][0-9]{7,14}");
    }

    public static String generateMessageHash(String id, int number, String message) {
        String[] words = message.trim().split("\\s+");

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return (id.substring(0, 2) + ":"
                + number + ":"
                + firstWord + lastWord).toUpperCase();
    }

    public static void displayMessage(Messages msg) {
        System.out.println("\n--- Message Details ---");
        System.out.println("Message ID: " + msg.messageID);
        System.out.println("Message Hash: " + msg.messageHash);
        System.out.println("Recipient: " + msg.recipient);
        System.out.println("Message: " + msg.message);
        System.out.println("Status: " + msg.messageStatus);
    }
}