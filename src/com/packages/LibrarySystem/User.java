package com.packages.LibrarySystem;

import org.json.JSONArray;
import org.json.JSONObject;

import com.packages.ConsoleUtils;

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
   public static void ListAllUsers(Scanner scanner) {
    try {
        JSONArray users = readUsers();
        int totalUsers = users.length();
        int startIndex = 0;
        int endIndex = Math.min(10, totalUsers); // Display first 10 users

        while (true) {
            ConsoleUtils.clearScreen();
            System.out.println("========== List of Users (Showing " + (startIndex + 1) + " to " + endIndex + " of " + totalUsers + ") ==========");
            for (int i = startIndex; i < endIndex; i++) {
                JSONObject user = users.getJSONObject(i);
                int id = user.getInt("id");
                String name = user.getString("name");
                String grade = user.getString("grade");

                // Format the output with fixed-width columns
                String formattedOutput = String.format(
                    "%-3d. ID: %-10d Name: %-20s Grade: %s",
                    i + 1, id, name, grade
                );
                System.out.println(formattedOutput);
            }

            // Display options
            System.out.println("\nOptions:");
            System.out.println("1. Search for a user");
            System.out.println("2. Modify a user");
            System.out.println("3. Next 50 users");
            System.out.println("4. Previous 50 users");
            System.out.print("Choose an option (or -1 to cancel): ");
            int choice = 0;

            // Handle invalid input (non-integer)
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline after nextInt()
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Restart the loop
            }

            // Handle cancel option
            if (choice == -1) {
                System.out.println("Returning to main menu.");
                return;
            }

            switch (choice) {
                case 1:
                    // Search for a user
                    System.out.print("Enter the name or ID of the user to search: ");
                    String searchTerm = scanner.nextLine();
                    searchUser(users, searchTerm, scanner);
                    break;
                case 2:
                    // Modify a user
                    System.out.print("Enter the ID of the user to modify: ");
                    int modifyId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    modifyUser(users, modifyId, scanner);
                    break;
                case 3:
                    // Next 50 users
                    startIndex = endIndex;
                    endIndex = Math.min(endIndex + 50, totalUsers);
                    if (startIndex >= totalUsers) {
                        System.out.println("No more users to display.");
                        startIndex = Math.max(0, totalUsers - 50);
                        endIndex = totalUsers;
                    }
                    break;
                case 4:
                    // Previous 50 users
                    endIndex = startIndex;
                    startIndex = Math.max(0, endIndex - 50);
                    if (startIndex < 0) {
                        System.out.println("Already at the beginning of the list.");
                        startIndex = 0;
                        endIndex = Math.min(50, totalUsers);
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading users file: " + e.getMessage());
    }
    }
    // Modify a user
    private static void modifyUser(JSONArray users, int modifyId, Scanner scanner) {
        boolean found = false;
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getInt("id") == modifyId) {
                found = true;
    
                System.out.println("Current user details:");
                System.out.println(String.format(
                    "ID: %-10d Name: %-20s Grade: %s",
                    user.getInt("id"), user.getString("name"), user.getString("grade")
                ));
    
                System.out.print("Enter new name (or press Enter to keep current): ");
                String newName = scanner.nextLine().trim();
                if (!newName.isEmpty()) {
                    user.put("name", newName);
                }
    
                System.out.print("Enter new grade (e.g., 6 for 6th grade, 'Administrator', or press Enter to keep current): ");
                String newGrade = scanner.nextLine().trim();
                if (!newGrade.isEmpty()) {
                    if (newGrade.equalsIgnoreCase("Administrator")) {
                        user.put("grade", "Administrator");
                    } else {
                        try {
                            int gradeNumber = Integer.parseInt(newGrade);
                            if (gradeNumber >= 1 && gradeNumber <= 12) {
                                user.put("grade", gradeNumber + "th");
                            } else {
                                System.out.println("Invalid grade. Grade must be between 1 and 12 or 'Administrator'.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid grade. Grade must be a number between 1 and 12 or 'Administrator'.");
                        }
                    }
                }
    
                // Save changes to the file
                try {
                    writeUsers(users);
                    System.out.println("User updated successfully.");
                } catch (IOException e) {
                    System.err.println("Error updating user: " + e.getMessage());
                }
                break;
            }
        }
    
        if (!found) {
            System.out.println("No user found with ID: " + modifyId);
        }
    }
    // Search a user
    private static void searchUser(JSONArray users, String searchTerm, Scanner scanner) {
        int startIndex = 0;
        int pageSize = 10; // Number of users to display per page
        boolean exitSearch = false;
    
        while (!exitSearch) {
            ConsoleUtils.clearScreen();
            System.out.println("========== List of Users (Filtered) ==========");
            int endIndex = Math.min(startIndex + pageSize, users.length());
            boolean found = false;
    
            for (int i = startIndex; i < endIndex; i++) {
                JSONObject user = users.getJSONObject(i);
                int id = user.getInt("id");
                String name = user.getString("name");
    
                // Check if all characters in the search term appear in the name in order
                if (containsCharactersInOrder(name.toLowerCase(), searchTerm.toLowerCase())) {
                    System.out.println(String.format(
                        "%-3d. ID: %-10d Name: %-20s Grade: %s",
                        i + 1, id, name, user.getString("grade")
                    ));
                    found = true;
                }
            }
    
            if (!found) {
                System.out.println("No users found matching: " + searchTerm);
                return; // Exit the search if no matches are found
            }
    
            // Display pagination options
            System.out.println("\nOptions:");
            System.out.println("1. Next 10 users");
            System.out.println("2. Previous 10 users");
            System.out.println("3. Return to main menu");
            System.out.print("Choose an option (or -1 to cancel): ");
            int choice = 0;
    
            // Handle invalid input (non-integer)
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline after nextInt()
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Restart the loop
            }
    
            // Handle cancel option
            if (choice == -1) {
                System.out.println("Returning to main menu.");
                return;
            }
    
            switch (choice) {
                case 1:
                    // Next 10 users
                    startIndex = endIndex;
                    if (startIndex >= users.length()) {
                        System.out.println("No more users to display.");
                        startIndex = Math.max(0, users.length() - pageSize);
                    }
                    break;
                case 2:
                    // Previous 10 users
                    startIndex = Math.max(0, startIndex - pageSize);
                    if (startIndex < 0) {
                        System.out.println("Already at the beginning of the list.");
                        startIndex = 0;
                    }
                    break;
                case 3:
                    // Return to main menu
                    System.out.println("Returning to main menu.");
                    exitSearch = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    // Helper method to check if all characters in the search term appear in the name in order
    private static boolean containsCharactersInOrder(String name, String searchTerm) {
        int index = -1;
        for (char c : searchTerm.toCharArray()) {
            index = name.indexOf(c, index + 1);
            if (index == -1) {
                return false;
            }
        }
        return true;
    }
    // Add a new user
    public static void AddUser(String newUserName, Scanner scanner) {   
        try {
            JSONArray users = readUsers();
    
            // Auto-generate user ID
            int id = users.length() + 1;
    
            // Validate grade input
            String grade;
            while (true) {
                System.out.print("Enter user grade (e.g., 6 for 6th grade, 'Administrator', or -1 to cancel): ");
                grade = scanner.nextLine().trim();
    
                // Check if the user wants to cancel
                if (grade.equals("-1")) {
                    System.out.println("User addition canceled. Returning to main menu.");
                    return; // Exit the method and return to the main menu
                }
    
                // Check if the grade is a number (e.g., 6) or "Administrator"
                if (grade.equalsIgnoreCase("Administrator")) {
                    break; // Valid grade
                } else {
                    try {
                        int gradeNumber = Integer.parseInt(grade);
                        if (gradeNumber >= 1 && gradeNumber <= 12) {
                            grade = gradeNumber + "th"; // Convert to "6th", "7th", etc.
                            break; // Valid grade
                        } else {
                            System.out.println("Invalid grade. Please enter a number between 1 and 12, 'Administrator', or -1 to cancel.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid grade. Please enter a number between 1 and 12, 'Administrator', or -1 to cancel.");
                    }
                }
            }
    
            // Create a new user object
            JSONObject newUser = new JSONObject();
            newUser.put("id", id);
            newUser.put("name", newUserName);
            newUser.put("grade", grade);
            newUser.put("booksBorrowed", new JSONArray()); // Initialize with no borrowed books
    
            // Add the new user to the list
            users.put(newUser);
            writeUsers(users);
    
            System.out.println("User added successfully: ID " + id + ", Name: " + newUserName + ", Grade: " + grade);
        } catch (IOException e) {
            System.err.println("An error occurred while adding the user: " + e.getMessage());
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