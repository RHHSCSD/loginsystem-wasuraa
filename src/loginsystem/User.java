/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginsystem;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class representing a User
 * @author William
 */
public class User {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String salt;
    
    /**
     * Creates an instance of the User class with an auto-generated salt
     * @param username the User's username
     * @param password the User's password
     * @param firstName the User's first name
     * @param lastName the User's last name
     * @param email the User's email
     */
    public User(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        
        // Generating a salt of random length 4-6
        SecureRandom random = new SecureRandom();
        this.salt = new String(random.generateSeed((int)(Math.random() * 3) + 4), StandardCharsets.UTF_8); 
        
        this.password = password;
    }
    
    /**
     * Creates an instance of the User class with a provided salt
     * @param username the User's username
     * @param password the User's password
     * @param firstName the User's first name
     * @param lastName the User's last name
     * @param email the User's email
     * @param salt the User's salt
     */
    public User(String username, String password, String firstName, String lastName, String email, String salt) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salt = salt; 
        this.password = password;
    }
    
    /**
     * Forms a text representation of the User using a delimeter to separate its attributes
     * @param delimeter the delimeter to be used
     * @return a String containing each field of the User separated by the provided delimeter
     */
    public String formString(String delimeter) {
        return this.getUsername() + delimeter + this.password + delimeter + this.firstName + delimeter + this.lastName + delimeter + this.getEmail() + delimeter + this.salt; 
    }
    
    /**
     * Checks if the provided password matches the password of the User
     * @param password the String to check
     * @return true if it matches the password, false otherwise
     */
    public boolean checkPassword(String password){
        return this.password.equals(this.encrypt(password));
    }
    
    /**
     * Encrypts the User's password 
     */
    public void encryptPass() {
        this.password = this.encrypt(this.password);
    }
    
    private String encrypt(String password){
        try {
            // password to be encrypted (add the salt beforehand)
            password += this.salt;
            // java helper class to perform encryption
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // give the helper function the password
            md.update(password.getBytes());
            // perform the encryption
            byte byteData[] = md.digest();
            // To express the byte data as a hexadecimal number (the normal way)
            String encryptedPassword = "";
            for (int i = 0; i < byteData.length; ++i) {
                encryptedPassword += (Integer.toHexString((byteData[i] & 0xFF) | 0x100).substring(1,3));
            }
            return encryptedPassword;
            
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("NoSuchAlgorithmException");
            return password;
        }
    }
     
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }
}

