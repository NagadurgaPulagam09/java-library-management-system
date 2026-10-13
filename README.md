# Library Management System (Java)

A console-based library application built with core Java. It supports full CRUD operations on books and stores data in a file so it persists between runs.

## Features
- Add, view, search, update and delete books
- Issue a book to a member and return it
- Data saved automatically to a CSV file and loaded on startup
- Custom exceptions for missing and unavailable books
- Input validation on every prompt

## Concepts Used
- CRUD operations
- File handling with java.nio.file (Files, Path)
- Collections: LinkedHashMap, ArrayList
- Custom checked exceptions
- Layered structure: model, storage, service, exception, main

## Project Structure
```
src/com/library
├── model       Book
├── exception   BookNotFoundException, BookNotAvailableException
├── storage     FileBookRepository
├── service     LibraryService
└── main        LibraryAppMain
```

## How to Run
1. Clone: `git clone https://github.com/NagadurgaPulagam09/java-library-management-system.git`
2. Open in Eclipse (File → Import → Existing Projects into Workspace)
3. Run `com.library.main.LibraryAppMain` as a Java Application
4. Books are saved to `data/books.csv` (created automatically)

## Future Improvements
- Replace file storage with MySQL using JDBC
- Add member accounts and due dates with fines
- Add JUnit tests
