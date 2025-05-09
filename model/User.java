package model;

public class User {
    private String username;
    private String password; // For simplicity, we use plaintext (not secure for production)

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public boolean checkPassword(String input) {
        return password.equals(input);
    }
}