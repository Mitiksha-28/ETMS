package com.etms.ui;

import com.etms.controller.UserController;
import com.etms.exception.ETMSException;
import com.etms.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        Color bgColor = new Color(245, 245, 245);
        Color accentColor = new Color(0, 120, 215);
        Color fieldColor = Color.WHITE;
        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(bgColor);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(accentColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        nameField = createStyledTextField(font, fieldColor);
        emailField = createStyledTextField(font, fieldColor);
        phoneField = createStyledTextField(font, fieldColor);
        passwordField = createStyledPasswordField(font, fieldColor);
        confirmPasswordField = createStyledPasswordField(font, fieldColor);
        userTypeCombo = new JComboBox<>(User.UserType.values());
        userTypeCombo.setFont(font);
        userTypeCombo.setBackground(fieldColor);

        addLabeledField(formPanel, gbc, "Full Name:", nameField, 1);
        addLabeledField(formPanel, gbc, "Email:", emailField, 2);
        addLabeledField(formPanel, gbc, "Phone Number:", phoneField, 3);
        addLabeledField(formPanel, gbc, "Password:", passwordField, 4);
        addLabeledField(formPanel, gbc, "Confirm Password:", confirmPasswordField, 5);
        addLabeledField(formPanel, gbc, "User Type:", userTypeCombo, 6);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(bgColor);

        registerButton = createStyledButton("Register", accentColor, Color.BLACK);
        cancelButton = createStyledButton("Cancel", new Color(200, 0, 0), Color.BLACK);

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Action listeners
        registerButton.addActionListener(e -> handleRegistration());
        cancelButton.addActionListener(e -> dispose());
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, String label, Component field, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JTextField createStyledTextField(Font font, Color bg) {
        JTextField field = new JTextField(20);
        field.setFont(font);
        field.setBackground(bg);
        return field;
    }

    private JPasswordField createStyledPasswordField(Font font, Color bg) {
        JPasswordField field = new JPasswordField(20);
        field.setFont(font);
        field.setBackground(bg);
        return field;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bg.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bg);
            }
        });
        return button;
    }

    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        User.UserType userType = (User.UserType) userTypeCombo.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setPassword(password); // In real applications, hash this
            newUser.setUserType(userType);

            userController.registerUser(newUser);

            JOptionPane.showMessageDialog(this,
                    "Registration successful! Please login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (ETMSException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}