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
import java.time.LocalDate;
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
        getContentPane().setBackground(new Color(240, 240, 245));

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 245));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create tabbed pane with custom styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(51, 51, 51));
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

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
        tabbedPane.addTab("Payments", paymentsPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }


    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setBackground(new Color(70, 130, 180)); // Steel Blue

        // Create welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        panel.add(welcomeLabel, BorderLayout.WEST);

        // Create logout button with new color scheme
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(220, 53, 69)); // Bootstrap danger red
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> handleLogout());
        panel.add(logoutButton, BorderLayout.EAST);

        return panel;
    }

    private JButton createStyledButton(String text, String emoji) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(new Color(70, 130, 180));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates the profile panel with user information and edit options.
     * 
     * @return the profile panel
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Style form fields
        JTextField nameField = new JTextField(currentUser.getName(), 20);
        nameField.setEditable(false);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField emailField = new JTextField(currentUser.getEmail(), 20);
        emailField.setEditable(false);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField phoneField = new JTextField(currentUser.getPhone(), 20);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add styled labels and fields
        addFormField(formPanel, "Name:", nameField, gbc, 0);
        addFormField(formPanel, "Email:", emailField, gbc, 1);
        addFormField(formPanel, "Phone:", phoneField, gbc, 2);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton updateButton = createStyledButton("Update Profile", "💾");
        JButton changePasswordButton = createStyledButton("Change Password", "🔑");

        updateButton.addActionListener(e -> handleUpdateProfile(phoneField.getText()));
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

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 14));
        jLabel.setForeground(new Color(70, 130, 180));
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    /**
     * Creates the events panel with a table of available events.
     * 
     * @return the events panel
     */
    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Create table model with column names
        String[] columnNames = { "ID", "Name", "Date", "Time", "Description", "Type", "Price" };
        eventTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and configure table
        eventTable = new JTable(eventTableModel);
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        eventTable.getTableHeader().setReorderingAllowed(false);
        eventTable.setRowHeight(30);
        eventTable.setFont(new Font("Arial", Font.PLAIN, 14));
        eventTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        eventTable.getTableHeader().setBackground(new Color(70, 130, 180));
        eventTable.getTableHeader().setForeground(Color.WHITE);
        eventTable.setSelectionBackground(new Color(173, 216, 230));
        eventTable.setSelectionForeground(new Color(51, 51, 51));

        // Set column widths
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        eventTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Date
        eventTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Time
        eventTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Venue
        eventTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Description
        eventTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Type
        eventTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Price

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton viewDetailsButton = createStyledButton("View Details", "🔍");
        JButton bookTicketButton = createStyledButton("Book Ticket", "🎟️");
        JButton refreshButton = createStyledButton("Refresh", "🔄");

        viewDetailsButton.addActionListener(e -> handleViewEventDetails());
        bookTicketButton.addActionListener(e -> handleBookTicket());
        refreshButton.addActionListener(e -> loadData());

        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(bookTicketButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Create table model with columns
        ticketTableModel = new DefaultTableModel(
                new String[] { "ID", "Event", "Seat", "Type", "Price", "Status" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and configure table
        ticketTable = new JTable(ticketTableModel);
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketTable.setRowHeight(30);
        ticketTable.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        ticketTable.getTableHeader().setBackground(new Color(70, 130, 180));
        ticketTable.getTableHeader().setForeground(Color.WHITE);
        ticketTable.setSelectionBackground(new Color(173, 216, 230));
        ticketTable.getTableHeader().setReorderingAllowed(false);

        // Set column widths
        ticketTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        ticketTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Event
        ticketTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Seat
        ticketTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Type
        ticketTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Price
        ticketTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Status

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewDetailsButton = new JButton("🔍 View Details");
        JButton cancelButton = new JButton("❌ Cancel Ticket");
        JButton refreshButton = new JButton("🔄 Refresh");

        viewDetailsButton.addActionListener(e -> handleViewTicketDetails());
        cancelButton.addActionListener(e -> handleCancelTicket());
        refreshButton.addActionListener(e -> loadData());

        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }


    private JPanel createPaymentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Create table model
        String[] columns = { "ID", "Amount", "Status", "Transaction Date" };
        paymentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table with custom styling
        paymentTable = new JTable(paymentTableModel);
        paymentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentTable.setRowHeight(25);
        paymentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Create button panel with consistent styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = createStyledButton("View Details", "👁️");
        viewButton.addActionListener(e -> viewPaymentDetails());

        JButton refreshButton = createStyledButton("Refresh", "🔄");
        refreshButton.addActionListener(e -> loadData());

        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    
    private void loadData() {
        try {
            // Show loading indicator
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Load events
            List<Event> events = eventController.getUpcomingEvents();
            if (events.isEmpty()) {
                events = eventController.getAllEvents();
            }
            eventTableModel.setRowCount(0);
            for (Event event : events) {
                Object[] rowData = {
                        event.getEventId(),
                        event.getEventName(),
                        event.getDate(),
                                event.getTime(),
                        event.getDescription(),
                        event.getEventType(),
                        event.getTicketPrice()
                };
                eventTableModel.addRow(rowData);
            }

            // Load tickets
            List<Ticket> tickets = ticketController.getTicketsByUserId(currentUser.getUserId());
            ticketTableModel.setRowCount(0);
            LocalDate today = LocalDate.now();

            for (Ticket ticket : tickets) {
                Event event = eventController.getEventById(ticket.getEventId());
                String status = event != null && event.getDate().isAfter(today) ? "Upcoming" : "Past";

                Object[] rowData = {
                        ticket.getTicketId(),
                        event != null ? event.getEventName() : "Unknown Event",
                                ticket.getSeatNumber(),
                                ticket.getTicketType(),
                        ticket.getPrice(),
                        status
                };
                ticketTableModel.addRow(rowData);
            }

            // Load payments
            List<Payment> payments = paymentController.getPaymentsByUserId(currentUser.getUserId());
            paymentTableModel.setRowCount(0);
            for (Payment payment : payments) {
                paymentTableModel.addRow(new Object[] {
                        payment.getPaymentId(),
                        payment.getAmount(),
                        payment.getStatus().toString(),
                        payment.getTransactionDate()
                });
            }

            // Reset cursor
            setCursor(Cursor.getDefaultCursor());
        } catch (ETMSException e) {
            setCursor(Cursor.getDefaultCursor());
            JOptionPane.showMessageDialog(this,
                    "Error loading data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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

    /**
     * Views the details of the selected event.
     */
    private void handleViewEventDetails() {
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

    /**
     * Books a ticket for the selected event.
     */
    private void handleBookTicket() {
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

        JComboBox<String> ticketTypeComboBox = new JComboBox<>(new String[] { "General", "VIP" });
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
                // Validate inputs
                if (seatNumberField.getText().trim().isEmpty()) {
                    throw new ValidationException("Please enter a seat number");
                }

                String ticketType = (String) ticketTypeComboBox.getSelectedItem();
                String seatNumber = seatNumberField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText());

                if (quantity <= 0) {
                    throw new ValidationException("Quantity must be greater than 0");
                }

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
            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid quantity",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error booking ticket: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Views the details of the selected ticket.
     */
    private void handleViewTicketDetails() {
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

    /**
     * Cancels the selected ticket.
     */
    private void handleCancelTicket() {
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
