package com.library.model;

public class Book {
    private final int id;
    private String title;
    private String author;
    private boolean issued;
    private String issuedTo; // empty when the book is available

    public Book(int id, String title, String author) {
        this(id, title, author, false, "");
    }

    public Book(int id, String title, String author, boolean issued, String issuedTo) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issued = issued;
        this.issuedTo = issuedTo;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public boolean isIssued() { return issued; }

    public String getIssuedTo() { return issuedTo; }

    public void issueTo(String memberName) {
        this.issued = true;
        this.issuedTo = memberName;
    }

    public void markReturned() {
        this.issued = false;
        this.issuedTo = "";
    }

    // One line in the data file: id|title|author|issued|issuedTo
    public String toCsv() {
        return id + "|" + title + "|" + author + "|" + issued + "|" + issuedTo;
    }

    public static Book fromCsv(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length != 5) {
            throw new IllegalArgumentException("Bad record: " + line);
        }
        try {
            return new Book(Integer.parseInt(p[0]), p[1], p[2],
                    Boolean.parseBoolean(p[3]), p[4]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Bad record: " + line);
        }
    }

    @Override
    public String toString() {
        String status = issued ? "Issued to " + issuedTo : "Available";
        return String.format("%-4d %-30s %-20s %s", id, title, author, status);
    }
}
