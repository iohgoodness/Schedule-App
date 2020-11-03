/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import classes.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modules.DatabaseConn;
import modules.AdjustedTime;

/**
 * FXML Controller class
 *
 * @author Soap
 */
public class MenuController implements Initializable {

    @FXML public Text warningMsg;

    public boolean checkNearAppt() {
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from appointment;";
            ResultSet rs = line.executeQuery(q);
            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setStartTime(rs.getString("start"));

                AdjustedTime aj = () -> {
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    df.setTimeZone(TimeZone.getDefault());
                    String adjustedTime = df.format(date);

                    return adjustedTime;
                };

                String adjustedTime = aj.getAdjustedTime();

                int apptStartTime = Integer.parseInt(rs.getString("start").split(" ")[1].split(":")[0])*60 + Integer.parseInt(rs.getString("start").split(" ")[1].split(":")[1]);
                int currTime = Integer.parseInt(adjustedTime.split(" ")[1].split(":")[0])*60 + Integer.parseInt(adjustedTime.split(" ")[1].split(":")[1]);
                if (apptStartTime - currTime < 15 && apptStartTime - currTime > 0) {
                    return true;
                }
            }
            line.close();
        } catch (SQLException e) {

        }
        return false;
    }

    public void setWarningMsg() {
        boolean isAppt = checkNearAppt();

        if (isAppt) {
            warningMsg.setText("Appointment in the next 15 mintues!");
        } else {
            warningMsg.setText("No soon appointments.");
        }
    }
    
    @FXML public void changeCustomerRecords(ActionEvent event) throws IOException {        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CustomerRecords.fxml"));
        Parent menuViewParent = (Parent) loader.load();

        Scene menuViewScene = new Scene(menuViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuViewScene);
        window.show();
    }
    
    @FXML public void viewCalendar(ActionEvent event) throws IOException {        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Calendar.fxml"));
        Parent menuViewParent = (Parent) loader.load();

        Scene menuViewScene = new Scene(menuViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuViewScene);
        window.show();
    }
    
    @FXML public void viewAppointments(ActionEvent event) throws IOException {        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Appointments.fxml"));
        Parent menuViewParent = (Parent) loader.load();

        Scene menuViewScene = new Scene(menuViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuViewScene);
        window.show();
    }
    
    @FXML public void generateReports(ActionEvent event) throws IOException {        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Reports.fxml"));
        Parent menuViewParent = (Parent) loader.load();

        Scene menuViewScene = new Scene(menuViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuViewScene);
        window.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setWarningMsg();
    }
    
}
