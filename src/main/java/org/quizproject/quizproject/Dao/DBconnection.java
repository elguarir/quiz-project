package org.quizproject.quizproject.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBconnection {
    private Connection con;
    private Statement s;

    private static final String URL = "jdbc:mysql://localhost:3306/quizGame";

    public DBconnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, "root", "");
            System.out.println("Connection established");
        } catch (Exception e) {
            System.out.println("Problem with JDBC or XAMPP: " + e.getMessage());
        }
    }

    public Statement getStatement() {
        try {
            s = con.createStatement();
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        return s;
    }

    public Connection getCon() {
        return con;
    }


}
