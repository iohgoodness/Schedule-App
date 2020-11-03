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
import java.util.ResourceBundle;

import classes.Appointment;
import classes.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modules.CalendarSegment;
import modules.DatabaseConn;

/**
 * FXML Controller class
 *
 * @author Soap
 */
public class ReportsController implements Initializable {

    @FXML private Text m15;
    @FXML private Text m30;
    @FXML private Text m45;

    @FXML private TextField dateZone;
    @FXML private TextField dateZone1;

    private int calNum = 2;
    private int calYear = 2019;

    @FXML private Text customers;
    @FXML private Text appointments;

    @FXML private TableView<Appointment> rView;
    @FXML private TableColumn<Appointment, String> rTitle;
    @FXML private TableColumn<Appointment, String> rStartTime;
    @FXML private TableColumn<Appointment, String> rDescription;
    @FXML private TableColumn<Appointment, String> rLocation;

    @FXML private TableView<Customer> tableView;
    @FXML private TableColumn<Customer, String> cName;

    ObservableList<Appointment> apptsList = FXCollections.observableArrayList();
    ObservableList<Customer> customerNames = FXCollections.observableArrayList();

    @FXML public void updateViews() {
        Customer c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            return;
        }
        apptsList.clear();
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from appointment;";
            ResultSet rs = line.executeQuery(q);
            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setCustomerId(rs.getInt("customerId"));
                appt.setTitle(rs.getString("title"));
                appt.setLocation(rs.getString("location"));
                appt.setDescription(rs.getString("description"));
                appt.setStartTime(rs.getString("start"));
                appt.setEndTime(rs.getString("end"));
                appt.setAppointmentId(rs.getInt("userId"));

                if (appt.getCustomerId() == c.getAddressId()) {
                    apptsList.add(appt);
                }
            }
            line.close();
            //removeView.setItems(appointments);
            rView.setItems(apptsList);
        } catch (SQLException e) {

        }
    }

    public void updateExsistingCustomers() {
        customerNames.clear();
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from customer;";
            ResultSet rs = line.executeQuery(q);
            while (rs.next()) {
                String cName = rs.getString("customerName");
                int cId = rs.getInt("addressId");
                Customer x = new Customer();
                x.setName(cName);
                x.setAddressId(cId);
                if (x.getName().compareTo("Sample") != 0) {
                    customerNames.add(x);
                }
            }
            line.close();
            tableView.setItems(customerNames);
        } catch (SQLException e) {

        }
    }

    public void getAppointmentTypes() {
        int c15 = 0;
        int c30 = 0;
        int c45 = 0;
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from appointment;";
            ResultSet rs = line.executeQuery(q);
            while (rs.next()) {
                String apptDate = rs.getString("start").split(" ")[0];
                try {
                    calNum = Integer.parseInt(dateZone.getText());
                    calYear = Integer.parseInt(dateZone1.getText());
                } catch (Exception e) {
                    return;
                }

                if (Integer.parseInt(apptDate.split("-")[1]) == calNum && Integer.parseInt(apptDate.split("-")[0]) == calYear) {
                    String start = rs.getString("start");
                    String end = rs.getString("end");
                    int startT = Integer.parseInt(start.split(" ")[1].split(":")[0])*60 + Integer.parseInt(start.split(" ")[1].split(":")[1]);
                    int endT = Integer.parseInt(end.split(" ")[1].split(":")[0])*60 + Integer.parseInt(end.split(" ")[1].split(":")[1]);

                    if (endT - startT == 15) {
                        c15++;
                    } else if (endT - startT == 30) {
                        c30++;
                    } else if (endT - startT == 45) {
                        c45++;
                    }
                }
            }
            line.close();
        } catch (SQLException e) {

        }

        m15.setText("15 minute appts: " + c15);
        m30.setText("30 minute appts: " + c30);
        m45.setText("45 minute appts: " + c45);

    }

    public boolean loadYear() {
        boolean worked = false;
        try {
            String dz = dateZone1.getText();
            calYear = Integer.parseInt(dz);
            if (calYear < 0) {
                int bad = 1/0;
            }
            dateZone1.setText("");
            dateZone1.setPromptText("e.g. 2019");
            worked = true;
            if (worked) {
                return true;
            }
        } catch (Exception e) {
            dateZone1.setText("");
            dateZone1.setPromptText("Invalid Format");
        }
        return false;
    }

    public boolean loadMonth() {
        boolean worked = false;
        try {
            String dz = dateZone.getText();
            calNum = Integer.parseInt(dz);
            if (calNum < 0 || calNum > 12) {
                int bad = 1/0;
            }
            dateZone.setText("");
            dateZone.setPromptText("e.g. 02");
            worked = true;
            if (worked) {
                return true;
            }
        } catch (Exception e) {
            dateZone.setText("");
            dateZone.setPromptText("Invalid Format");
        }
        return false;
    }

    public void reportTotalNumbers() {
        int totalAppts     = 0;
        int totalCustomers = 0;
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from appointment;";
            ResultSet rs = line.executeQuery(q);
            while (rs.next()) {
                totalAppts++;
            }
            line.close();
        } catch (SQLException e) {

        }

        try {
            Statement line1 = DatabaseConn.getConnection().createStatement();
            String q1 = "select * from customer;";
            ResultSet rs1 = line1.executeQuery(q1);
            while (rs1.next()) {
                totalCustomers++;
            }
            line1.close();
        } catch (SQLException e) {

        }

        customers.setText("Customers: " + Integer.toString(totalCustomers-1));
        appointments.setText("Appointments: " + totalAppts);
    }

    public void reportAppointmentTypes() {
        boolean x = loadMonth();
        boolean y = loadYear();

        if (x && y) {
            getAppointmentTypes();
        }
    }
    @FXML public void returnToMainMenu (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Menu.fxml"));
        Parent menuViewParent = (Parent) loader.load();

        Scene menuViewScene = new Scene(menuViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menuViewScene);
        window.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));

        rTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        rStartTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startTime"));
        rDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        rLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
    }    
    
}
