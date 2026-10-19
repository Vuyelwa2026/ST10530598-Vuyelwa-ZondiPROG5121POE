/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignmentpart1;

public class Login {

    private String firstName;
    private String surname;
    private String username;
    private String password;
    private String cellphoneNumber;
    private boolean loginSuccessful;

    public Login(String firstName, String surname, String username,
                 String password, String cellphoneNumber) {
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.cellphoneNumber = cellphoneNumber;
        this.loginSuccessful = false;
    }

    public boolean checkUsername(String username) {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapitalLetter = true;
            }
            if (Character.isDigit(character)) {
                hasNumber = true;
            }
            if (!Character.isLetterOrDigit(character)) {
                hasSpecialChar = true;
            }
        }

        return hasCapitalLetter && hasNumber && hasSpecialChar;
    }

    public boolean checkCellphoneNumber(String cellphoneNumber) {
        if (cellphoneNumber == null) {
            return false;
        }
        return cellphoneNumber.matches("\\+27[0-9]{9}");
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            loginSuccessful = false;
            return false;
        }

        if (enteredUsername.equals(this.username) && enteredPassword.equals(this.password)) {
            loginSuccessful = true;
            return true;
        } else {
            loginSuccessful = false;
            return false;
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}