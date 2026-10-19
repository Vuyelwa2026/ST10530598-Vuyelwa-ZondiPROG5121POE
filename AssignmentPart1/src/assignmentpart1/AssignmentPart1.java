/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package assignmentpart1;

import java.util.Scanner;

public class AssignmentPart1 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // ==========================
        // Declare variables
        // ==========================

        String firstName;
        String surname;
        String username;
        String cellphoneNumber;
        String password;

        // ==========================
        // First Name
        // ==========================

        System.out.print("Enter your first name: ");
        firstName = input.nextLine();

        // ==========================
        // Surname
        // ==========================

        System.out.print("Enter your surname: ");
        surname = input.nextLine();

        // ==========================
        // Username
        // ==========================

        while (true) {

            System.out.print("Enter your username: ");
            username = input.nextLine();

            Login tempUser = new Login(firstName, surname, username, "", "");

            if (tempUser.checkUsername(username)) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted.");
                System.out.println("Username must contain an underscore (_) and must be no more than 5 characters long.");
            }
        }

        // ==========================
        // Cellphone Number
        // ==========================

        while (true) {

            System.out.print("Enter your cellphone number: ");
            cellphoneNumber = input.nextLine();

            Login tempUser = new Login(firstName, surname, username, "", cellphoneNumber);

            if (tempUser.checkCellphoneNumber(cellphoneNumber)) {
                System.out.println("Cellphone number successfully captured.");
                break;
            } else {
                System.out.println("Cellphone number is not correctly formatted.");
                System.out.println("Please enter a number in the format +27821234567");
            }
        }

        // ==========================
        // Password
        // ==========================

        while (true) {

            System.out.print("Enter your password: ");
            password = input.nextLine();

            Login tempUser = new Login(firstName, surname, username, password, cellphoneNumber);

            if (tempUser.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password is not correctly formatted.");
                System.out.println("Password must:");
                System.out.println("- Be at least 8 characters long");
                System.out.println("- Contain at least one capital letter");
                System.out.println("- Contain at least one number");
                System.out.println("- Contain at least one special character");
            }
        }

        // ==========================
        // Create Final Login Object
        // ==========================

        Login user = new Login(firstName, surname, username, password, cellphoneNumber);

        // ==========================
        // Registration Successful
        // ==========================

        System.out.println();
        System.out.println("==============================");
        System.out.println("REGISTRATION SUCCESSFUL!");
        System.out.println("==============================");
        System.out.println("Welcome " + user.getFirstName() + " " + user.getSurname());

        // ==========================
        // Login Section
        // ==========================

        System.out.println();
        System.out.println("==============================");
        System.out.println("LOGIN");
        System.out.println("==============================");

        int attempts = 0;
        boolean loggedIn = false;

        while (attempts < 3 && !loggedIn) {

            System.out.print("Enter your username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = input.nextLine();

            if (user.loginUser(loginUsername, loginPassword)) {
                loggedIn = true;

                System.out.println();
                System.out.println("Login successful!");
                System.out.println("Welcome " + user.getFirstName() + " " + user.getSurname() + ", it is great to see you again.");

            } else {
                attempts++;
                System.out.println();
                System.out.println("Username or password is incorrect.");

                if (attempts < 3) {
                    System.out.println("You have " + (3 - attempts) + " attempt(s) remaining.");
                } else {
                    System.out.println("You have reached the maximum number of login attempts.");
                    System.out.println("Exiting program.");
                    input.close();
                    return;
                }
            }
        }

        // ==========================
        // Transition to QuickChat Menu
        // ==========================
        if (loggedIn) {
            Messages.startQuickChatApp(input, user);
        } else {
            input.close();
        }
    }
}