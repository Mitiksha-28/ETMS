package com.etms;

import com.etms.ui.LoginFrame;
import javax.swing.SwingUtilities;

public class ETMSApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel to system default
                javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName());

                // Create and show login frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setLocationRelativeTo(null); // Center on screen
                loginFrame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}