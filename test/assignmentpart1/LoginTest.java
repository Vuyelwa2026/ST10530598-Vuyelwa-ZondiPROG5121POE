/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package assignmentpart1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Student
 */
public class LoginTest {

    private Login instance;

    private static final String FIRST_NAME = "Kyle";
    private static final String SURNAME = "Smith";
    private static final String VALID_USERNAME = "kyl_1";
    private static final String INVALID_USERNAME = "kyle!!!!!!!";
    private static final String VALID_PASSWORD = "Ch&&sec@ke99!";
    private static final String INVALID_PASSWORD = "password";
    private static final String VALID_CELLPHONE = "+27838968976";
    private static final String INVALID_CELLPHONE = "08966553";

    public LoginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new Login(FIRST_NAME, SURNAME, VALID_USERNAME, VALID_PASSWORD, VALID_CELLPHONE);
    }

    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testCheckUsername() {
        System.out.println("checkUsername");
        boolean expResult = true;
        boolean result = instance.checkUsername(VALID_USERNAME);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckUsernameIncorrect() {
        System.out.println("checkUsername - incorrectly formatted");
        boolean expResult = false;
        boolean result = instance.checkUsername(INVALID_USERNAME);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckUsernameTrue() {
        System.out.println("checkUsername - assertTrue");
        assertTrue(instance.checkUsername(VALID_USERNAME));
    }

    @Test
    public void testCheckUsernameFalse() {
        System.out.println("checkUsername - assertFalse");
        assertFalse(instance.checkUsername(INVALID_USERNAME));
    }

    @Test
    public void testCheckPasswordComplexity() {
        System.out.println("checkPasswordComplexity");
        boolean expResult = true;
        boolean result = instance.checkPasswordComplexity(VALID_PASSWORD);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckPasswordComplexityIncorrect() {
        System.out.println("checkPasswordComplexity - does not meet requirements");
        boolean expResult = false;
        boolean result = instance.checkPasswordComplexity(INVALID_PASSWORD);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckPasswordComplexityTrue() {
        System.out.println("checkPasswordComplexity - assertTrue");
        assertTrue(instance.checkPasswordComplexity(VALID_PASSWORD));
    }

    @Test
    public void testCheckPasswordComplexityFalse() {
        System.out.println("checkPasswordComplexity - assertFalse");
        assertFalse(instance.checkPasswordComplexity(INVALID_PASSWORD));
    }

    @Test
    public void testCheckCellphoneNumber() {
        System.out.println("checkCellphoneNumber");
        boolean expResult = true;
        boolean result = instance.checkCellphoneNumber(VALID_CELLPHONE);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckCellphoneNumberIncorrect() {
        System.out.println("checkCellphoneNumber - incorrectly formatted");
        boolean expResult = false;
        boolean result = instance.checkCellphoneNumber(INVALID_CELLPHONE);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckCellphoneNumberTrue() {
        System.out.println("checkCellphoneNumber - assertTrue");
        assertTrue(instance.checkCellphoneNumber(VALID_CELLPHONE));
    }

    @Test
    public void testCheckCellphoneNumberFalse() {
        System.out.println("checkCellphoneNumber - assertFalse");
        assertFalse(instance.checkCellphoneNumber(INVALID_CELLPHONE));
    }

    @Test
    public void testLoginUser() {
        System.out.println("loginUser - successful");
        boolean expResult = true;
        boolean result = instance.loginUser(VALID_USERNAME, VALID_PASSWORD);
        assertEquals(expResult, result);
    }

    @Test
    public void testLoginUserFailed() {
        System.out.println("loginUser - failed");
        boolean expResult = false;
        boolean result = instance.loginUser(INVALID_USERNAME, INVALID_PASSWORD);
        assertEquals(expResult, result);
    }

    @Test
    public void testLoginUserTrue() {
        System.out.println("loginUser - assertTrue");
        assertTrue(instance.loginUser(VALID_USERNAME, VALID_PASSWORD));
    }

    @Test
    public void testLoginUserFalse() {
        System.out.println("loginUser - assertFalse");
        assertFalse(instance.loginUser(INVALID_USERNAME, INVALID_PASSWORD));
    }

    @Test
    public void testIsLoginSuccessful() {
        System.out.println("isLoginSuccessful - successful");
        instance.loginUser(VALID_USERNAME, VALID_PASSWORD);
        assertTrue(instance.isLoginSuccessful());
    }

    @Test
    public void testIsLoginSuccessfulFailed() {
        System.out.println("isLoginSuccessful - failed");
        instance.loginUser(INVALID_USERNAME, INVALID_PASSWORD);
        assertFalse(instance.isLoginSuccessful());
    }

    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        String expResult = FIRST_NAME;
        String result = instance.getFirstName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSurname() {
        System.out.println("getSurname");
        String expResult = SURNAME;
        String result = instance.getSurname();
        assertEquals(expResult, result);
    }
}