package com.etms.ui;

import com.etms.controller.UserController;
import com.etms.exception.ETMSException;
import com.etms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationFrame extends JFrame {
    private final UserController userController;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField phoneField;
    private JComboBox<User.UserType> userTypeCombo;
    private JButton registerButton;
    private JButton cancelButton;

    public RegistrationFrame() {
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Event Ticket Management System - Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with some padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add title label
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);

        // Add name field
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Add email field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        // Add phone field
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);

        // Add password field
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Add confirm password field
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);

        // Add user type combo box
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("User Type:"), gbc);

        gbc.gridx = 1;
        userTypeCombo = new JComboBox<>(User.UserType.values());
        formPanel.add(userTypeCombo, gbc);

        // Add buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        User.UserType userType = (User.UserType) userTypeCombo.getSelectedItem();

        // Validate input
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create new user
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setPassword(password); // Note: In a real application, password should be hashed
            newUser.setUserType(userType);

            // Save user to database
            userController.registerUser(newUser);

            JOptionPane.showMessageDialog(this,
                    "Registration successful! Please login.",
                    "Registration Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } catch (ETMSException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}