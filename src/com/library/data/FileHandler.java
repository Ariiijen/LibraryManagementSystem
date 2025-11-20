package com.library.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String USERS_FILE = "data/users.dat";
    private static final String BOOKS_FILE = "data/books.dat";
    private static final String BORROW_RECORDS_FILE = "data/borrow_records.dat";

    // Create data directory if it doesn't exist
    static {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    // User file operations
    public static void saveUsers(List<Object> users) {
        saveToFile(users, USERS_FILE);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> loadUsers() {
        List<Object> users = (List<Object>) loadFromFile(USERS_FILE);
        return users != null ? users : new ArrayList<>();
    }

    // Book file operations
    public static void saveBooks(List<Object> books) {
        saveToFile(books, BOOKS_FILE);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> loadBooks() {
        List<Object> books = (List<Object>) loadFromFile(BOOKS_FILE);
        return books != null ? books : new ArrayList<>();
    }

    // Generic save method
    private static void saveToFile(List<Object> data, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    // Generic load method
    private static Object loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from " + filename + ": " + e.getMessage());
            return null;
        }
    }

    // Backup methods
    public static void createBackup() {
        String backupDir = "backup/" + System.currentTimeMillis() + "/";
        new File(backupDir).mkdirs();
        
        copyFile(USERS_FILE, backupDir + "users.dat");
        copyFile(BOOKS_FILE, backupDir + "books.dat");
        copyFile(BORROW_RECORDS_FILE, backupDir + "borrow_records.dat");
    }

    private static void copyFile(String sourcePath, String destPath) {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) return;
        
        try (InputStream in = new FileInputStream(sourcePath);
             OutputStream out = new FileOutputStream(destPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.err.println("Backup failed for: " + sourcePath);
        }
    }
}