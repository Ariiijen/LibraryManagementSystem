package com.library.gui;

import com.library.data.DataStorage;
import com.library.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton, cancelButton;
    private DataStorage dataStorage;
    private JTabbedPane tabbedPane;

    public LoginFrame() {
        dataStorage = DataStorage.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Login", createLoginPanel());
        tabbedPane.addTab("Register", createRegisterPanel());

        add(tabbedPane);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Library Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Login to Your Account"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        registerButton = new JButton("Create New Account");
        cancelButton = new JButton("Cancel");

        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        cancelButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(loginButton);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Create New Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registration Form"));

        JTextField regUsernameField = new JTextField();
        JPasswordField regPasswordField = new JPasswordField();
        JTextField fullNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        formPanel.add(new JLabel("Username:"));
        formPanel.add(regUsernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(regPasswordField);
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(fullNameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        registerBtn.addActionListener(e -> {
            String username = regUsernameField.getText().trim();
            String password = new String(regPasswordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (username.length() < 3) {
                JOptionPane.showMessageDialog(this, "Username must be at least 3 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 4) {
                JOptionPane.showMessageDialog(this, "Password must be at least 4 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = dataStorage.registerUser(username, password, fullName, email, phone);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                tabbedPane.setSelectedIndex(0);
                clearRegistrationFields(regUsernameField, regPasswordField, fullNameField, emailField, phoneField);
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void clearRegistrationFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Please enter both username and password",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = dataStorage.authenticateUser(username, password);
            if (user != null) {
                dataStorage.setCurrentUser(user);
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Welcome, " + user.getFullName() + "!",
                        "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Invalid username or password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                usernameField.requestFocus();
            }
        }
    }
}