package assignmentpart1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessagesTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream capturedOut;
    private boolean jsonFileExistedBefore;

    @Before
    public void setUp() {
        capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));
        jsonFileExistedBefore = new File("messages.json").exists();
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        // Only delete messages.json if the tests created it
        File f = new File("messages.json");
        if (!jsonFileExistedBefore && f.exists()) {
            f.delete();
        }
    }

    // ---------- helpers ----------

    private Scanner scannerOf(String... lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append("\n");
        }
        return new Scanner(sb.toString());
    }

    private String output() {
        return capturedOut.toString();
    }

    private String repeat(char c, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    // ---------- isValidRecipient ----------

    @Test
    public void testIsValidRecipient_validNumbers() {
        assertTrue(Messages.isValidRecipient("+27821234567"));      // typical SA number
        assertTrue(Messages.isValidRecipient("+27721234567"));
        assertTrue(Messages.isValidRecipient("+12345678"));         // 8 digits (minimum)
        assertTrue(Messages.isValidRecipient("+123456789012345"));  // 15 digits (maximum)
    }

    @Test
    public void testIsValidRecipient_invalidNumbers() {
        assertFalse(Messages.isValidRecipient(""));                  // empty
        assertFalse(Messages.isValidRecipient("0821234567"));        // no + prefix
        assertFalse(Messages.isValidRecipient("27821234567"));       // missing +
        assertFalse(Messages.isValidRecipient("+0821234567"));       // starts with 0 after +
        assertFalse(Messages.isValidRecipient("+1234567"));          // 7 digits (too short)
        assertFalse(Messages.isValidRecipient("+1234567890123456")); // 16 digits (too long)
        assertFalse(Messages.isValidRecipient("+27 82 123 4567"));   // spaces
        assertFalse(Messages.isValidRecipient("+27abc1234567"));     // letters
    }

    // ---------- generateMessageHash ----------

    @Test
    public void testGenerateMessageHash_twoWords() {
        assertEquals("12:1:HELLOWORLD",
                Messages.generateMessageHash("1234567890", 1, "Hello world"));
    }

    @Test
    public void testGenerateMessageHash_singleWord() {
        assertEquals("98:5:OKOK",
                Messages.generateMessageHash("9876543210", 5, "ok"));
    }

    @Test
    public void testGenerateMessageHash_longMessageUsesFirstAndLastWord() {
        assertEquals("45:3:HITONIGHT",
                Messages.generateMessageHash("4567891230", 3, "Hi there how are you tonight"));
    }

    @Test
    public void testGenerateMessageHash_trimsWhitespace() {
        assertEquals("12:2:HITHERE",
                Messages.generateMessageHash("1234567890", 2, "   Hi   there   "));
    }

    @Test
    public void testGenerateMessageHash_isUpperCase() {
        String hash = Messages.generateMessageHash("1234567890", 1, "mixed Case");
        assertEquals(hash.toUpperCase(), hash);
    }

    // ---------- generateMessageID ----------

    @Test
    public void testGenerateMessageID_isTenDigits() {
        for (int i = 0; i < 100; i++) {
            String id = Messages.generateMessageID();
            assertEquals(10, id.length());
            assertTrue("ID should be numeric: " + id, id.matches("[0-9]{10}"));
        }
    }

    // ---------- escapeJSON ----------

    @Test
    public void testEscapeJSON_plainText() {
        assertEquals("Hello world", Messages.escapeJSON("Hello world"));
    }

    @Test
    public void testEscapeJSON_quotes() {
        assertEquals("He said \\\"hi\\\"", Messages.escapeJSON("He said \"hi\""));
    }

    @Test
    public void testEscapeJSON_backslash() {
        assertEquals("C:\\\\temp", Messages.escapeJSON("C:\\temp"));
    }

    @Test
    public void testEscapeJSON_controlCharacters() {
        assertEquals("line1\\nline2\\ttab\\rreturn",
                Messages.escapeJSON("line1\nline2\ttab\rreturn"));
    }

    @Test
    public void testEscapeJSON_emptyString() {
        assertEquals("", Messages.escapeJSON(""));
    }

    // ---------- readPositiveNumber ----------

    @Test
    public void testReadPositiveNumber_validFirstTry() {
        assertEquals(5, Messages.readPositiveNumber(scannerOf("5")));
    }

    @Test
    public void testReadPositiveNumber_rejectsZeroNegativeAndText() {
        // 0, -3 and "abc" are rejected; 7 is accepted
        assertEquals(7, Messages.readPositiveNumber(scannerOf("0", "-3", "abc", "7")));
    }

    // ---------- sendMessage ----------

    @Test
    public void testSendMessage_sendIncreasesTotal() {
        int before = Messages.returnTotalMessages();
        Messages.sendMessage(scannerOf("+27821234567", "Hello world", "1"));
        assertEquals(before + 1, Messages.returnTotalMessages());
        assertTrue(output().contains("Message successfully sent."));
        assertTrue(output().contains("Status: Sent"));
    }

    @Test
    public void testSendMessage_disregardDoesNotIncreaseTotal() {
        int before = Messages.returnTotalMessages();
        Messages.sendMessage(scannerOf("+27821234567", "Skip this one", "0"));
        assertEquals(before, Messages.returnTotalMessages());
        assertTrue(output().contains("Message disregarded successfully."));
    }

    @Test
    public void testSendMessage_storeWritesToFile() {
        int before = Messages.returnTotalMessages();
        Messages.sendMessage(scannerOf("+27821234567", "Send me later", "2"));
        assertEquals(before, Messages.returnTotalMessages());
        assertTrue(output().contains("Message successfully stored."));
        assertTrue(new File("messages.json").exists());
    }

    @Test
    public void testSendMessage_invalidRecipientIsRejected() {
        int before = Messages.returnTotalMessages();
        Messages.sendMessage(scannerOf("0821234567"));
        assertEquals(before, Messages.returnTotalMessages());
        assertTrue(output().contains("Invalid recipient cellphone number."));
    }

    @Test
    public void testSendMessage_emptyMessageIsRejected() {
        int before = Messages.returnTotalMessages();
        Messages.sendMessage(scannerOf("+27821234567", "   "));
        assertEquals(before, Messages.returnTotalMessages());
        assertTrue(output().contains("Please enter a message of less than 250 characters."));
    }

    @Test
    public void testSendMessage_250CharactersIsRejected() {
        int before = Messages.returnTotalMessages();
        Messages.sendMessage(scannerOf("+27821234567", repeat('a', 250)));
        assertEquals(before, Messages.returnTotalMessages());
        assertTrue(output().contains("Please enter a message of less than 250 characters."));
    }

    @Test
    public void testSendMessage_249CharactersIsAccepted() {
        int before = Messages.returnTotalMessages();
        Messages.sendMessage(scannerOf("+27821234567", repeat('a', 249), "1"));
        assertEquals(before + 1, Messages.returnTotalMessages());
    }

    @Test
    public void testSendMessage_invalidActionDoesNotSend() {
        int before = Messages.returnTotalMessages();
        Messages.sendMessage(scannerOf("+27821234567", "Hello there", "9"));
        assertEquals(before, Messages.returnTotalMessages());
        assertTrue(output().contains("Invalid action. Message not sent."));
    }

    // ---------- startQuickChat ----------

    @Test
    public void testStartQuickChat_sendsMultipleMessages() {
        int before = Messages.returnTotalMessages();
        // Enter 2 messages, send both
        Messages.startQuickChat(scannerOf(
                "2",
                "+27821234567", "First message", "1",
                "+27721234567", "Second message", "1"));
        assertEquals(before + 2, Messages.returnTotalMessages());
    }

    // ---------- displayMessage ----------

    @Test
    public void testDisplayMessage_printsAllFields() {
        Messages msg = new Messages("1234567890", 1, "+27821234567",
                "Hello world", "12:1:HELLOWORLD", "Sent");
        Messages.displayMessage(msg);

        String out = output();
        assertTrue(out.contains("Message ID: 1234567890"));
        assertTrue(out.contains("Message Hash: 12:1:HELLOWORLD"));
        assertTrue(out.contains("Recipient: +27821234567"));
        assertTrue(out.contains("Message: Hello world"));
        assertTrue(out.contains("Status: Sent"));
    }

    // ---------- storeMessage ----------

    @Test
    public void testStoreMessage_reportsSuccess() {
        Messages msg = new Messages("1234567890", 1, "+27821234567",
                "He said \"hi\"", "12:1:HEHI\"", "Stored");
        Messages.storeMessage(msg);
        assertTrue(output().contains("Message successfully stored."));
        assertTrue(new File("messages.json").exists());
    }

    // ---------- returnTotalMessages ----------

    @Test
    public void testReturnTotalMessages_neverNegative() {
        assertTrue(Messages.returnTotalMessages() >= 0);
    }

    // Note: startQuickChatApp is not tested here because it needs a Login
    // object, and the Login class wasn't included.
}