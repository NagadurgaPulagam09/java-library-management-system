package com.library.service;

import com.library.exception.BookNotAvailableException;
import com.library.exception.BookNotFoundException;
import com.library.model.Book;
import com.library.storage.FileBookRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LibraryService {
    private final Map<Integer, Book> books = new LinkedHashMap<>();
    private final FileBookRepository repository;
    private int nextId = 1;

    public LibraryService(FileBookRepository repository) {
        this.repository = repository;
        for (Book book : repository.load()) {
            books.put(book.getId(), book);
            nextId = Math.max(nextId, book.getId() + 1);
        }
    }

    // ---------- Create ----------
    public Book addBook(String title, String author) {
        String t = clean(title);
        String a = clean(author);
        if (t.isEmpty() || a.isEmpty()) {
            throw new IllegalArgumentException("Title and author cannot be empty.");
        }
        Book book = new Book(nextId++, t, a);
        books.put(book.getId(), book);
        save();
        return book;
    }

    // ---------- Read ----------
    public Book getBook(int id) throws BookNotFoundException {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException("No book found with ID " + id);
        }
        return book;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public List<Book> search(String keyword) {
        String key = clean(keyword).toLowerCase();
        List<Book> result = new ArrayList<>();
        for (Book b : books.values()) {
            if (b.getTitle().toLowerCase().contains(key)
                    || b.getAuthor().toLowerCase().contains(key)) {
                result.add(b);
            }
        }
        return result;
    }

    // ---------- Update ----------
    // Empty values mean "keep the old one".
    public void updateBook(int id, String newTitle, String newAuthor)
            throws BookNotFoundException {
        Book book = getBook(id);
        String t = clean(newTitle);
        String a = clean(newAuthor);
        if (!t.isEmpty()) {
            book.setTitle(t);
        }
        if (!a.isEmpty()) {
            book.setAuthor(a);
        }
        save();
    }

    // ---------- Delete ----------
    public void deleteBook(int id) throws BookNotFoundException {
        Book book = getBook(id);
        if (book.isIssued()) {
            throw new IllegalStateException("Cannot delete a book that is currently issued.");
        }
        books.remove(id);
        save();
    }

    // ---------- Issue and return ----------
    public void issueBook(int id, String memberName)
            throws BookNotFoundException, BookNotAvailableException {
        Book book = getBook(id);
        String member = clean(memberName);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty.");
        }
        if (book.isIssued()) {
            throw new BookNotAvailableException(
                    "Book is already issued to " + book.getIssuedTo());
        }
        book.issueTo(member);
        save();
    }

    public void returnBook(int id) throws BookNotFoundException {
        Book book = getBook(id);
        if (!book.isIssued()) {
            throw new IllegalStateException("This book is not issued.");
        }
        book.markReturned();
        save();
    }

    // ---------- helpers ----------
    private void save() {
        repository.save(books.values());
    }

    // The "|" character is our file separator, so it is replaced in user text.
    private String clean(String text) {
        return text == null ? "" : text.replace("|", "-").trim();
    }
}