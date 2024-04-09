/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginsystem;
import java.io.*;
import java.util.*;

/**
 * A class for a user registration system
 * @author William
 */
public class RegistrationSystem {
    final String storage = "storage.txt";
    private ArrayList<User> users;
    final String DELIMITER = "~";
    
    /**
     * Creates an instance of a RegistrationSystm
     */
    public RegistrationSystem() {
        this.read(); // Updates the list of users to match the file
    }
    
    /**
     * Checks if a password matches any password in a text file containing weak passwords
     * @param password the password to check
     * @return true if the password is weak, false otherwise
     */
    public static boolean matchingWeakPass(String password) {
        try {
            Scanner sc = new Scanner(new File("dictbadpass.txt"));
            while(sc.hasNextLine()) { 
                if(sc.nextLine().equals(password)) { // Check if the password can be found on a line
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
        return false;
    }
    
    /**
     * Checks if a password fails the requirements for a strong password, meaning it contains less than 8 characters, or it does not contain an upper case, lower case, number or special character
     * @param password the password to check
     * @return true if the password is weak, false otherwise
     */
    public static boolean failedPassRequirements(String password) {
        // Keeps track of upper case, lower, numbers and special characters in that order
        boolean[] charChecks = {true, true, true, true};
        for(int i = 0; i < password.length(); i++) {
            char c = password.charAt(i); // Get each character
            if(Character.isUpperCase(c)) { // Check upper case
                charChecks[0] = false;
            } else if(Character.isLowerCase(c)) { // Check lower case
                charChecks[1] = false;
            } else if(Character.isDigit(c)) { // Check number
                charChecks[2] = false;
            } else { // Must be a special character
                charChecks[3] = false;
            }
        }
        // Check everything at once and return false if any weak condition is met
        return password.length() < 8 || charChecks[0] || charChecks[1] || charChecks[2] || charChecks[3];
    }
    
    /**
     * Attempts to register a User into the system, which will not be possible if the attempted User contains a username or email already in use
     * @param username the User's username
     * @param password the User's password
     * @param firstName the User's first name
     * @param lastName the User's last name
     * @param email the User's email
     * @return true if the User was able to be registered, false otherwise
     */
    public boolean register(String username, String password, String firstName, String lastName, String email) {
        // Ensure there is no matching usernames or emails already registered
        for(User u: users) {
            if(u.getUsername().equals(username) || u.getEmail().equals(email)){
                return false; // This will skip the code adding the user and notify the GUI that the logic was false
            }
        }
        
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(this.storage, true)); // Ensure the PrintWriter will append new lines instead of rewriting the whole file
            User u = new User(username, password, firstName, lastName, email); // Create user with newly generated salt
            u.encryptPass(); // Encrypt password before storing it in text file
            
            // Add the new User to the text file
            pw.println(u.formString(DELIMITER));
            pw.close();
            return true;
        } catch (IOException e) {
            System.out.println("IOException");
            return false;
        }
    }
    
    // Called automatically to update the list of Users to match the storage file
    private void read() {
        String[] userParams; // Stores the fields for the User
        ArrayList<User> temp = new ArrayList();
        try {
            Scanner sc = new Scanner(new File(this.storage));
            while(sc.hasNextLine()) { // Go through storage file
                userParams = sc.nextLine().split(DELIMITER);
                // Create a user with its previously created salt
                User u = new User(userParams[0], userParams[1], userParams[2], userParams[3], userParams[4], userParams[5]);
                temp.add(u); // Add User to list
            }
            sc.close();
            users = temp;
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
    
    /**
     * Attempts to login to an existing User within the RegistrationSystem
     * @param username the User's username
     * @param password the User's password
     * @return true if the login was successful, otherwise false
     */
    public boolean login(String username, String password) {
        // Check for the matching username in storage
        this.read();
        for(User u: users) {
            if(u.getUsername().equals(username) && u.checkPassword(password)){ // Checks for a match
                return true;
            }
        }
        return false;
    }
}
