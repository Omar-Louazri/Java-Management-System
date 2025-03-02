package com.packages.LibrarySystem;

import org.json.JSONArray;
import org.json.JSONObject;

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
    private static JSONArray readBooks() throws IOException {
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
    public static void ListAllBooks() {
        try {
            JSONArray books = readBooks();
            System.out.println("========== List of All Books ==========");
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                System.out.println(book);
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
    }

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
            JSONArray books = readBooks();
            JSONArray users = readUsers();
    
            // Display all books
            System.out.println("========== List of All Books ==========");
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                System.out.println("ID: " + book.getInt("id") + ", Title: " + book.getString("title") + 
                                   ", Available: " + book.getInt("nbAvailable"));
            }
    
            // Allow the user to search for a book by title
            System.out.print("Enter a book title to search (or press Enter to skip) (-1 to cancel): ");
            String searchTitle = scanner.nextLine();
            if (searchTitle.equals("-1")) {
                System.out.println("Return operation canceled. Returning to main menu.");
                return; // Exit the method and return to the main menu
            }
            if (!searchTitle.isEmpty()) {
                boolean found = false;
                for (int i = 0; i < books.length(); i++) {
                    JSONObject book = books.getJSONObject(i);
                    if (book.getString("title").toLowerCase().contains(searchTitle.toLowerCase())) {
                        System.out.println("ID: " + book.getInt("id") + ", Title: " + book.getString("title") + 
                                          ", Available: " + book.getInt("nbAvailable"));
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No books found with title: " + searchTitle);
                }
            }
    
            // Prompt the user to enter the book ID to borrow
            System.out.print("Enter the ID of the book you want to borrow: ");
            int bookId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (bookId == -1) {
                System.out.println("Return operation canceled. Returning to main menu.");
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
            writeBooks(books);
    
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
            writeUsers(users);
    
            System.out.println("Book borrowed successfully: " + selectedBook.getString("title"));
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
   
    // Search for a book by title
    public static void SearchBook(String searchTerm) {
        try {
            JSONArray books = readBooks();
            boolean found = false;
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                String title = book.getString("title");
                if (title.equalsIgnoreCase(searchTerm)) {
                    System.out.println("Book found: " + book);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("No book found with title: " + searchTerm);
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
    }

    // Search for books by author
    public static void SearchByAuthor(String author) {
        try {
            JSONArray books = readBooks();
            boolean found = false;
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                String bookAuthor = book.getString("author");
                if (bookAuthor.equalsIgnoreCase(author)) {
                    System.out.println("Book found: " + book);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No books found by author: " + author);
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
    }

    // Search for books by theme
    public static void SearchByTheme(String theme) {
        try {
            JSONArray books = readBooks();
            boolean found = false;
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                JSONArray themes = book.getJSONArray("themes");
                for (int j = 0; j < themes.length(); j++) {
                    if (themes.getString(j).equalsIgnoreCase(theme)) {
                        System.out.println("Book found: " + book);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                System.out.println("No books found with theme: " + theme);
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
        }
    }

    // Search for books by availability
    public static void SearchByAvailability(int nbAvailable) {
        try {
            JSONArray books = readBooks();
            boolean found = false;
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                int availableCopies = book.getInt("nbAvailable");
                if (availableCopies >= nbAvailable) {
                    System.out.println("Book found: " + book);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No books found with at least " + nbAvailable + " copies available.");
            }
        } catch (IOException e) {
            System.err.println("Error reading books file: " + e.getMessage());
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
