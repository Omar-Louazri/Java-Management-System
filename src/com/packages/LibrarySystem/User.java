package com.packages.LibrarySystem;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class User {
    private static final Path USERS_JSON_PATH = Paths.get("src/data/users.json");

    // Helper method to read the users.json file
    private static JSONArray readUsers() throws IOException {
        String content = new String(Files.readAllBytes(USERS_JSON_PATH));
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getJSONArray("students");
    }

    // Helper method to write the users.json file
    private static void writeUsers(JSONArray users) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("students", users);
        Files.write(USERS_JSON_PATH, jsonObject.toString().getBytes());
    }

    // List all users
    public static void ListAllUsers() {
        try {
            JSONArray users = readUsers();
            System.out.println("========== List of All Users ==========");
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                System.out.println(user);
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
    }

    // Add a new user
    public static void AddUser(String newUserName) {
        try (Scanner scanner = new Scanner(System.in)) {
            JSONArray users = readUsers();

            System.out.print("Enter user grade (e.g., 12th, Administrator): ");
            String grade = scanner.nextLine();

            System.out.print("Enter user ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Create a new user object
            JSONObject newUser = new JSONObject();
            newUser.put("id", id);
            newUser.put("name", newUserName);
            newUser.put("grade", grade);
            newUser.put("booksBorrowed", new JSONArray()); // Initialize with no borrowed books

            // Add the new user to the list
            users.put(newUser);
            writeUsers(users);

            System.out.println("User added successfully: " + newUser);
        } catch (IOException e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
    }

    // Search for a user by name
    public static void SearchUser(String searchName) {
        try {
            JSONArray users = readUsers();
            boolean found = false;

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("name").equalsIgnoreCase(searchName)) {
                    System.out.println("User found: " + user);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No user found with name: " + searchName);
            }
        } catch (IOException e) {
            System.err.println("Error searching for user: " + e.getMessage());
        }
    }
}