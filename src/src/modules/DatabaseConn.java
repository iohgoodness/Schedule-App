/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Soap
 */
public class DatabaseConn {
    
    private static final String driver = "com.mysql.jdbc.Driver"; 
    
    private static final String dbName = "U06kpq";
    private static final String url = "jdbc:mysql://3.227.166.251/" + dbName + "?allowMultiQueries=true";
    private static final String username = "U06kpq";
    private static final String password = "53688793707";
    
    private static Connection newConn;
    
    public DatabaseConn() {}

    public static void fireServer(String q, boolean keyChecks) {
        if (keyChecks) {q = "SET FOREIGN_KEY_CHECKS = 0;" + q + ";SET FOREIGN_KEY_CHECKS = 1;" ;}
        try {
            Statement l1 = DatabaseConn.getConnection().createStatement();
            l1.execute(q);
            l1.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public static void makeConn() {
        try{
            newConn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Established");
        } catch (Exception e) {
            System.out.println("Error connecting to database!");
        }
    }

    public static void breakConn() {
        try {
            newConn.close();
            System.out.println("Connection Disestablished");
        } catch (Exception e) {
            System.out.println("Error disconnecting from database!");
        }
    }
    
    public static Connection getConnection() {
        return newConn;
    }
    
}
