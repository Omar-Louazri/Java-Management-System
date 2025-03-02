package com.packages.LibrarySystem;


import org.json.JSONObject;
import com.packages.ConsoleUtils;

import java.util.Scanner;
public class LibManager {
    public static void ManageActions(JSONObject user) {
        Scanner scanner = new Scanner(System.in); // Create a single Scanner instance
      
        while (true) {
            System.out.println("========== Library System =========");
            System.out.println("Welcome, " + user.getString("name") + " (" + user.getString("grade") + ")!");
            System.out.println("0. Show borrowed books");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. List all books");
            System.out.println("4. Search for a book");
            System.out.println("5. Disconnect");

            if (user.getString("grade").equals("Administrator")) {
                System.out.println("6. Add a book");
                System.out.println("7. Remove a book");
                System.out.println("8. List all users");
                System.out.println("9. Add a user");
                System.out.println("10. Search for a user");
            }

            System.out.print("Choose an option: ");
            int nb_choice = 0;

            // Handle invalid input (non-integer)
            try {
                nb_choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline after nextInt()
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Restart the loop
            }

            switch (nb_choice) {
                case 0:
                    ConsoleUtils.clearScreen();
                    Book.ShowBorrowedBooks(user);
                    break;
                case 1:
                    ConsoleUtils.clearScreen();
                    Book.BorrowFunction(user, scanner);
                    
                    break;
                case 2:
                    ConsoleUtils.clearScreen();
                    Book.ReturnFunction(user, scanner);
                    break;
                case 3:
                    ConsoleUtils.clearScreen();
                    Book.ListAllBooks(user,scanner);
                    break;
                case 4:
                    ConsoleUtils.clearScreen();
                    System.out.println("Waiting for the feature to be implemented.");
                    break;
                case 5:
                    ConsoleUtils.clearScreen();
                    System.out.println("Return to the main menu.");
                    scanner.nextLine(); // Wait for the user to press Enter
                    return; // Exit the method
                case 6:
                    ConsoleUtils.clearScreen();

                    if (user.getString("grade").equals("Administrator")) {
                        System.out.print("Enter book title: ");
                        String newBookTitle = scanner.nextLine();
                        Book.AddBook(newBookTitle);
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 7:
                    ConsoleUtils.clearScreen();

                    if (user.getString("grade").equals("Administrator")) {
                        System.out.print("Enter book title to remove: ");
                        String bookToRemove = scanner.nextLine();
                        Book.RemoveBook(bookToRemove);
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 8:
                    if (user.getString("grade").equals("Administrator")) {
                        ConsoleUtils.clearScreen();
                        User.ListAllUsers(scanner); // Pass the Scanner object
                    } else {
                        System.out.println("Unauthorized action.");
                    }
                    break;
                case 9:
                    if (user.getString("grade").equals("Administrator")) {
                        ConsoleUtils.clearScreen();
                        System.out.print("Enter new user name: ");
                        String newUser = scanner.nextLine();
                        User.AddUser(newUser, scanner);
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 10:
                    if (user.getString("grade").equals("Administrator")) {
                        ConsoleUtils.clearScreen();
                        System.out.print("Enter user name to search: ");
                        String searchUser = scanner.nextLine();
                        User.SearchUser(searchUser);
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                default:
                    ConsoleUtils.clearScreen();
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}