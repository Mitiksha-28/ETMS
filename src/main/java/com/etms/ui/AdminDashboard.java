package com.etms.ui;

import com.etms.controller.EventController;
import com.etms.controller.TicketController;
import com.etms.controller.UserController;
import com.etms.model.Event;
import com.etms.model.Ticket;
import com.etms.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AdminDashboard extends JFrame {
    private User currentUser;
    private JTable eventTable;
    private JTable ticketTable;
    private JTable userTable;
    private DefaultTableModel eventTableModel;
    private DefaultTableModel ticketTableModel;
    private DefaultTableModel userTableModel;
    private EventController eventController;
    private TicketController ticketController;
    private UserController userController;

    public AdminDashboard(User user) {
        this.currentUser = user;
        this.eventController = new EventController();
        this.ticketController = new TicketController();
        this.userController = new UserController();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setTitle("Admin Dashboard - Event Ticket Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
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
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(51, 51, 51));
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Events tab
        JPanel eventsPanel = createEventsPanel();
        tabbedPane.addTab("Events", eventsPanel);

        // Tickets tab
        JPanel ticketsPanel = createTicketsPanel();
        tabbedPane.addTab("Tickets", ticketsPanel);

        // Users tab
        JPanel usersPanel = createUsersPanel();
        tabbedPane.addTab("Users", usersPanel);

        // Add tabbed pane to main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add main panel to frame
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setBackground(new Color(70, 130, 180)); // Steel Blue

        // Create welcome label
        JLabel welcomeLabel = new JLabel("Welcome, Admin " + currentUser.getName());
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

    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Create table model
        String[] columns = { "ID", "Name", "Date", "Time", "VenueID", "Venue Name", "Event Type", "Ticket Price" };
        eventTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table with custom styling
        eventTable = new JTable(eventTableModel);
        eventTable.setFont(new Font("Arial", Font.PLAIN, 14));
        eventTable.setRowHeight(25);
        eventTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        eventTable.getTableHeader().setBackground(new Color(70, 130, 180));
        eventTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(eventTable);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Add Event", "➕");
        JButton editButton = createStyledButton("Edit Event", "✏️");
        JButton deleteButton = createStyledButton("Delete Event", "🗑️");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Add action listeners
        addButton.addActionListener(e -> showAddEventDialog());
        editButton.addActionListener(e -> editSelectedEvent());
        deleteButton.addActionListener(e -> deleteSelectedEvent());

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Create table model
        String[] columns = { "ID", "Event", "Customer", "Seat Number", "Price", "Type" };
        ticketTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table with custom styling
        ticketTable = new JTable(ticketTableModel);
        ticketTable.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketTable.setRowHeight(25);
        ticketTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        ticketTable.getTableHeader().setBackground(new Color(70, 130, 180));
        ticketTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(ticketTable);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = createStyledButton("View Details", "👁️");
        JButton cancelButton = createStyledButton("Cancel Ticket", "❌");

        buttonPanel.add(viewButton);
        buttonPanel.add(cancelButton);

        viewButton.addActionListener(e -> viewTicketDetails());
        cancelButton.addActionListener(e -> cancelTicket());

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Create table model
        String[] columns = { "ID", "Name", "Email", "Phone No.", "User Type" };
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table with custom styling
        userTable = new JTable(userTableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.setRowHeight(25);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(new Color(70, 130, 180));
        userTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = createStyledButton("View User", "👤");
        JButton deleteButton = createStyledButton("Delete User", "🗑️");

        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);

        viewButton.addActionListener(e -> viewUserDetails());
        deleteButton.addActionListener(e -> deleteSelectedUser());

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        try {
            // Load events
            List<Event> events = eventController.getAllEvents();
            eventTableModel.setRowCount(0);
            for (Event event : events) {
                eventTableModel.addRow(new Object[] {
                        event.getEventId(),
                        event.getEventName(),
                        event.getDate(),
                        event.getTime(),
                        event.getVenueId(),
                        event.getDescription(),
                        event.getEventType(),
                        event.getTicketPrice()
                });
            }

            // Load tickets
            loadTicketData();

            // Load users
            List<User> users = userController.getAllUsers();
            userTableModel.setRowCount(0);
            for (User user : users) {
                userTableModel.addRow(new Object[] {
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getUserType()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTicketData() {
        try {
            List<Ticket> tickets = ticketController.getAllTickets();
            ticketTableModel.setRowCount(0);

            for (Ticket ticket : tickets) {
                Event event = eventController.getEventById(ticket.getEventId());
                User customer = userController.getUserById(ticket.getUserId());

                String eventName = event != null ? event.getEventName() : "Unknown Event";
                String customerName = customer != null ? customer.getName() : "Unknown Customer";

                Object[] rowData = {
                        ticket.getTicketId(),
                        eventName,
                        customerName,
                        ticket.getSeatNumber(),
                        ticket.getPrice(),
                        ticket.getTicketType()
                };
                ticketTableModel.addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading tickets: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddEventDialog() {
        JDialog dialog = new JDialog(this, "Add Event", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add form fields
        JTextField nameField = new JTextField(20);
        JTextField dateField = new JTextField(20);
        JTextField timeField = new JTextField(20);
        JTextField venueField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JComboBox<Event.EventType> typeCombo = new JComboBox<>(Event.EventType.values());

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Time (HH:mm):"), gbc);
        gbc.gridx = 1;
        formPanel.add(timeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Venue ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(venueField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeCombo, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                Event event = new Event();
                event.setEventName(nameField.getText());
                event.setDate(LocalDate.parse(dateField.getText()));
                event.setTime(LocalTime.parse(timeField.getText()));
                event.setVenueId(Integer.parseInt(venueField.getText()));
                event.setTicketPrice(new BigDecimal(priceField.getText()));
                event.setEventType((Event.EventType) typeCombo.getSelectedItem());

                eventController.createEvent(event);
                loadData();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error creating event: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void editSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to edit");
            return;
        }

        int eventId = (int) eventTableModel.getValueAt(selectedRow, 0);
        try {
            Event event = eventController.getEventById(eventId);
            if (event != null) {
                showEditEventDialog(event);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading event: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEditEventDialog(Event event) {
        JDialog dialog = new JDialog(this, "Edit Event", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add form fields
        JTextField nameField = new JTextField(event.getEventName(), 20);
        JTextField dateField = new JTextField(event.getDate().toString(), 20);
        JTextField timeField = new JTextField(event.getTime().toString(), 20);
        JTextField venueField = new JTextField(String.valueOf(event.getVenueId()), 20);
        JTextField priceField = new JTextField(event.getTicketPrice().toString(), 20);
        JComboBox<Event.EventType> typeCombo = new JComboBox<>(Event.EventType.values());
        typeCombo.setSelectedItem(event.getEventType());

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Time (HH:mm):"), gbc);
        gbc.gridx = 1;
        formPanel.add(timeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Venue ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(venueField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeCombo, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                event.setEventName(nameField.getText());
                event.setDate(LocalDate.parse(dateField.getText()));
                event.setTime(LocalTime.parse(timeField.getText()));
                event.setVenueId(Integer.parseInt(venueField.getText()));
                event.setTicketPrice(new BigDecimal(priceField.getText()));
                event.setEventType((Event.EventType) typeCombo.getSelectedItem());

                eventController.updateEvent(event);
                loadData();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Error updating event: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event to delete");
            return;
        }

        int eventId = (int) eventTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this event?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                eventController.deleteEvent(eventId);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting event: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewTicketDetails() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to view");
            return;
        }

        int ticketId = (int) ticketTableModel.getValueAt(selectedRow, 0);
        try {
            Ticket ticket = ticketController.getTicketById(ticketId);
            if (ticket != null) {
                showTicketDetailsDialog(ticket);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading ticket: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTicketDetailsDialog(Ticket ticket) {
        JDialog dialog = new JDialog(this, "Ticket Details", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        try {
            Event event = eventController.getEventById(ticket.getEventId());
            User user = userController.getUserById(ticket.getUserId());

            gbc.gridx = 0;
            gbc.gridy = 0;
            detailsPanel.add(new JLabel("Ticket ID:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(String.valueOf(ticket.getTicketId())), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            detailsPanel.add(new JLabel("Event:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(event.getEventName()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            detailsPanel.add(new JLabel("Customer:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(user.getName()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            detailsPanel.add(new JLabel("Seat Number:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(ticket.getSeatNumber()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            detailsPanel.add(new JLabel("Price:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(ticket.getPrice().toString()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            detailsPanel.add(new JLabel("Type:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(ticket.getTicketType().name()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 6;
            detailsPanel.add(new JLabel("Booking Date:"), gbc);
            gbc.gridx = 1;
            detailsPanel.add(new JLabel(ticket.getBookingDate().toString()), gbc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog,
                    "Error loading ticket details: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return;
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.add(closeButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void cancelTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket to cancel");
            return;
        }

        int ticketId = (int) ticketTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this ticket?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (ticketController.cancelTicket(ticketId)) {
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to cancel ticket",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error cancelling ticket: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewUserDetails() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to view");
            return;
        }

        int userId = (int) userTableModel.getValueAt(selectedRow, 0);
        try {
            User user = userController.getUserById(userId);
            if (user != null) {
                showUserDetailsDialog(user);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showUserDetailsDialog(User user) {
        JDialog dialog = new JDialog(this, "User Details", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(String.valueOf(user.getUserId())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(user.getName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(user.getEmail()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        detailsPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(user.getPhone()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        detailsPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(user.getUserType().name()), gbc);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.add(closeButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete");
            return;
        }

        int userId = (int) userTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this user?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userController.deleteUser(userId);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting user: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}