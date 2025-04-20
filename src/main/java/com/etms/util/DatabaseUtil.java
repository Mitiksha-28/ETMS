package com.etms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final String URL = "jdbc:mysql://localhost:3306/etms";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mysql@mitiksha";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load MySQL JDBC Driver", e);
            throw new RuntimeException("Failed to load MySQL JDBC Driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            logger.error("Failed to get database connection", e);
            throw e;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Failed to close database connection", e);
            }
        }
    }
}