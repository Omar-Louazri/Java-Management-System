package com.packages.LoginSystem;

import com.packages.ConsoleUtils;
import com.packages.LibrarySystem.LibManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginSystem {

    // 🔹 Define the file path as a constant
    private static final Path STUDENTS_JSON_PATH = Paths.get("src/data/students.json");
    private static final Path BOOKS_JSON_PATH = Paths.get("src/data/books.json");
    // 🔹 Create a logger instance for this class
    private static final Logger logger = Logger.getLogger(LoginSystem.class.getName());

    public static void login(Scanner scanner) {
        System.out.println("\n ====  LOGIN MENU =====");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();  // Read username

        System.out.print("Enter your ID: ");
        int id = scanner.nextInt();  // Read ID
        scanner.nextLine();  // 🔹 Consume the newline left by nextInt()

        // Check user data from JSON
        try {
            // 🔹 Read JSON file as a string using the constant
            String jsonData = new String(Files.readAllBytes(STUDENTS_JSON_PATH));
            JSONObject jsonObject = new JSONObject(jsonData);
            // 🔹 Extract "students" array
            JSONArray studentsArray = jsonObject.getJSONArray("students");
            boolean userFound = false;

            for (int i = 0; i < studentsArray.length(); i++) {
                JSONObject user = studentsArray.getJSONObject(i);
                String firstName = user.getString("name");  // 🔹 Use getString() directly
                int id_usr = user.getInt("id");  // 🔹 Use getInt() directly

                if (firstName.equals(username) && id_usr == id) {
                    System.out.println("\nLogin successful! Welcome, " + username + "!");
                    userFound = true;
                    MenuAfterLoginSuccess(user);
                    break;
                }
            }

            if (!userFound) {
                System.out.println("\nInvalid username or ID. Please try again.");
                login(scanner);
            }

        } catch (IOException e) {
            // 🔹 Log the exception with a user-friendly message
            logger.log(Level.SEVERE, "Error: Could not read the students.json file. Please check the file path.", e);
            System.out.println("Error: Could not read the students.json file. Please check the file path.");
        } catch (org.json.JSONException e) {
            // 🔹 Log the exception with a user-friendly message
            logger.log(Level.SEVERE, "Error: JSON file format is incorrect. Please verify the data.", e);
            System.out.println("Error: JSON file format is incorrect. Please verify the data.");
        }
    }

    public static int register(String name, String grade) {
        try {
            // 🔹 Read the existing students.json file using the constant
            String jsonData = new String(Files.readAllBytes(STUDENTS_JSON_PATH));
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray studentsArray = jsonObject.getJSONArray("students");

            // Generate a new user ID (You can improve this logic based on your needs)
            int newId = studentsArray.length() + 1;  // Simple example: generate ID based on array size
            JSONArray borrowedBOOKS = new JSONArray();

            // Create a new user JSON object
            JSONObject newUser = new JSONObject();
            newUser.put("name", name);
            newUser.put("id", newId);
            newUser.put("grade", grade);
            newUser.put("booksBorrowed",borrowedBOOKS);

            // Add the new user to the students array
            studentsArray.put(newUser);

            // 🔹 Write the updated JSON back to the file using the constant
            Files.write(STUDENTS_JSON_PATH, jsonObject.toString(4).getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            // Return the generated user ID
            return newId;

        } catch (IOException e) {
            // 🔹 Log the exception with a user-friendly message
            logger.log(Level.SEVERE, "Error: Could not read/write the students.json file.", e);
            System.out.println("Error: Could not read/write the students.json file. Please check the file path.");
        }

        return -1;  // Return -1 if there was an error
    }

    private static void MenuAfterLoginSuccess(JSONObject user) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
    
       
    
        while (true) {
            ConsoleUtils.clearScreen();
            System.out.println("\n =========  LOGIN MENU ==========");
            System.out.println("\t 1. Proceed to Library System");
            System.out.println("\t 2. Display Student Information");
            System.out.println("\t 3. Disconnect");
            System.out.print("Choose an option: ");
            if (!scanner.hasNextInt()) {
                System.out.println("ERROR: Invalid input! Please enter a number (1-3).");
                scanner.next();
                continue;
            }
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1:
                    ConsoleUtils.clearScreen();
                    LibManager.ManageActions();
                    break;
    
                case 2:
                    ConsoleUtils.clearScreen();
                    System.out.println("\n ====  Student Information =====");
                    System.out.println("ID: " + user.getInt("id"));
                    System.out.println("Name: " + user.getString("name"));
                    System.out.println("Grade: " + user.getString("grade"));
    
                    
                    // ✅ Use optJSONArray to avoid exceptions
                    JSONArray booksBorrowed = user.optJSONArray("booksBorrowed");

                    if (booksBorrowed == null) {
                        System.out.println("Books Borrowed: None (Field not found or invalid format)");
                    } else {
                        System.out.println("Books Borrowed:");  
                        if (booksBorrowed.length() > 0) {
                            try {
                                // Read the books.json file
                                String booksJsonData = new String(Files.readAllBytes(BOOKS_JSON_PATH));
                                JSONObject booksJsonObject = new JSONObject(booksJsonData);
                                JSONArray booksArray = booksJsonObject.getJSONArray("books");
        
                                for (int i = 0; i < booksBorrowed.length(); i++) {
                                    JSONObject borrowedBook = booksBorrowed.getJSONObject(i);
                                    int bookId = borrowedBook.getInt("bookId");
        
                                    // Find the book details in the books.json file
                                    for (int j = 0; j < booksArray.length(); j++) {
                                        JSONObject book = booksArray.getJSONObject(j);
                                        if (book.getInt("id") == bookId) {
                                            System.out.println(i + 1 + ". " + book.getString("title"));
                                            System.out.println("\t Book ID: " + book.getInt("id"));
                                            System.out.println("\t Author: " + book.getString("author"));
                                            System.out.println("\t Themes: " + book.getJSONArray("themes").join(", "));
                                            System.out.println("\t Borrowed Date: " + borrowedBook.getString("borrowDate"));
                                            System.out.println("\t Due Date: " + borrowedBook.getString("dueDate"));
                                            System.out.println(); // Space for better readability
                                            break;
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, "Error: Could not read the books.json file.", e);
                                System.out.println("Error: Could not read the books.json file. Please check the file path.");
                            }
                        } else {
                            System.out.println(" None");
                        }
                    }
                    break;
    
                case 3:
                    ConsoleUtils.clearScreen();
                    System.out.println("\n ====  ARE YOU SURE YOU WANT TO DISCONNECT ? =====");
                    return; // Exit the menu
    
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }
    
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine(); // Pause before reloading menu
        }
    }
    
}