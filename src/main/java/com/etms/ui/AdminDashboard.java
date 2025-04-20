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

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

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

    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model
        String[] columns = { "ID", "Name", "Date", "Time", "Venue", "Capacity", "Available Tickets", "Price" };
        eventTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        eventTable = new JTable(eventTableModel);
        JScrollPane scrollPane = new JScrollPane(eventTable);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Event");
        JButton editButton = new JButton("Edit Event");
        JButton deleteButton = new JButton("Delete Event");

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

        // Create table model
        String[] columns = { "ID", "Event", "Customer", "Quantity", "Total Price", "Purchase Date" };
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
        JButton cancelButton = new JButton("Cancel Ticket");

        buttonPanel.add(viewButton);
        buttonPanel.add(cancelButton);

        // Add action listeners
        viewButton.addActionListener(e -> viewTicketDetails());
        cancelButton.addActionListener(e -> cancelSelectedTicket());

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table model
        String[] columns = { "ID", "Name", "Email", "User Type" };
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        userTable = new JTable(userTableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewButton = new JButton("View User");
        JButton deleteButton = new JButton("Delete User");

        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);

        // Add action listeners
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
            List<Ticket> tickets = ticketController.getAllTickets();
            ticketTableModel.setRowCount(0);
            for (Ticket ticket : tickets) {
                ticketTableModel.addRow(new Object[] {
                        ticket.getTicketId(),
                        ticket.getEventId(),
                        ticket.getUserId(),
                        ticket.getSeatNumber(),
                        ticket.getPrice(),
                        ticket.getTicketType(),
                        ticket.getBookingDate()
                });
            }

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

    private void cancelSelectedTicket() {
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