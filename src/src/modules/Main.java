/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Soap
 */
public class Main extends Application {

    private void fixingKey(String tableName, String columnName) {
        int maxKey = -1;
        // needs to have at least one record previously in
        // the database to locate the PK (so start init value of PK)
        // at -1, from the fact that the query will find a greater PK
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from "+tableName+";";
            ResultSet rs = line.executeQuery(q);
            while (rs.next()) {
                int x = rs.getInt(columnName);
                if (x > maxKey) {
                    maxKey = x;
                }
            }
            line.close();
            if (tableName.equalsIgnoreCase("appointment")) {
                KeyIndex.setApptKey(maxKey);
            } else {
                KeyIndex.setKey(maxKey);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseConn.makeConn();

        fixingKey("country", "countryId");
        fixingKey("appointment", "userId");

        Parent root = FXMLLoader.load(getClass().getResource("../views/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // when app closes, so does SQL connection
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e){
                DatabaseConn.breakConn();
            }
        });
    }
}