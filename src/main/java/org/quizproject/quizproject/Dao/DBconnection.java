package org.quizproject.quizproject.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

import static javafx.application.Application.launch;

public class DBconnection {
    private Connection con;
    private Statement s;

   // private static final String URL = "jdbc:mysql://localhost:3306/quizproject";
    private static final String URL = "jdbc:mysql://localhost:3306/quizGame";


    public DBconnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
           // con = DriverManager.getConnection(URL, "root", "moha");
            con = DriverManager.getConnection(URL, "root", "");
            System.out.println("Connection established");
        } catch (Exception e) {
            System.out.println("Problem with JDBC or XAMPP: " + e.getMessage());
        }
    }

    public Statement getStatement() {
        try {
            if (con == null || con.isClosed()) {
                throw new SQLException("Database connection is not established");
            }
            s = con.createStatement();
        } catch (SQLException e) {
            System.err.println("Error creating statement: " + e.getMessage());
            throw new RuntimeException("Failed to create database statement", e);
        }
        return s;
    }

    public void closeResources() {
        try {
            if (s != null) {
                s.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    public Connection getCon() {
        return con;
    }

    public static void main(String [] args){
        launch();
    }
}
