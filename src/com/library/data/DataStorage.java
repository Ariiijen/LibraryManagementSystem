package com.library.data;

import com.library.models.Book;
import com.library.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataStorage {
    private static DataStorage instance;
    private List<User> users;
    private List<Book> books;
    private User currentUser;

    private DataStorage() {
        loadData();
        // If no data exists, initialize with sample data
        if (users.isEmpty() || books.isEmpty()) {
            initializeSampleData();
            saveData();
        }
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        // Load users
        List<Object> userObjects = FileHandler.loadUsers();
        users = userObjects.stream()
                .filter(obj -> obj instanceof User)
                .map(obj -> (User) obj)
                .collect(Collectors.toCollection(ArrayList::new));

        // Load books
        List<Object> bookObjects = FileHandler.loadBooks();
        books = bookObjects.stream()
                .filter(obj -> obj instanceof Book)
                .map(obj -> (Book) obj)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void saveData() {
        FileHandler.saveUsers(new ArrayList<>(users));
        FileHandler.saveBooks(new ArrayList<>(books));
        FileHandler.createBackup();
    }

    private void initializeSampleData() {
        // Initialize default admin user
        users.add(new User("admin", "admin123", "admin", "System Administrator", "admin@library.com", "123-456-7890"));
        users.add(new User("librarian", "lib123", "librarian", "Jane Smith", "jane@library.com", "123-456-7891"));
        users.add(new User("user", "user123", "user", "John Doe", "john@email.com", "123-456-7892"));
        
        // Initialize sample books
        books.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch", "Programming", 
                          "Addison-Wesley", 2018, 5, "A comprehensive guide to Java programming best practices"));
        books.add(new Book("978-0201633610", "Design Patterns", "Erich Gamma", "Computer Science", 
                          "Addison-Wesley", 1994, 3, "Elements of Reusable Object-Oriented Software"));
        books.add(new Book("978-0596009205", "Head First Java", "Kathy Sierra", "Programming", 
                          "O'Reilly", 2005, 4, "A brain-friendly guide to Java programming"));
        books.add(new Book("978-0061120084", "To Kill a Mockingbird", "Harper Lee", "Fiction", 
                          "J.B. Lippincott", 1960, 2, "A classic novel about racial inequality"));
        books.add(new Book("978-0439023481", "The Hunger Games", "Suzanne Collins", "Science Fiction", 
                          "Scholastic", 2008, 3, "Dystopian novel set in a post-apocalyptic nation"));
    }

    // User methods
    public User authenticateUser(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public boolean registerUser(String username, String password, String fullName, String email, String phoneNumber) {
        // Check if username already exists
        if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            return false;
        }

        User newUser = new User(username, password, "user", fullName, email, phoneNumber);
        users.add(newUser);
        saveData();
        return true;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }

    // Book methods
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public void addBook(Book book) {
        books.add(book);
        saveData();
    }

    public boolean removeBook(String isbn) {
        boolean removed = books.removeIf(book -> book.getIsbn().equals(isbn));
        if (removed) {
            saveData();
        }
        return removed;
    }

    public Book findBookByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    public List<Book> searchBooks(String query) {
        String lowerQuery = query.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerQuery) ||
                               book.getAuthor().toLowerCase().contains(lowerQuery) ||
                               book.getGenre().toLowerCase().contains(lowerQuery) ||
                               book.getIsbn().contains(query) ||
                               book.getPublisher().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(updatedBook.getIsbn())) {
                books.set(i, updatedBook);
                saveData();
                break;
            }
        }
    }
}