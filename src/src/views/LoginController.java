/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import modules.AdjustedTime;
import modules.AuthUser;

/**
 * FXML Controller class
 *
 * @author Soap
 */
public class LoginController implements Initializable {
    @FXML private Label LoginLabel;
    @FXML private Label UsernameLabel;
    @FXML private Label PasswordLabel;
    
    @FXML private Label UsernameErrorLabel;
    @FXML private Label PasswordErrorLabel;
    @FXML private Label InputErrorLabel;
    @FXML private Label InputErrorLabel1;
    
    @FXML private TextField Username;
    @FXML private TextField Password;
    
    public void fixLang() {
        Locale lang = Locale.getDefault();
        
        // spanish and french are the two other working languages.
        //Locale lang = new Locale("es", "ES");
        //Locale lang = new Locale("fr", "FR");
        
        try {
            ResourceBundle rb = ResourceBundle.getBundle("langs/" + lang);
            LoginLabel.setText(rb.getString("Login"));
            UsernameLabel.setText(rb.getString("Username"));
            PasswordLabel.setText(rb.getString("Password"));
            UsernameErrorLabel.setText(rb.getString("ErrorText"));
            PasswordErrorLabel.setText(rb.getString("ErrorText"));
            InputErrorLabel.setText(rb.getString("SameErrorText"));
            InputErrorLabel1.setText(rb.getString("BadUserPass"));
        }
        catch (Exception e) {
            // there is no en, EN properties file for english, so just use the default english rb
        }
        
    }
    public void createLog() throws IOException {

        AdjustedTime aj = () -> {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getDefault());
            String adjustedTime = df.format(date);

            return adjustedTime;
        };

        String adjustedTime = aj.getAdjustedTime();

        File f = new File("logs.txt");
        if (f.exists() && !f.isDirectory()) {
            PrintWriter pw = new PrintWriter(new FileOutputStream(new File("logs.txt"),true));
            pw.append("test user logged in at " + adjustedTime + "\n");
            pw.close();
        } else {
            PrintWriter pw = new PrintWriter("logs.txt", "UTF-8");
            pw.println("test user logged in at " + adjustedTime);
            pw.close();
        }
    }
    
    @FXML public void SubmitBtn(ActionEvent event) throws IOException {
        String user = Username.getText();
        String pass = Password.getText();
        
        UsernameErrorLabel.setVisible(false);
        PasswordErrorLabel.setVisible(false);
        InputErrorLabel.setVisible(false);
        InputErrorLabel1.setVisible(false);
        
        if (user.compareTo("") == 0) {
            UsernameErrorLabel.setVisible(true);
            return;
        }
        if (pass.compareTo("") == 0) {
            PasswordErrorLabel.setVisible(true);
            return;
        }
        if (user.compareTo(pass) != 0) {
            InputErrorLabel.setVisible(true);
            return;
        }

        int returnValue = AuthUser.login(user, pass);
        if (returnValue == 1) {

            createLog();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Menu.fxml"));
            Parent menuViewParent = (Parent) loader.load();

            Scene menuViewScene = new Scene(menuViewParent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(menuViewScene);
            window.show();
        } else {
            System.out.println("failed!");
            InputErrorLabel1.setVisible(true);
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fixLang();
    }    
    
}
