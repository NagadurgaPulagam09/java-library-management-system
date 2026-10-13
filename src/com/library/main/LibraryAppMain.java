package com.library.main;

import com.library.exception.BookNotAvailableException;
import com.library.exception.BookNotFoundException;
import com.library.model.Book;
import com.library.service.LibraryService;
import com.library.storage.FileBookRepository;
import java.util.List;
import java.util.Scanner;

public class LibraryAppMain {
    private static final Scanner sc = new Scanner(System.in);
    private static final LibraryService library =
            new LibraryService(new FileBookRepository("data/books.csv"));

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = readInt("Choose an option (1-8): ");
            try {
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> viewAll();
                    case 3 -> searchBooks();
                    case 4 -> updateBook();
                    case 5 -> deleteBook();
                    case 6 -> issueBook();
                    case 7 -> returnBook();
                    case 8 -> System.out.println("Goodbye! Your data is saved.");
                    default -> System.out.println("Invalid option.");
                }
            } catch (BookNotFoundException | BookNotAvailableException
                     | IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 8);
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Library Management System ===");
        System.out.println("1. Add Book");
        System.out.println("2. View All Books");
        System.out.println("3. Search Books");
        System.out.println("4. Update Book");
        System.out.println("5. Delete Book");
        System.out.println("6. Issue Book");
        System.out.println("7. Return Book");
        System.out.println("8. Exit");
    }

    private static void addBook() {
        String title = readText("Title: ");
        String author = readText("Author: ");
        Book book = library.addBook(title, author);
        System.out.println("Book added with ID " + book.getId());
    }

    private static void viewAll() {
        printBooks(library.getAllBooks());
    }

    private static void searchBooks() {
        String keyword = readText("Search by title or author: ");
        printBooks(library.search(keyword));
    }

    private static void updateBook() throws BookNotFoundException {
        int id = readInt("Book ID to update: ");
        Book current = library.getBook(id);
        System.out.println("Current: " + current);
        String title = readText("New title (press Enter to keep): ");
        String author = readText("New author (press Enter to keep): ");
        library.updateBook(id, title, author);
        System.out.println("Book updated.");
    }

    private static void deleteBook() throws BookNotFoundException {
        int id = readInt("Book ID to delete: ");
        library.deleteBook(id);
        System.out.println("Book deleted.");
    }

    private static void issueBook()
            throws BookNotFoundException, BookNotAvailableException {
        int id = readInt("Book ID to issue: ");
        String member = readText("Member name: ");
        library.issueBook(id, member);
        System.out.println("Book issued to " + member + ".");
    }

    private static void returnBook() throws BookNotFoundException {
        int id = readInt("Book ID to return: ");
        library.returnBook(id);
        System.out.println("Book returned.");
    }

    private static void printBooks(List<Book> list) {
        if (list.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        System.out.printf("%-4s %-30s %-20s %s%n", "ID", "Title", "Author", "Status");
        System.out.println("-".repeat(75));
        list.forEach(System.out::println);
    }

    // ---------- input helpers (no crashes on bad input) ----------

    private static String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}
