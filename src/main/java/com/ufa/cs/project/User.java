// File header comment
package com.ufa.cs.project;

public class User {
    private String username;
    private String password;


    /**
     * Instantiate a user object
     * @param username Username
     * @param password Password
     */
    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /**
     * Copy constructor for the user class
     * @param user The user object to be copied
     */
    public User(User user) {
        setUsername(user.getUsername());
        setPassword(user.getPassword());
    }

    /**
     * Get the username of the user
     * @return The user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the user's hashed password
     * @return The user's hashed password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the username of the user
     * @param username The user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the user's password
     * @param password The password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
