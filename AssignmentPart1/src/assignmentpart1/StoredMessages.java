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

    // ADDED: sent + stored messages together (used by the search/longest options)
    private static ArrayList<Messages> allMessages() {
        ArrayList<Messages> all = new ArrayList<>(Messages.getSentMessages());
        all.addAll(storedMessages);
        return all;
    }

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
                    deleteStoredMessage(input);
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

    // Option 2  (CHANGED: searches sent + stored)
    public static void searchMessageByID(Scanner input) {

        System.out.print("Enter message ID: ");
        String id = input.nextLine();

        for (Messages msg : allMessages()) {

            if (msg.getMessageID().equals(id)) {

                Messages.displayMessage(msg);
                return;
            }
        }

        System.out.println("Message not found.");
    }

    // Option 3  (CHANGED: searches sent + stored)
    public static void searchMessagesByRecipient(Scanner input) {

        System.out.print("Enter recipient cellphone number: ");
        String recipient = input.nextLine();

        boolean found = false;

        for (Messages msg : allMessages()) {

            if (msg.getRecipient().equals(recipient)) {

                Messages.displayMessage(msg);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // Option 4  (CHANGED: searches sent + stored)
    public static void displayLongestMessage() {

        ArrayList<Messages> all = allMessages();

        if (all.isEmpty()) {
            System.out.println("No messages available.");
            return;
        }

        Messages longest = all.get(0);

        for (Messages msg : all) {

            if (msg.getMessage().length() > longest.getMessage().length()) {
                longest = msg;
            }
        }

        System.out.println("Longest message:");
        Messages.displayMessage(longest);
    }

    // Option 5  (CHANGED: prints the deleted message text)
    public static void deleteStoredMessage(Scanner input) {

        System.out.print("Enter the message hash to delete: ");
        String hash = input.nextLine();

        for (Messages msg : storedMessages) {

            if (msg.getMessageHash().equals(hash)) {

                storedMessages.remove(msg);

                saveToJSON();

                System.out.println("Message: \"" + msg.getMessage() + "\" successfully deleted.");
                return;
            }
        }

        System.out.println("Message hash not found.");
    }

    // Option 6  (CHANGED: report covers sent + stored messages)
    public static void displayFullReport() {

        ArrayList<Messages> all = allMessages();

        if (all.isEmpty()) {
            System.out.println("No messages available.");
            return;
        }

        System.out.println("\n========== FULL MESSAGE REPORT ==========");

        for (Messages msg : all) {

            System.out.println("\nSender: You");
            System.out.println("Recipient: " + msg.getRecipient());
            System.out.println("Message: " + msg.getMessage());
            System.out.println("Message Hash: " + msg.getMessageHash());
            System.out.println("Message ID: " + msg.getMessageID());
            System.out.println("Status: " + msg.getMessageStatus());
        }

        System.out.println("\nTotal messages: " + all.size());
    }

    // =====================================================================
    // ADDED: instance (array-based) version used by StoredMessagesUnitTest.
    // The static methods above are untouched, so the QuickChat app still works.
    // =====================================================================
    private final ArrayList<String> ids = new ArrayList<>();
    private final ArrayList<String> senders = new ArrayList<>();
    private final ArrayList<String> recipients = new ArrayList<>();
    private final ArrayList<String> texts = new ArrayList<>();
    private final ArrayList<String> hashes = new ArrayList<>();

    public StoredMessages(String[] messageIDs, String[] messageSenders,
                          String[] messageRecipients, String[] messageTexts,
                          String[] messageHashes, int count) {
        for (int i = 0; i < count; i++) {
            ids.add(messageIDs[i]);
            senders.add(messageSenders[i]);
            recipients.add(messageRecipients[i]);
            texts.add(messageTexts[i]);
            hashes.add(messageHashes[i]);
        }
    }

    private void printEntry(int i) {
        System.out.println("Message ID: " + ids.get(i));
        System.out.println("Message Hash: " + hashes.get(i));
        System.out.println("Sender: " + senders.get(i));
        System.out.println("Recipient: " + recipients.get(i));
        System.out.println("Message: " + texts.get(i));
    }

    // Messages array populated: shows sender, recipient and message
    public void displayStoredSendersAndRecipients() {
        for (int i = 0; i < texts.size(); i++) {
            System.out.println("\n--- Stored Message " + (i + 1) + " ---");
            System.out.println("Sender: " + senders.get(i));
            System.out.println("Recipient: " + recipients.get(i));
            System.out.println("Message: " + texts.get(i));
        }
    }

    // Longest message
    public String getLongestStoredMessage() {
        if (texts.isEmpty()) {
            System.out.println("No stored messages available.");
            return null;
        }

        int longest = 0;
        for (int i = 1; i < texts.size(); i++) {
            if (texts.get(i).length() > texts.get(longest).length()) {
                longest = i;
            }
        }

        System.out.println("\nLongest Stored Message:");
        printEntry(longest);
        return texts.get(longest);
    }

    // Search by message ID
    public void searchByMessageID(Scanner input) {
        System.out.print("Enter message ID: ");
        String id = input.nextLine().trim();

        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i).equals(id)) {
                System.out.println("Message found!");
                printEntry(i);
                return;
            }
        }
        System.out.println("Message ID not found.");
    }

    // Search all messages for a recipient
    public void searchByRecipient(Scanner input) {
        System.out.print("Enter recipient cellphone number: ");
        String recipient = input.nextLine().trim();

        boolean found = false;
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equals(recipient)) {
                printEntry(i);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // Delete by message hash
    public void deleteMessageByHash(Scanner input) {
        System.out.print("Enter the message hash to delete: ");
        String hash = input.nextLine().trim();

        for (int i = 0; i < hashes.size(); i++) {
            if (hashes.get(i).equals(hash)) {
                System.out.println("Message found!");
                System.out.println("Message: " + texts.get(i));

                ids.remove(i);
                senders.remove(i);
                recipients.remove(i);
                texts.remove(i);
                hashes.remove(i);

                System.out.println("Message successfully deleted!");
                return;
            }
        }
        System.out.println("Message hash not found.");
    }

    // Report: hash, recipient, message (plus sender and ID)
    public void displayFullMessageReport() {
        System.out.println("\n========== FULL MESSAGE REPORT ==========");

        for (int i = 0; i < texts.size(); i++) {
            System.out.println();
            printEntry(i);
        }

        System.out.println("\nTotal Stored Messages: " + texts.size());
    }

    // Access stored messages
    public static ArrayList<Messages> getStoredMessages() {
        return storedMessages;
    }
}