/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package assignmentpart1;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class StoredMessages {

    private static ArrayList<Messages> storedMessages = new ArrayList<>();

    private static final String JSON_FILE = "messages.json";

    // Add a stored message
    public static void addStoredMessage(Messages msg) {

        storedMessages.add(msg);

        saveToJSON();

        System.out.println("Message successfully stored.");
    }

    // Load stored messages from JSON
    public static void loadFromJSON() {

        storedMessages.clear();

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(JSON_FILE)) {

            JSONArray messages = (JSONArray) parser.parse(reader);

            for (Object object : messages) {

                JSONObject jsonMessage = (JSONObject) object;

                String id = (String) jsonMessage.get("messageID");

                int number = ((Number) jsonMessage.get("messageNumber")).intValue();

                String recipient = (String) jsonMessage.get("recipient");

                String message = (String) jsonMessage.get("message");

                String hash = (String) jsonMessage.get("messageHash");

                String status = (String) jsonMessage.get("status");

                Messages msg = new Messages(
                        id, number, recipient, message, hash, status
                );

                storedMessages.add(msg);

                Messages.setMessageCounterAtLeast(number);
            }

            System.out.println("Stored messages loaded successfully.");

        } catch (IOException | ParseException e) {

            // File may not exist yet.
            System.out.println("No existing stored messages found.");

        }
    }

    // Save messages to JSON
    @SuppressWarnings("unchecked")
    public static void saveToJSON() {

        JSONArray messages = new JSONArray();

        for (Messages msg : storedMessages) {

            JSONObject jsonMessage = new JSONObject();

            jsonMessage.put("messageID", msg.getMessageID());
            jsonMessage.put("messageNumber", msg.getMessageNumber());
            jsonMessage.put("messageHash", msg.getMessageHash());
            jsonMessage.put("recipient", msg.getRecipient());
            jsonMessage.put("message", msg.getMessage());
            jsonMessage.put("status", msg.getMessageStatus());

            messages.add(jsonMessage);
        }

        try (FileWriter writer = new FileWriter(JSON_FILE)) {

            writer.write(messages.toJSONString());

            System.out.println("Messages saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving messages: " + e.getMessage());
        }
    }

    // Display stored messages
    public static void displayStoredMessagesMenu(Scanner input) {

        int option = 0;

        while (option != 7) {

            System.out.println("\n==============================");
            System.out.println("    STORED MESSAGE OPTIONS");
            System.out.println("==============================");
            System.out.println("1. Display all stored messages");
            System.out.println("2. Search message by ID");
            System.out.println("3. Search messages by recipient");
            System.out.println("4. Display longest message");
            System.out.println("5. Delete message by hash");
            System.out.println("6. Display full message report");
            System.out.println("7. Return to main menu");
            System.out.print("Enter your option: ");

            try {
                option = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            switch (option) {

                case 1:
                    displayAllStoredMessages();
                    break;

                case 2:
                    searchMessageByID(input);
                    break;

                case 3:
                    searchMessagesByRecipient(input);
                    break;

                case 4:
                    displayLongestMessage();
                    break;

                case 5:
                    deleteMessageByHash(input);
                    break;

                case 6:
                    displayFullReport();
                    break;

                case 7:
                    System.out.println("Returning to main menu.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Option 1
    public static void displayAllStoredMessages() {

        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages available.");
            return;
        }

        for (Messages msg : storedMessages) {
            Messages.displayMessage(msg);
        }
    }

    // Option 2
    public static void searchMessageByID(Scanner input) {

        System.out.print("Enter message ID: ");
        String id = input.nextLine();

        for (Messages msg : storedMessages) {

            if (msg.getMessageID().equals(id)) {

                Messages.displayMessage(msg);
                return;
            }
        }

        System.out.println("Message not found.");
    }

    // Option 3
    public static void searchMessagesByRecipient(Scanner input) {

        System.out.print("Enter recipient cellphone number: ");
        String recipient = input.nextLine();

        boolean found = false;

        for (Messages msg : storedMessages) {

            if (msg.getRecipient().equals(recipient)) {

                Messages.displayMessage(msg);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // Option 4
    public static void displayLongestMessage() {

        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages available.");
            return;
        }

        Messages longest = storedMessages.get(0);

        for (Messages msg : storedMessages) {

            if (msg.getMessage().length() > longest.getMessage().length()) {
                longest = msg;
            }
        }

        System.out.println("Longest stored message:");
        Messages.displayMessage(longest);
    }

    // Option 5
    public static void deleteMessageByHash(Scanner input) {

        System.out.print("Enter the message hash to delete: ");
        String hash = input.nextLine();

        boolean removed = storedMessages.removeIf(
                msg -> msg.getMessageHash().equals(hash)
        );

        if (removed) {

            saveToJSON();

            System.out.println("Message successfully deleted.");

        } else {

            System.out.println("Message hash not found.");
        }
    }

    // Option 6
    public static void displayFullReport() {

        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages available.");
            return;
        }

        System.out.println("\n========== FULL MESSAGE REPORT ==========");

        for (Messages msg : storedMessages) {

            System.out.println("\nSender: You");
            System.out.println("Recipient: " + msg.getRecipient());
            System.out.println("Message: " + msg.getMessage());
            System.out.println("Message Hash: " + msg.getMessageHash());
            System.out.println("Message ID: " + msg.getMessageID());
            System.out.println("Status: " + msg.getMessageStatus());
        }

        System.out.println("\nTotal stored messages: " + storedMessages.size());
    }

    // Access stored messages
    public static ArrayList<Messages> getStoredMessages() {
        return storedMessages;
    }
}