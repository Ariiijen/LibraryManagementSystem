package com.library.models;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String role; // admin, librarian, user
    private String fullName;
    private String email;
    private String phoneNumber;

    public User(String username, String password, String role, String fullName, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}