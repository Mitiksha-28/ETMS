package com.etms.ui;

import com.etms.controller.EventController;
import com.etms.controller.PaymentController;
import com.etms.controller.TicketController;
import com.etms.controller.UserController;
import com.etms.exception.ETMSException;
import com.etms.exception.AuthenticationException;
import com.etms.exception.ValidationException;
import com.etms.model.Event;
import com.etms.model.Payment;
import com.etms.model.Ticket;
import com.etms.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;


public class CustomerDashboard extends JFrame {
    private final User currentUser;
    private final EventController eventController;
    private final TicketController ticketController;
    private final PaymentController paymentController;

    private JTable eventTable;
    private JTable ticketTable;
    private JTable paymentTable;
    private DefaultTableModel eventTableModel;
    private DefaultTableModel ticketTableModel;
    private DefaultTableModel paymentTableModel;

    private JTabbedPane tabbedPane;
    private JPanel profilePanel;
    private JPanel eventsPanel;
    private JPanel ticketsPanel;
    private JPanel paymentsPanel;


    public CustomerDashboard(User user) {
        this.currentUser = user;
        this.eventController = new EventController();
        this.ticketController = new TicketController();
        this.paymentController = new PaymentController();

        initializeUI();
        loadData();
    }


    private void initializeUI() {
        setTitle("Customer Dashboard - Event Ticket Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Create profile panel
        profilePanel = createProfilePanel();
        tabbedPane.addTab("Profile", profilePanel);

        // Create events panel
        eventsPanel = createEventsPanel();
        tabbedPane.addTab("Events", eventsPanel);

        // Create tickets panel
        ticketsPanel = createTicketsPanel();
        tabbedPane.addTab("My Tickets", ticketsPanel);

        // Create payments panel
        paymentsPanel = createPaymentsPanel();
        tabbedPane.addTab("My Payments", paymentsPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }


    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(welcomeLabel, BorderLayout.WEST);

        // Create logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }


    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add user information
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(currentUser.getName(), 20);
        nameField.setEditable(false);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(currentUser.getEmail(), 20);
        emailField.setEditable(false);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(currentUser.getPhone(), 20);
        formPanel.add(phoneField, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton updateButton = new JButton("Update Profile");
        updateButton.addActionListener(e -> handleUpdateProfile(phoneField.getText()));

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(e -> showChangePasswordDialog());

        buttonPanel.add(updateButton);
        buttonPanel.add(changePasswordButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model with non-editable cells
        String[] columns = { "ID", "Name", "Date", "Time", "Venue", "Description", "Type", "Price" };
        eventTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table with proper settings
        eventTable = new JTable(eventTableModel);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        eventTable.getTableHeader().setReorderingAllowed(false);

        // Set preferred column widths
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        eventTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Date
        eventTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Time
        eventTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Venue
        eventTable.getColumnModel().getColumn(5).setPreferredWidth(250); // Description
        eventTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Type
        eventTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Price

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> viewEventDetails());

        JButton bookButton = new JButton("Book Ticket");
        bookButton.addActionListener(e -> bookTicket());

        buttonPanel.add(viewButton);
        buttonPanel.add(bookButton);

        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadData());
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model
        String[] columns = { "ID", "Event", "Seat", "Price", "Type", "Booking Date" };
        ticketTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        ticketTable = new JTable(ticketTableModel);
        JScrollPane scrollPane = new JScrollPane(ticketTable);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> viewTicketDetails());

        JButton cancelButton = new JButton("Cancel Ticket");
        cancelButton.addActionListener(e -> cancelTicket());

        buttonPanel.add(viewButton);
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    private JPanel createPaymentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model
        String[] columns = { "ID", "Amount", "Status", "Transaction Date" };
        paymentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        paymentTable = new JTable(paymentTableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> viewPaymentDetails());

        buttonPanel.add(viewButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    
    private void loadData() {
        try {
            // Show loading indicator
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Load ALL events directly instead of trying upcoming events first
            List<Event> events = eventController.getAllEvents();

            // Clear existing rows
            eventTableModel.setRowCount(0);

            // Debug print
            System.out.println("Loading " + events.size() + " events");

            // Add events to table
            for (Event event : events) {
                Object[] rowData = {
                        event.getEventId(),
                        event.getEventName(),
                        event.getDate().toString(),
                        event.getTime().toString(),
                        event.getVenueId(),
                        event.getDescription(),
                        event.getEventType().toString(),
                        event.getTicketPrice().toString()
                };
                eventTableModel.addRow(rowData);
            }

            // Make sure table is visible and refreshed
            eventTable.revalidate();
            eventTable.repaint();

            // Load tickets
            List<Ticket> tickets = ticketController.getTicketsByUserId(currentUser.getUserId());
            ticketTableModel.setRowCount(0);
            for (Ticket ticket : tickets) {
                Event event = eventController.getEventById(ticket.getEventId());
                String eventName = event != null ? event.getEventName() : "Unknown Event";

                ticketTableModel.addRow(new Object[] {
                        ticket.getTicketId(),
                        eventName,
                        ticket.getSeatNumber(),
                        ticket.getPrice(),
                        ticket.getTicketType(),
                        ticket.getBookingDate()
                });
            }

            // Load payments
            List<Payment> payments = paymentController.getPaymentsByUserId(currentUser.getUserId());
            paymentTableModel.setRowCount(0);
            for (Payment payment : payments) {
                paymentTableModel.addRow(new Object[] {
                        payment.getPaymentId(),
                        payment.getAmount(),
                        payment.getStatus(),
                        payment.getTransactionDate()
                });
            }
        } catch (ETMSException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Reset cursor
            setCursor(Cursor.getDefaultCursor());
        }
    }


    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }


    private void handleUpdateProfile(String phone) {
        try {
            // Validate phone number
            if (phone == null || phone.trim().isEmpty()) {
                throw new ValidationException("Phone number cannot be empty");
            }
            if (!phone.matches("\\d{10}")) {
                throw new ValidationException("Phone number must be 10 digits");
            }

            // Show loading indicator
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Update user in database using UserController
            UserController userController = new UserController();
            currentUser.setPhone(phone);
            userController.updateUser(currentUser);

            JOptionPane.showMessageDialog(this,
                    "Profile updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ETMSException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating profile: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Reset cursor
            setCursor(Cursor.getDefaultCursor());
        }
    }


    private void showChangePasswordDialog() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Old Password:"), gbc);

        gbc.gridx = 1;
        panel.add(oldPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("New Password:"), gbc);

        gbc.gridx = 1;
        panel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Confirm New Password:"), gbc);

        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        int result = JOptionPane.showConfirmDialog(this,
                panel,
                "Change Password",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Show loading indicator
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                String oldPassword = new String(oldPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Validate passwords
                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    throw new ValidationException("All password fields are required");
                }
                if (!newPassword.equals(confirmPassword)) {
                    throw new ValidationException("New passwords do not match");
                }
                if (newPassword.length() < 8) {
                    throw new ValidationException("New password must be at least 8 characters long");
                }

                // Change password using UserController
                UserController userController = new UserController();
                boolean success = userController.changePassword(currentUser.getUserId(), oldPassword, newPassword);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Password changed successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new AuthenticationException("Failed to change password. Please check your old password.");
                }
            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (AuthenticationException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Authentication Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (ETMSException e) {
                JOptionPane.showMessageDialog(this,
                        "Error changing password: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                // Reset cursor
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }


    private void viewEventDetails() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an event to view",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int eventId = (int) eventTableModel.getValueAt(selectedRow, 0);

        try {
            Event event = eventController.getEventById(eventId);
            if (event != null) {
                StringBuilder details = new StringBuilder();
                details.append("Event: ").append(event.getEventName()).append("\n");
                details.append("Date: ").append(event.getDate()).append("\n");
                details.append("Time: ").append(event.getTime()).append("\n");
                details.append("Venue: ").append(event.getVenueId()).append("\n");
                details.append("Description: ").append(event.getDescription()).append("\n");
                details.append("Type: ").append(event.getEventType()).append("\n");
                details.append("Price: ").append(event.getTicketPrice()).append("\n");

                JOptionPane.showMessageDialog(this,
                        details.toString(),
                        "Event Details",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ETMSException e) {
            JOptionPane.showMessageDialog(this,
                    "Error retrieving event details: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void bookTicket() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an event to book",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int eventId = (int) eventTableModel.getValueAt(selectedRow, 0);

        try {
            Event event = eventController.getEventById(eventId);
            if (event != null) {
                // Show booking dialog
                showBookingDialog(event);
            }
        } catch (ETMSException e) {
            JOptionPane.showMessageDialog(this,
                    "Error booking ticket: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void showBookingDialog(Event event) {
        // This is a simplified booking dialog
        // In a real application, this would be more complex

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> ticketTypeComboBox = new JComboBox<>(new String[] { "GENERAL", "VIP" });
        JTextField seatNumberField = new JTextField(10);
        JTextField quantityField = new JTextField("1", 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Ticket Type:"), gbc);

        gbc.gridx = 1;
        panel.add(ticketTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Seat Number:"), gbc);

        gbc.gridx = 1;
        panel.add(seatNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        int result = JOptionPane.showConfirmDialog(this,
                panel,
                "Book Ticket for " + event.getEventName(),
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String ticketType = (String) ticketTypeComboBox.getSelectedItem();
                String seatNumber = seatNumberField.getText();
                int quantity = Integer.parseInt(quantityField.getText());

                // Create ticket
                Ticket ticket = new Ticket();
                ticket.setUserId(currentUser.getUserId());
                ticket.setEventId(event.getEventId());
                ticket.setSeatNumber(seatNumber);
                ticket.setTicketType(Ticket.TicketType.valueOf(ticketType));
                ticket.setPrice(event.getTicketPrice());
                ticket.setBookingDate(LocalDateTime.now());

                // Save ticket
                ticketController.createTicket(ticket);

                // Create payment
                Payment payment = new Payment();
                payment.setUserId(currentUser.getUserId());
                payment.setAmount(event.getTicketPrice().multiply(java.math.BigDecimal.valueOf(quantity)));
                payment.setStatus(Payment.PaymentStatus.COMPLETED);
                payment.setTransactionDate(LocalDateTime.now());

                // Save payment
                paymentController.createPayment(payment);

                JOptionPane.showMessageDialog(this,
                        "Ticket booked successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Reload data
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error booking ticket: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void viewTicketDetails() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a ticket to view",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int ticketId = (int) ticketTableModel.getValueAt(selectedRow, 0);

        try {
            Ticket ticket = ticketController.getTicketById(ticketId);
            if (ticket != null) {
                Event event = eventController.getEventById(ticket.getEventId());
                String eventName = event != null ? event.getEventName() : "Unknown Event";

                StringBuilder details = new StringBuilder();
                details.append("Ticket ID: ").append(ticket.getTicketId()).append("\n");
                details.append("Event: ").append(eventName).append("\n");
                details.append("Seat: ").append(ticket.getSeatNumber()).append("\n");
                details.append("Price: ").append(ticket.getPrice()).append("\n");
                details.append("Type: ").append(ticket.getTicketType()).append("\n");
                details.append("Booking Date: ").append(ticket.getBookingDate()).append("\n");

                JOptionPane.showMessageDialog(this,
                        details.toString(),
                        "Ticket Details",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ETMSException e) {
            JOptionPane.showMessageDialog(this,
                    "Error retrieving ticket details: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void cancelTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a ticket to cancel",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int ticketId = (int) ticketTableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this ticket?",
                "Cancel Ticket",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                boolean success = ticketController.cancelTicket(ticketId);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Ticket cancelled successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Reload data
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to cancel ticket",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (ETMSException e) {
                JOptionPane.showMessageDialog(this,
                        "Error cancelling ticket: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void viewPaymentDetails() {
        int selectedRow = paymentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a payment to view",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int paymentId = (int) paymentTableModel.getValueAt(selectedRow, 0);

        try {
            Payment payment = paymentController.getPaymentById(paymentId);
            if (payment != null) {
                StringBuilder details = new StringBuilder();
                details.append("Payment ID: ").append(payment.getPaymentId()).append("\n");
                details.append("Amount: ").append(payment.getAmount()).append("\n");
                details.append("Status: ").append(payment.getStatus()).append("\n");
                details.append("Transaction Date: ").append(payment.getTransactionDate()).append("\n");

                JOptionPane.showMessageDialog(this,
                        details.toString(),
                        "Payment Details",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ETMSException e) {
            JOptionPane.showMessageDialog(this,
                    "Error retrieving payment details: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
