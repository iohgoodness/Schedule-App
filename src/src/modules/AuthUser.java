/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author Soap
 */
public class AuthUser {
    private static String user;
    
    public static String getUser() {
        return user;
    }
    
    public static int login(String username, String password) {
        int returnValue = 0;
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from user where userName='" + username + "' and password='" + password + "'";
            ResultSet r = line.executeQuery(q);
            if (r.next()) {
                user = r.getString("userName");
                line.close();
                returnValue = 1;
            } else {
                System.out.println("Failed to make query");
            }
        } catch (Exception e) {
            
        }
        return returnValue;
    }
}
