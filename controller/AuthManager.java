package controller;

import model.User;
import java.io.*;
import java.util.*;

public class AuthManager {
    private static final String USER_FILE = "users.txt";
    private Map<String, User> userMap = new HashMap<>();

    public AuthManager() {
        loadUsers();
    }

    private void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userMap.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (User user : userMap.values()) {
                writer.printf("%s,%s%n", user.getUsername(), user.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean register(String username, String password) {
        if (userMap.containsKey(username)) return false;
        userMap.put(username, new User(username, password));
        saveUsers();
        return true;
    }

    public User login(String username, String password) {
        User user = userMap.get(username);
        if (user != null && user.checkPassword(password)) {
            return user;
        }
        return null;
    }
}