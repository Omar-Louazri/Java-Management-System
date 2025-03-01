package com.packages.LibrarySystem;

import org.json.JSONObject;
import java.util.Scanner;

public class LibManager {
    public static void ManageActions(JSONObject user) {
        Scanner scanner = new Scanner(System.in); // Create a single Scanner instance

        while (true) {
            System.out.println("========== Library System =========");
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
                case 1:
                    Book.BorrowFunction(user, scanner); // Pass the Scanner object
                    break;
                case 2:
                    Book.ReturnFunction(user, scanner); // Pass the Scanner object
                    break;
                case 3:
                    Book.ListAllBooks();
                    break;
                case 4:
                    System.out.println("========== Advanced Search for a Book =========");
                    System.out.println("1. Search by Title");
                    System.out.println("2. Search by Author");
                    System.out.println("3. Search by Theme");
                    System.out.println("4. Search by Availability");
                    System.out.print("Choose a search option: ");
                    int searchChoice = 0;

                    // Handle invalid input (non-integer)
                    try {
                        searchChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline after nextInt()
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        scanner.nextLine(); // Clear the invalid input
                        break;
                    }

                    switch (searchChoice) {
                        case 1:
                            System.out.print("Enter book title: ");
                            String title = scanner.nextLine();
                            Book.SearchBook(title);
                            break;
                        case 2:
                            System.out.print("Enter author name: ");
                            String author = scanner.nextLine();
                            Book.SearchByAuthor(author);
                            break;
                        case 3:
                            System.out.print("Enter theme: ");
                            String theme = scanner.nextLine();
                            Book.SearchByTheme(theme);
                            break;
                        case 4:
                            System.out.print("Enter minimum number of available copies: ");
                            int nbAvailable = 0;

                            // Handle invalid input (non-integer)
                            try {
                                nbAvailable = scanner.nextInt();
                                scanner.nextLine(); // Consume the newline after nextInt()
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Invalid input. Please enter a number.");
                                scanner.nextLine(); // Clear the invalid input
                                break;
                            }

                            Book.SearchByAvailability(nbAvailable);
                            break;
                        default:
                            System.out.println("Invalid choice. Returning to main menu.");
                            break;
                    }
                    break;
                case 5:
                    System.out.println("Logging out...");
                    scanner.close(); // Close the scanner only when exiting
                    return; // Exit the method
                case 6:
                    if (user.getString("grade").equals("Administrator")) {
                        System.out.print("Enter book title: ");
                        String newBookTitle = scanner.nextLine();
                        Book.AddBook(newBookTitle);
                    } else {
                        System.out.println("Unauthorized action.");
                    }
                    break;
                case 7:
                    if (user.getString("grade").equals("Administrator")) {
                        System.out.print("Enter book title to remove: ");
                        String bookToRemove = scanner.nextLine();
                        Book.RemoveBook(bookToRemove);
                    } else {
                        System.out.println("Unauthorized action.");
                    }
                    break;
                case 8:
                    if (user.getString("grade").equals("Administrator")) {
                        User.ListAllUsers();
                    } else {
                        System.out.println("Unauthorized action.");
                    }
                    break;
                case 9:
                    if (user.getString("grade").equals("Administrator")) {
                        System.out.print("Enter new user name: ");
                        String newUser = scanner.nextLine();
                        User.AddUser(newUser);
                    } else {
                        System.out.println("Unauthorized action.");
                    }
                    break;
                case 10:
                    if (user.getString("grade").equals("Administrator")) {
                        System.out.print("Enter user name to search: ");
                        String searchUser = scanner.nextLine();
                        User.SearchUser(searchUser);
                    } else {
                        System.out.println("Unauthorized action.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}