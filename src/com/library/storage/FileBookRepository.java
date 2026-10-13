package com.library.storage;

import com.library.model.Book;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileBookRepository {
    private final Path file;

    public FileBookRepository(String filePath) {
        this.file = Paths.get(filePath);
    }

    // Reads all books from the file. Returns an empty list on the first run.
    public List<Book> load() {
        List<Book> books = new ArrayList<>();
        if (!Files.exists(file)) {
            return books;
        }
        try {
            for (String line : Files.readAllLines(file)) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    books.add(Book.fromCsv(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipped a corrupt line in the data file.");
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read data file: " + e.getMessage());
        }
        return books;
    }

    // Overwrites the file with the current list of books.
    public void save(Collection<Book> books) {
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (Book book : books) {
                lines.add(book.toCsv());
            }
            Files.write(file, lines);
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }
}
