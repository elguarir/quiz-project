package org.quizproject.quizproject.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    private static DBconnection instance;
    private Connection connection;
    //hosted db :
    private static final String URL = "jdbc:mysql://51.83.68.93:3306/quizproject";
    private static final String USER = "moha";
    private static final String PASSWORD = "123";

    //el garuir credentials :
//    private static final String URL = "jdbc:mysql://localhost:3306/quizproject";
//    private static final String USER = "root";
//    private static final String PASSWORD = "moha";

    //el aassal credentials :
    // private static final String URL = "jdbc:mysql://localhost:3306/quizgame";
    // private static final String USER = "root";
    // private static final String PASSWORD = "";

    private DBconnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static synchronized DBconnection getInstance() {
        if (instance == null) {
            instance = new DBconnection();
        }
        return instance;
    }

    public Connection getCon() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database connection", e);
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    //main method to test the db connectivity :
    public static void main(String[] args) {
        System.out.println("Testing database connectivity...");

        // Get an instance of DBconnection
        DBconnection dbConnection = DBconnection.getInstance();

        try (Connection connection = dbConnection.getCon()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection established successfully!");
            } else {
                System.out.println("Failed to establish database connection.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            dbConnection.closeConnection();
        }
    }
}
