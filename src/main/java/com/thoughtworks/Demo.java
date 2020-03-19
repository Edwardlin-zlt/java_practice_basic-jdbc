package com.thoughtworks;

import com.sun.org.apache.bcel.internal.generic.DMUL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo {

    public static final String URL = "jdbc:mysql://localhost:3306/student_examination_sys";
    public static final String USER = "root";
    public static final String PASSWORD = "password";
    private static Connection conn = null;
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL,  USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        return conn;
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection();
        String sql = "update students set name = \"Edward\" where id = 1";
        Statement statement = connection.createStatement();
        int execute = statement.executeUpdate(sql);
        System.out.println(execute);
    }
}