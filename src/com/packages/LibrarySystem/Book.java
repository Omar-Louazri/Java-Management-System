package com.packages.LibrarySystem;

import org.json.JSONArray;
import org.json.JSONObject;

import com.packages.ConsoleUtils;
import com.packages.LoginSystem.LoginSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class Book {
    private static final Path BOOKS_JSON_PATH = Paths.get("src/data/books.json");
    private static final Path STUDENTS_JSON_PATH = Paths.get("src/data/users.json");
    private static final Logger logger = Logger.getLogger(LoginSystem.class.getName());
    // Helper method to read the books.json file
    public static JSONArray readBooks() throws IOException {
        String content = new String(Files.readAllBytes(BOOKS_JSON_PATH));
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getJSONArray("books");
    }

    // Helper method to write the books.json file
    private static void writeBooks(JSONArray books) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("books", books);
        Files.write(BOOKS_JSON_PATH, jsonObject.toString().getBytes(), StandardOpenOption.WRITE, 
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    // Helper method to read the users.json file
    private static JSONArray readUsers() throws IOException {
        String content = new String(Files.readAllBytes(STUDENTS_JSON_PATH));
        JSONObject jsonObject = new JSONObject(content);
        return jsonObject.getJSONArray("students");
    }

    // Helper method to write the users.json file
    private static void writeUsers(JSONArray users) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("students", users);
        Files.write(STUDENTS_JSON_PATH, jsonObject.toString().getBytes(), StandardOpenOption.WRITE, 
        StandardOpenOption.TRUNCATE_EXISTING);
    }

    // List all books
    public static void ListAllBooks(JSONObject user, Scanner scanner) {
        try {
            JSONArray books = readBooks();
            int totalBooks = books.length();
            int startIndex = 0;
            int pageSize = 10; // Number of books to display per page
    
            while (true) {
                System.out.println("========== List of Books (Showing " + (startIndex + 1) + " to " + Math.min(startIndex + pageSize, totalBooks) + " of " + totalBooks + ") ==========");
                for (int i = startIndex; i < Math.min(startIndex + pageSize, totalBooks); i++) {
                    JSONObject book = books.getJSONObject(i);
                    int id = book.getInt("id");
                    String title = book.getString("title");
                    String author = book.getString("author");
                    int nbAvailable = book.getInt("nbAvailable");
                    String displayTitle = title.length() > 27 ? title.substring(0, 27) + "..." : title;
                    // Format the output with fixed-width columns
                    String formattedOutput = String.format(
                        "%-3d. ID: %-10d Title: %-30s Author: %-20s Available: %d",
                        i + 1, id, displayTitle, author, nbAvailable
                    );
                    System.out.println(formattedOutput);
                }
    
                // Display options
                System.out.println("\nOptions:");
                System.out.println("1. Search for a book");
                System.out.println("2. Next 10 books");
                System.out.println("3. Previous 10 books");
                System.out.println("4. Borrow a book");
                if (user.getString("grade").equals("Administrator")) {
                    System.out.println("5. Modify a book");
                    
                }
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
                    ConsoleUtils.clearScreen();
                    System.out.println("Returning to main menu.");
                    return;
                }
    
                switch (choice) {
                    case 1:
                        // Search for a book
                        System.out.print("Enter the Title,Author or ID of the book to search: ");
                        String searchTerm = scanner.nextLine();
                        searchBook(books, searchTerm, scanner, user);
                        break;
                    case 2:
                        ConsoleUtils.clearScreen();
                        // Next 10 books
                        startIndex = Math.min(startIndex + pageSize, totalBooks);
                        if (startIndex >= totalBooks) {
                            System.out.println("No more books to display.");
                            startIndex = Math.max(0, totalBooks - pageSize);
                        }
                        break;
                    case 3:
                        ConsoleUtils.clearScreen(); 
                        // Previous 10 books
                        startIndex = Math.max(0, startIndex - pageSize);
                        if (startIndex < 0) {
                            System.out.println("Already at the beginning of the list.");
                            startIndex = 0;
                        }
                        break;
                    case 4:
                        // Borrow a book
                        BorrowFunction(user, scanner); // No need to pass bookId
                        books = readBooks(); // Reload books data after borrowing
                        break;
                    case 5:
                        if (!user.getString("grade").equals("Administrator")) {
                            ConsoleUtils.clearScreen();
                            System.out.println("Invalid choice. Please try again.");
                            break;
                        }else{
                            // Modify a book
                            System.out.print("Enter the ID of the book to modify: ");
                            int modifyId = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            modifyBook(books, modifyId, scanner);
                            break;
                        }
                    default:
                        ConsoleUtils.clearScreen(); 
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
    }
    // ================== Helper Methods for ListAllBooks ==================
    private static void modifyBook(JSONArray books, int modifyId, Scanner scanner) {
        boolean found = false;
        for (int i = 0; i < books.length(); i++) {
            JSONObject book = books.getJSONObject(i);
            if (book.getInt("id") == modifyId) {
                found = true;
    
                System.out.println("Current book details:");
                System.out.println(String.format(
                    "ID: %-10d Title: %-30s Author: %-20s Available: %d",
                    book.getInt("id"), book.getString("title"), book.getString("author"), book.getInt("nbAvailable")
                ));
    
                System.out.print("Enter new title (or press Enter to keep current): ");
                String newTitle = scanner.nextLine().trim();
                if (!newTitle.isEmpty()) {
                    book.put("title", newTitle);
                }
    
                System.out.print("Enter new author (or press Enter to keep current): ");
                String newAuthor = scanner.nextLine().trim();
                if (!newAuthor.isEmpty()) {
                    book.put("author", newAuthor);
                }
    
                System.out.print("Enter new number of available copies (or press Enter to keep current): ");
                String newNbAvailable = scanner.nextLine().trim();
                if (!newNbAvailable.isEmpty()) {
                    try {
                        int nbAvailable = Integer.parseInt(newNbAvailable);
                        if (nbAvailable >= 0) {
                            book.put("nbAvailable", nbAvailable);
                        } else {
                            System.out.println("Invalid number of copies. Must be a non-negative integer.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Number of copies must be an integer.");
                    }
                }
    
                // Save changes to the file
                try {
                    ConsoleUtils.clearScreen();
                    writeBooks(books);
                    System.out.println("Book updated successfully.");
                } catch (IOException e) {
                    System.err.println("Error updating book: " + e.getMessage());
                }
                break;
            }
        }
    
        if (!found) {
            System.out.println("No book found with ID: " + modifyId);
        }
    }
    //======================================================================
    private static void searchBook(JSONArray books, String searchTerm, Scanner scanner, JSONObject user) {
        int startIndex = 0;
        int pageSize = 10; // Number of books to display per page
        boolean exitSearch = false;
    
        while (!exitSearch) {
            ConsoleUtils.clearScreen(); 
            System.out.println("========== List of Books (Filtered) ==========");
            int endIndex = Math.min(startIndex + pageSize, books.length());
            boolean found = false;
    
            for (int i = startIndex; i < endIndex; i++) {
                JSONObject book = books.getJSONObject(i);
                int id = book.getInt("id");
                String title = book.getString("title");
                String author = book.getString("author");
                // Check if all characters in the search term appear in the title or ID
                if (ConsoleUtils.containsCharactersInOrder(title.toLowerCase(), searchTerm.toLowerCase()) ||
                    String.valueOf(id).contains(searchTerm) ||
                    ConsoleUtils.containsCharactersInOrder(author.toLowerCase(), searchTerm.toLowerCase())) {
                        String displayTitle = title.length() > 28 ? title.substring(0, 25) + "..." : title;

                    System.out.println(String.format(
                        "%-3d. ID: %-10d Title: %-30s Author: %-20s Available: %d %d",
                        i + 1, id, displayTitle, book.getString("author"), book.getInt("nbAvailable")
                    ));
                    found = true;
                }
            }
    
            if (!found) {
                System.out.println("No books found matching: " + searchTerm);
                return; // Exit the search if no matches are found
            }
    
            // Display pagination options
            System.out.println("\nOptions:");
            System.out.println("1. Next 10 books");
            System.out.println("2. Previous 10 books");
            System.out.println("3. Borrow a book");
            if (user.getString("grade").equals("Administrator")) {
                System.out.println("4. Modify a book");
            }
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
                ConsoleUtils.clearScreen();
                System.out.println("Returning to main menu.");
                return;
            }
    
            switch (choice) {
                case 1:
                    // Next 10 books
                    startIndex = endIndex;
                    if (startIndex >= books.length()) {
                        System.out.println("No more books to display.");
                        startIndex = Math.max(0, books.length() - pageSize);
                    }
                    break;
                case 2:
                    // Previous 10 books
                    startIndex = Math.max(0, startIndex - pageSize);
                    if (startIndex < 0) {
                        System.out.println("Already at the beginning of the list.");
                        startIndex = 0;
                    }
                    break;
                case 3:
                    // Borrow a book
                    BorrowFunction(user, scanner);
                    break;
                case 4:
                    if (!user.getString("grade").equals("Administrator")) {
                        ConsoleUtils.clearScreen();
                        System.out.println("Invalid choice. Please try again.");
                        break;
                    }else{
                        // Modify a book
                        System.out.print("Enter the ID of the book to modify: ");
                        int modifyId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        modifyBook(books, modifyId, scanner);
                        break;
                    }
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    //======================================================================        
    // Add a new book
    public static void AddBook(String newBookTitle) {
        try (Scanner scanner = new Scanner(System.in)) {
            JSONArray books = readBooks();

            System.out.print("Enter book author: ");
            String author = scanner.nextLine();

            System.out.print("Enter themes (comma-separated): ");
            String[] themes = scanner.nextLine().split(",");

            System.out.print("Enter number of available copies: ");
            int nbAvailable = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Create a new book object
            JSONObject newBook = new JSONObject();
            newBook.put("id", books.length() + 100); // Generate a unique ID
            newBook.put("title", newBookTitle);
            newBook.put("author", author);
            newBook.put("themes", new JSONArray(themes));
            newBook.put("nbAvailable", nbAvailable);

            // Add the new book to the list
            books.put(newBook);
            writeBooks(books);

            System.out.println("Book added successfully: " + newBook);
        } catch (IOException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    // Remove a book
    public static void RemoveBook(String bookToRemove) {
        try {
            JSONArray books = readBooks();
            boolean found = false;

            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                if (book.getString("title").equalsIgnoreCase(bookToRemove)) {
                    books.remove(i); // Remove the book
                    writeBooks(books);
                    System.out.println("Book removed successfully: " + bookToRemove);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No book found with title: " + bookToRemove);
            }
        } catch (IOException e) {
            System.err.println("Error removing book: " + e.getMessage());
        }
    }

    // Borrow a book
    public static void BorrowFunction(JSONObject user, Scanner scanner) {
        try {
            JSONArray books = readBooks(); // Load books data
            JSONArray users = readUsers(); // Load users data
    
            // Prompt the user to enter the book ID to borrow
            System.out.print("Enter the ID of the book you want to borrow (or -1 to cancel): ");
            int bookId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            // Check if the user wants to cancel
            if (bookId == -1) {
                ConsoleUtils.clearScreen();
                System.out.println("Borrow operation canceled. Returning to main menu.");
                return; // Exit the method and return to the main menu
            }
    
            // Check if the book exists
            JSONObject selectedBook = null;
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                if (book.getInt("id") == bookId) {
                    selectedBook = book;
                    break;
                }
            }
    
            if (selectedBook == null) {
                System.out.println("Invalid book ID. Please try again.");
                return;
            }
    
            // Check if the book is available
            int nbAvailable = selectedBook.getInt("nbAvailable");
            if (nbAvailable <= 0) {
                System.out.println("No copies available for this book.");
                return;
            }
    
            // Check if the user has already borrowed this book
            JSONArray booksBorrowed = user.getJSONArray("booksBorrowed");
            for (int i = 0; i < booksBorrowed.length(); i++) {
                JSONObject borrowRecord = booksBorrowed.getJSONObject(i);
                if (borrowRecord.getInt("bookId") == bookId) {
                    System.out.println("You have already borrowed this book.");
                    return;
                }
            }
    
            // Update book availability
            selectedBook.put("nbAvailable", nbAvailable - 1);
            writeBooks(books); // Save updated books data
    
            // Add the book to the user's borrowed books
            JSONObject borrowRecord = new JSONObject();
            borrowRecord.put("bookId", bookId);
            borrowRecord.put("borrowDate", LocalDate.now().toString());
            borrowRecord.put("dueDate", LocalDate.now().plusWeeks(2).toString()); // Due in 2 weeks
            booksBorrowed.put(borrowRecord);
    
            // Update the user in the users.json file
            for (int i = 0; i < users.length(); i++) {
                JSONObject u = users.getJSONObject(i);
                if (u.getInt("id") == user.getInt("id")) {
                    users.put(i, user);
                    break;
                }
            }
            writeUsers(users); // Save updated users data
    
            System.out.println("Book borrowed successfully: " + selectedBook.getString("title"));
    
            // Refresh the books data after borrowing
            books = readBooks(); // Reload books data to reflect changes
        } catch (IOException e) {
            System.err.println("Error borrowing book: " + e.getMessage());
        }
    }
    // Return a book
    public static void ReturnFunction(JSONObject user, Scanner scanner) {
        try {
            JSONArray books = readBooks();
            JSONArray users = readUsers();
    
            // Get the list of books borrowed by the user
            JSONArray booksBorrowed = user.getJSONArray("booksBorrowed");
    
            if (booksBorrowed.isEmpty()) {
                System.out.println("You have not borrowed any books.");
                return;
            }
    
            // Display the list of borrowed books
            System.out.println("========== Books Borrowed by You ==========");
            for (int i = 0; i < booksBorrowed.length(); i++) {
                JSONObject borrowRecord = booksBorrowed.getJSONObject(i);
                int bookId = borrowRecord.getInt("bookId");
    
                // Find the book details
                for (int j = 0; j < books.length(); j++) {
                    JSONObject book = books.getJSONObject(j);
                    if (book.getInt("id") == bookId) {
                        System.out.println("ID: " + book.getInt("id") + 
                                           ", Title: " + book.getString("title") +
                                           ", Borrowed on: " + borrowRecord.getString("borrowDate") +
                                           ", Due on: " + borrowRecord.getString("dueDate"));
                        break;
                    }
                }
            }
    
            // Prompt the user to enter the ID of the book to return
            System.out.print("Enter the ID of the book you want to return: (-1 to cancel) ");
            int bookId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (bookId == -1) {
                ConsoleUtils.clearScreen();
                System.out.println("Return operation canceled. Returning to main menu.");
                return; // Exit the method and return to the main menu
            }
            // Check if the user has borrowed the book with the given ID
            boolean bookFound = false;
            for (int i = 0; i < booksBorrowed.length(); i++) {
                JSONObject borrowRecord = booksBorrowed.getJSONObject(i);
                if (borrowRecord.getInt("bookId") == bookId) {
                    // Update book availability
                    for (int j = 0; j < books.length(); j++) {
                        JSONObject book = books.getJSONObject(j);
                        if (book.getInt("id") == bookId) {
                            int nbAvailable = book.getInt("nbAvailable");
                            book.put("nbAvailable", nbAvailable + 1);
                            writeBooks(books);
                            break;
                        }
                    }
    
                    // Remove the book from the user's borrowed books
                    booksBorrowed.remove(i);
    
                    // Update the user in the users.json file
                    for (int j = 0; j < users.length(); j++) {
                        JSONObject u = users.getJSONObject(j);
                        if (u.getInt("id") == user.getInt("id")) {
                            users.put(j, user);
                            break;
                        }
                    }
                    writeUsers(users);
    
                    System.out.println("Book returned successfully: ID " + bookId);
                    bookFound = true;
                    break;
                }
            }
    
            if (!bookFound) {
                System.out.println("You have not borrowed a book with ID: " + bookId);
            }
        } catch (IOException e) {
            System.err.println("Error returning book: " + e.getMessage());
        }
    }
   

    public static void ShowBorrowedBooks(JSONObject user) {
        JSONArray booksBorrowed = user.optJSONArray("booksBorrowed");
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


}
