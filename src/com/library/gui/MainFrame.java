package com.library.gui;

import com.library.data.DataStorage;
import com.library.models.Book;
import com.library.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private DataStorage dataStorage;
    private User currentUser;
    private JTabbedPane tabbedPane;
    private BookPanel bookPanel;
    private SearchPanel searchPanel;

    public MainFrame() {
        dataStorage = DataStorage.getInstance();
        currentUser = dataStorage.getCurrentUser();
        initializeUI();
    }

    private void initializeUI() {
        String displayName = (currentUser != null && currentUser.getFullName() != null) ? currentUser.getFullName() : "Guest";
        setTitle("Library Management System - Welcome, " + displayName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Create menu bar
        createMenuBar();

        // Create main tabbed pane
        tabbedPane = new JTabbedPane();

        // Add panels based on user role
        bookPanel = new BookPanel();
        tabbedPane.addTab("Books", bookPanel);

        searchPanel = new SearchPanel();
        tabbedPane.addTab("Search", searchPanel);

        String role = (currentUser != null && currentUser.getRole() != null) ? currentUser.getRole() : "user";
        if (role.equals("admin") || role.equals("librarian")) {
            tabbedPane.addTab("Manage Books", new ManageBooksPanel());
        }

        add(tabbedPane);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");

        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dataStorage.logout();
            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Library Management System\nVersion 2.0\n\nWith File Handling & User Registration\nDeveloped by: Allyza Espragera, Jennifer Barnedo, Shenaiah Sabana",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    // Inner classes for different panels
    private class BookPanel extends JPanel {
        private JTable bookTable;
        private BookTableModel tableModel;

        public BookPanel() {
            setLayout(new BorderLayout());
            initializeComponents();
        }

        private void initializeComponents() {
            // Table
            tableModel = new BookTableModel(dataStorage.getAllBooks());
            bookTable = new JTable(tableModel);
            bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            // Set column widths
            bookTable.getColumnModel().getColumn(0).setPreferredWidth(120); // ISBN
            bookTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
            bookTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Author
            bookTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Genre
            bookTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Publisher
            bookTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // Year
            bookTable.getColumnModel().getColumn(6).setPreferredWidth(70);  // Available
            bookTable.getColumnModel().getColumn(7).setPreferredWidth(60);  // Total

            JScrollPane scrollPane = new JScrollPane(bookTable);
            add(scrollPane, BorderLayout.CENTER);

            // Refresh button
            JButton refreshButton = new JButton("Refresh");
            refreshButton.addActionListener(e -> refreshBooks());

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(refreshButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void refreshBooks() {
            tableModel.setBooks(dataStorage.getAllBooks());
            tableModel.fireTableDataChanged();
        }
    }

    private class SearchPanel extends JPanel {
        private JTextField searchField;
        private JButton searchButton;
        private JTable resultTable;
        private BookTableModel tableModel;

        public SearchPanel() {
            setLayout(new BorderLayout(10, 10));
            initializeComponents();
        }

        private void initializeComponents() {
            // Search panel
            JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
            searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            searchField = new JTextField();
            searchButton = new JButton("Search");

            searchPanel.add(new JLabel("Search (title, author, genre, ISBN, publisher):"), BorderLayout.NORTH);
            searchPanel.add(searchField, BorderLayout.CENTER);
            searchPanel.add(searchButton, BorderLayout.EAST);

            // Results table
            tableModel = new BookTableModel(new ArrayList<>());
            resultTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(resultTable);

            // Layout
            add(searchPanel, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);

            // Event listeners
            searchButton.addActionListener(e -> performSearch());
            searchField.addActionListener(e -> performSearch());
        }

        private void performSearch() {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term");
                return;
            }

            List<Book> results = dataStorage.searchBooks(query);
            tableModel.setBooks(results);
            tableModel.fireTableDataChanged();

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No books found matching your search");
            }
        }
    }

    private class ManageBooksPanel extends JPanel {
        private JTextField isbnField, titleField, authorField, genreField, publisherField;
        private JTextField yearField, copiesField, descriptionField;
        private JButton addButton, removeButton, updateButton;

        public ManageBooksPanel() {
            setLayout(new BorderLayout(10, 10));
            initializeComponents();
        }

        private void initializeComponents() {
            // Form panel
            JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createTitledBorder("Manage Books"));

            isbnField = new JTextField();
            titleField = new JTextField();
            authorField = new JTextField();
            genreField = new JTextField();
            publisherField = new JTextField();
            yearField = new JTextField();
            copiesField = new JTextField();
            descriptionField = new JTextField();

            formPanel.add(new JLabel("ISBN:*"));
            formPanel.add(isbnField);
            formPanel.add(new JLabel("Title:*"));
            formPanel.add(titleField);
            formPanel.add(new JLabel("Author:*"));
            formPanel.add(authorField);
            formPanel.add(new JLabel("Genre:*"));
            formPanel.add(genreField);
            formPanel.add(new JLabel("Publisher:"));
            formPanel.add(publisherField);
            formPanel.add(new JLabel("Publication Year:"));
            formPanel.add(yearField);
            formPanel.add(new JLabel("Total Copies:*"));
            formPanel.add(copiesField);
            formPanel.add(new JLabel("Description:"));
            formPanel.add(descriptionField);

            // Buttons panel
            JPanel buttonPanel = new JPanel(new FlowLayout());
            addButton = new JButton("Add Book");
            removeButton = new JButton("Remove Book");
            updateButton = new JButton("Update Book");

            addButton.addActionListener(e -> addBook());
            removeButton.addActionListener(e -> removeBook());
            updateButton.addActionListener(e -> updateBook());

            buttonPanel.add(addButton);
            buttonPanel.add(removeButton);
            buttonPanel.add(updateButton);

            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void addBook() {
            try {
                String isbn = isbnField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String genre = genreField.getText().trim();
                String publisher = publisherField.getText().trim();
                int year = yearField.getText().isEmpty() ? 2023 : Integer.parseInt(yearField.getText().trim());
                int copies = Integer.parseInt(copiesField.getText().trim());
                String description = descriptionField.getText().trim();

                if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields (*)");
                    return;
                }

                Book existingBook = dataStorage.findBookByIsbn(isbn);
                if (existingBook != null) {
                    JOptionPane.showMessageDialog(this, "Book with this ISBN already exists");
                    return;
                }

                Book newBook = new Book(isbn, title, author, genre, publisher, year, copies, description);
                dataStorage.addBook(newBook);

                JOptionPane.showMessageDialog(this, "Book added successfully!");
                clearFields();
                bookPanel.refreshBooks();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for year and copies");
            }
        }

        private void removeBook() {
            String isbn = isbnField.getText().trim();
            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter ISBN to remove");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this book?",
                    "Confirm Removal", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean removed = dataStorage.removeBook(isbn);
                if (removed) {
                    JOptionPane.showMessageDialog(this, "Book removed successfully!");
                    clearFields();
                    bookPanel.refreshBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found with ISBN: " + isbn);
                }
            }
        }

        private void updateBook() {
            try {
                String isbn = isbnField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String genre = genreField.getText().trim();
                String publisher = publisherField.getText().trim();
                int year = yearField.getText().isEmpty() ? 2023 : Integer.parseInt(yearField.getText().trim());
                int copies = Integer.parseInt(copiesField.getText().trim());
                String description = descriptionField.getText().trim();

                if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields (*)");
                    return;
                }

                Book existingBook = dataStorage.findBookByIsbn(isbn);
                if (existingBook == null) {
                    JOptionPane.showMessageDialog(this, "Book not found with ISBN: " + isbn);
                    return;
                }

                Book updatedBook = new Book(isbn, title, author, genre, publisher, year, copies, description);
                dataStorage.updateBook(updatedBook);

                JOptionPane.showMessageDialog(this, "Book updated successfully!");
                clearFields();
                bookPanel.refreshBooks();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for year and copies");
            }
        }

        private void clearFields() {
            isbnField.setText("");
            titleField.setText("");e
            authorField.setText("");
            genreField.setText("");
            publisherField.setText("");
            yearField.setText("");
            copiesField.setText("");
            descriptionField.setText("");
        }

        private void setText(String string) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'setText'");
        }
    }
}