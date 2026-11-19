/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package assignmentpart1;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class StoredMessagesTest {

    private StoredMessages storedMessages;

    private final String[] messageID = {
        "0000000001",
        "0000000002",
        "0000000003",
        "0838884567",
        "0000000005"
    };

    private final String[] messageSender = {
        "Developer", "Developer", "Developer",
        "Developer", "Developer"
    };

    private final String[] messageRecipient = {
        "+27834557896",
        "+27838884567",
        "+27834484567",
        "0838884567",
        "+27838884567"
    };

    private final String[] messageText = {
        "Did you get the cake?",
        "Where are you? You are late! I have asked you to be on time.",
        "Yohhooo, I am at your gate.",
        "It is dinner time !",
        "Ok, I am leaving without you."
    };

    private final String[] messageHash = {
        "01:1:DIDCAKE",
        "02:2:WHEREONTIME",
        "03:3:YOHATGATE",
        "08:4:ITTIME",
        "05:5:OKWITHOUTYOU"
    };

    // SET UP TEST DATA
    @Before
    public void setUp() {

        storedMessages = new StoredMessages(
                messageID.clone(),
                messageSender.clone(),
                messageRecipient.clone(),
                messageText.clone(),
                messageHash.clone(),
                5
        );
    }

    // CAPTURE OUTPUT
    private String captureOutput(Runnable action) {

        PrintStream originalOut = System.out;

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        PrintStream testOut = new PrintStream(output);

        try {
            System.setOut(testOut);
            action.run();
        } finally {
            System.setOut(originalOut);
            testOut.close();
        }

        return output.toString();
    }

    // TEST 1: MESSAGES ARRAY CORRECTLY POPULATED
    @Test
    public void testMessagesArrayCorrectlyPopulated() {

        String output = captureOutput(() -> {
            storedMessages.displayStoredSendersAndRecipients();
        });

        assertEquals(true, output.contains("Did you get the cake?"));

        assertEquals(true, output.contains(
                "Where are you? You are late! I have asked you to be on time."
        ));

        assertEquals(true, output.contains(
                "Yohhooo, I am at your gate."
        ));

        assertEquals(true, output.contains("It is dinner time !"));

        assertEquals(true, output.contains(
                "Ok, I am leaving without you."
        ));

        assertEquals(true, output.contains("Developer"));

        assertEquals(true, output.contains("+27834557896"));
    }

    // TEST 2: DISPLAY THE LONGEST MESSAGE
    @Test
    public void testDisplayLongestMessage() {

        String output = captureOutput(() -> {
            storedMessages.getLongestStoredMessage();
        });

        assertEquals(true, output.contains("Longest Stored Message"));

        assertEquals(true, output.contains(
                "Where are you? You are late! I have asked you to be on time."
        ));
    }

    // TEST 3: SEARCH MESSAGE USING MESSAGE ID
    @Test
    public void testSearchByMessageID() {

        String output = captureOutput(() -> {

            try (Scanner input = new Scanner("0838884567")) {
                storedMessages.searchByMessageID(input);
            }
        });

        assertEquals(true, output.contains("Message found!"));

        assertEquals(true, output.contains("0838884567"));

        assertEquals(true, output.contains("It is dinner time !"));
    }

    // TEST 4: SEARCH ALL MESSAGES USING RECIPIENT
    @Test
    public void testSearchAllMessagesByRecipient() {

        String output = captureOutput(() -> {

            try (Scanner input = new Scanner("+27838884567")) {
                storedMessages.searchByRecipient(input);
            }
        });

        assertEquals(true, output.contains(
                "Where are you? You are late! I have asked you to be on time."
        ));

        assertEquals(true, output.contains(
                "Ok, I am leaving without you."
        ));

        assertEquals(true, output.contains("0000000002"));

        assertEquals(true, output.contains("0000000005"));
    }

    // TEST 5: DELETE MESSAGE USING MESSAGE HASH
    @Test
    public void testDeleteMessageUsingMessageHash() {

        String output = captureOutput(() -> {

            try (Scanner input = new Scanner("02:2:WHEREONTIME")) {
                storedMessages.deleteMessageByHash(input);
            }
        });

        assertEquals(true, output.contains("Message found!"));

        assertEquals(true, output.contains(
                "Where are you? You are late! I have asked you to be on time."
        ));

        assertEquals(true, output.contains(
                "Message successfully deleted!"
        ));

        // Check that the deleted message no longer exists
        String report = captureOutput(() -> {
            storedMessages.displayFullMessageReport();
        });

        assertEquals(false, report.contains(
                "Where are you? You are late! I have asked you to be on time."
        ));

        assertEquals(true, report.contains("Total Stored Messages: 4"));
    }
   
    //Test 6: DISPLAY A REPORT
    @Test
    public void testDisplayReport() {

        String output = captureOutput(() -> {
            storedMessages.displayFullMessageReport();
        });

        // Check that the report includes the message hashes
        assertEquals(true, output.contains("01:1:DIDCAKE"));
        assertEquals(true, output.contains("02:2:WHEREONTIME"));
        assertEquals(true, output.contains("03:3:YOHATGATE"));

        // Check that the report includes the recipients
        assertEquals(true, output.contains("+27834557896"));
        assertEquals(true, output.contains("+27838884567"));
        assertEquals(true, output.contains("+27834484567"));

        // Check that the report includes the messages
        assertEquals(true, output.contains("Did you get the cake?"));
        assertEquals(true, output.contains("Where are you? You are late! I have asked you to be on time."));
        assertEquals(true, output.contains("Yohhooo, I am at your gate."));
        assertEquals(true, output.contains("It is dinner time !"));
        assertEquals(true, output.contains("Ok, I am leaving without you."));
    }
}