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
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import classes.Appointment;
import modules.AdjustedTime;
import modules.AuthUser;
import classes.Customer;
import modules.KeyIndex;
import modules.DatabaseConn;
import org.omg.CORBA.INTERNAL;

import javax.xml.crypto.Data;

/**
 * FXML Controller class
 *
 * @author Soap
 */
public class AppointmentsController implements Initializable {

    @FXML private Text addTE;
    @FXML private Text removeTE;
    @FXML private Text updateTE;

    @FXML private Text addTS;
    @FXML private Text removeTS;
    @FXML private Text updateTS;

    @FXML private TextField apptTitle;
    @FXML private TextArea apptDesc;
    @FXML private TextField startTime;
    @FXML private TextField endTime;
    @FXML private TextField startDate;

    @FXML private RadioButton am;
    @FXML private RadioButton pm;

    @FXML private RadioButton hp;
    @FXML private RadioButton ch;
    @FXML private RadioButton bna;
    @FXML private RadioButton tec;
    @FXML private RadioButton hjm;

    @FXML private RadioButton m15;
    @FXML private RadioButton m30;
    @FXML private RadioButton m45;

    ToggleGroup ampm = new ToggleGroup();
    ToggleGroup place = new ToggleGroup();
    ToggleGroup timeInc = new ToggleGroup();

    @FXML private TextField u_apptTitle;
    @FXML private TextArea u_apptDesc;
    @FXML private TextField u_startTime;
    @FXML private TextField u_endTime;
    @FXML private TextField u_startDate;

    @FXML private RadioButton u_am;
    @FXML private RadioButton u_pm;
    @FXML private RadioButton u_hp;
    @FXML private RadioButton u_ch;
    @FXML private RadioButton u_bna;
    @FXML private RadioButton u_tec;
    @FXML private RadioButton u_hjm;
    @FXML private RadioButton u_m15;
    @FXML private RadioButton u_m30;
    @FXML private RadioButton u_m45;

    ToggleGroup u_ampm = new ToggleGroup();
    ToggleGroup u_place = new ToggleGroup();
    ToggleGroup u_timeInc = new ToggleGroup();

    @FXML private TableView<Customer> tableView;
    @FXML private TableColumn<Customer, String> cName;

    @FXML private TableView<Appointment> removeView;
    @FXML private TableColumn<Appointment, String> rTitle;
    @FXML private TableColumn<Appointment, String> rStartTime;
    @FXML private TableColumn<Appointment, String> rDescription;
    @FXML private TableColumn<Appointment, String> rLocation;

    @FXML private TableView<Appointment> updateView;
    @FXML private TableColumn<Appointment, String> uTitle;
    @FXML private TableColumn<Appointment, String> uStartTime;
    @FXML private TableColumn<Appointment, String> uDescription;
    @FXML private TableColumn<Appointment, String> uLocation;

    ObservableList<Customer> customerNames = FXCollections.observableArrayList();
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public void setLocation(String loc) {
        if (loc.compareTo("Horizon Pediatrics") == 0) {
            u_hp.setSelected(true);
        } else if (loc.compareTo("Thomasville Eye Ctr.") == 0) {
            u_tec.setSelected(true);
        } else if(loc.compareTo("Concentra Health") == 0) {
            u_ch.setSelected(true);
        } else if(loc.compareTo("Healing Jouirney Massage") == 0) {
            u_hjm.setSelected(true);
        } else if(loc.compareTo("Bradenton Neurology Assoc.") == 0) {
            u_bna.setSelected(true);
        }
    }

    public void setTimeInc(int s, int e) {
        int total = e - s;

        if (total == 15) {
            u_m15.setSelected(true);
        } else if (total == 30) {
            u_m30.setSelected(true);
        } else if (total == 45) {
            u_m45.setSelected(true);
        }
    }

    @FXML public void loadAppt() {
        Customer c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            errorMsg("Please first select a customer!", 1);
            return;
        }
        Appointment a = updateView.getSelectionModel().getSelectedItem();
        if (a == null) {
            errorMsg("Please first select an appointment!", 1);
            return;
        }

        u_startDate.setText(a.getStartTime().split(" ")[0].replaceAll("-", "/"));
        u_apptTitle.setText(a.getTitle());
        u_apptDesc.setText(a.getDescription());

        String stime = a.getStartTime();
        String shour = (stime.split(" ")[1].split(":")[0]);
        String smin = (stime.split(" ")[1].split(":")[1]);
        int sinthour = Integer.parseInt(shour);
        int sintmin = Integer.parseInt(smin);

        u_startTime.setText(shour + ":" + smin);
        int totalS = (sinthour * 60) + sintmin;

        String etime = a.getEndTime();
        String ehour = (etime.split(" ")[1].split(":")[0]);
        String emin = (etime.split(" ")[1].split(":")[1]);
        int einthour = Integer.parseInt(ehour);
        int eintmin = Integer.parseInt(emin);

        u_endTime.setText(ehour + ":" + emin);
        int totalE = (einthour * 60) + eintmin;

        setTimeInc(totalS, totalE);
        String theLocation = a.getLocation();
        setLocation(theLocation);

        if (sinthour <= 12) {
            u_am.setSelected(true);
        } else {
            u_pm.setSelected(true);
        }

    }

    @FXML public void updateAppt() {
        /*
        first remove the original appt
         */
        Customer c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            errorMsg("Please first select a customer!", 1);
            return;
        }
        Appointment a = updateView.getSelectionModel().getSelectedItem();
        if (a == null) {
            errorMsg("Please first select an appointment!", 1);
            return;
        }
        DatabaseConn.fireServer("delete from appointment where userId = " + a.getAppointmentId() + ";", false);
        updateViews();

        // add the new appt
        String placeStr = "";
        String ampmStr, timeIncStr;
        int left, right;

        addTE.setVisible(false);
        removeTE.setVisible(false);
        updateTE.setVisible(false);

        addTS.setVisible(false);
        removeTS.setVisible(false);
        updateTS.setVisible(false);

        if (u_apptTitle.getText().isEmpty()) {
            errorMsg("Need Appointment Title", 1);
            return;
        }
        if (u_apptDesc.getText().isEmpty()) {
            errorMsg("Need Appointment Description", 1);
            return;
        }
        if (u_startTime.getText().isEmpty()) {
            errorMsg("Need Appointment Start Time", 1);
            return;
        }

        if (u_startTime.getText().contains(":")) {
            String spliter[] = u_startTime.getText().split(":");
            if (spliter.length == 2) {
                try {
                    left = Integer.parseInt(spliter[0]);
                    right = Integer.parseInt(spliter[1]);
                } catch (Exception e) {
                    errorMsg("Please use format as follows: *:** (e.g. 8:30 or 12:00)", 1);
                    return;
                }
            } else {
                errorMsg("Please use format as follows: *:** (e.g. 8:30)", 1);
                return;
            }
        } else {
            errorMsg("Please use format as follows: *:** (e.g. 8:30)", 1);
            return;
        }

        try {
            RadioButton selectedTime  = (RadioButton) u_ampm.getSelectedToggle();
            ampmStr = selectedTime.getText();
        } catch (Exception e) {
            errorMsg("Please select either AM or PM", 1);
            return;
        }
        try {
            RadioButton selectedPlace  = (RadioButton) u_place.getSelectedToggle();
            placeStr = selectedPlace.getText();
        } catch (Exception e) {
            errorMsg("Please select a location", 1);
            return;
        }
        try {
            RadioButton selectedTimeInc  = (RadioButton) u_timeInc.getSelectedToggle();
            timeIncStr = selectedTimeInc.getText();
        } catch (Exception e) {
            errorMsg("Please select a 15, 30 or 45 minute time length", 1);
            return;
        }

        if (Integer.parseInt(u_startTime.getText().split(":")[0]) < 8 && ampmStr.compareTo("AM") == 0) {
            errorMsg("Please follow business hours", 1);
            return;
        }

        if (Integer.parseInt(u_startTime.getText().split(":")[0]) > 5 && ampmStr.compareTo("PM") == 0) {
            errorMsg("Please follow business hours", 1);
            return;
        }

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        //Date date = new Date();
        //String dt = dateFormat.format(date);

        String newDate = u_startDate.getText();
        try {
            int year  = Integer.parseInt(newDate.split("/")[0]);
            int month = Integer.parseInt(newDate.split("/")[1]);
            int day   = Integer.parseInt(newDate.split("/")[2]);
        } catch (Exception e) {
            errorMsg("Format like so: ****/**/** (e.g. 2019/11/30)", 0);
            return;
        }

        String sTime = newDate + " " + switchampmToMilitary(u_startTime.getText() + ":00");
        String eTime = newDate + " " + addToTime(sTime.split(" ")[1], timeIncStr.split(" ")[0]);

        if (ampmStr.compareToIgnoreCase("PM") == 0) {
            sTime = sTime.split(" ")[0] + " " + (Integer.parseInt(sTime.split(" ")[1].split(":")[0]) + 12) + ":" + sTime.split(" ")[1].split(":")[1] + ":00";
            eTime = eTime.split(" ")[0] + " " + (Integer.parseInt(eTime.split(" ")[1].split(":")[0]) + 12) + ":" + eTime.split(" ")[1].split(":")[1] + ":00";
        }

        if (apptOverlap(sTime, eTime)) {
            errorMsg("The appointments are overlapping", 1);
            return;
        }

        KeyIndex.setApptKey(KeyIndex.getApptKey()+1);
        String q = "insert into appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values ("+c.getAddressId()+", "+ KeyIndex.getApptKey() +", '"+u_apptTitle.getText()+"', '"+u_apptDesc.getText()+"', '"+placeStr+"', 'contactArea', 'typeArea', 'urlArea', '"+sTime+"', '"+eTime+"', CURRENT_TIMESTAMP, '"+AuthUser.getUser()+"', CURRENT_TIMESTAMP, '"+AuthUser.getUser()+"')";

        DatabaseConn.fireServer(q, true);

        updateViews();

        workedMsg("The appointment was added!", 1);
    }

    @FXML public void updateViews() {
        Customer c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            errorMsg("Please first select a customer!", 0);
            errorMsg("Please first select a customer!", 1);
            return;
        } else {
            errorMsg("", 0);
            errorMsg("", 1);
        }
        appointments.clear();
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

                //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                // time zone id saved with appt
                String timeZoneId = rs.getString("contact");

                Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(appt.getStartTime());
                Date date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(appt.getEndTime());

                Calendar calendar = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();

                calendar.setTime(date1);
                calendar2.setTime(date2);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                sdf.setTimeZone(TimeZone.getTimeZone(timeZoneId));
                sdf2.setTimeZone(TimeZone.getTimeZone(timeZoneId));

                appt.setStartTime(sdf.format(calendar.getTime()));
                appt.setEndTime(sdf2.format(calendar2.getTime()));

                if (appt.getCustomerId() == c.getAddressId()) {
                    appointments.add(appt);
                }
            }
            line.close();
            removeView.setItems(appointments);
            updateView.setItems(appointments);
        } catch (SQLException | ParseException e) {

        }
    }

    public void errorMsg(String msg, int c) {
        if (c == 0) {
            addTE.setVisible(true);
            addTE.setText(msg);
        } else if (c == 1) {
            removeTE.setVisible(true);
            removeTE.setText(msg);
        } else if (c == 2) {
            updateTE.setVisible(true);
            updateTE.setText(msg);
        }
    }

    public void workedMsg(String msg, int c) {
        if (c == 0) {
            addTS.setVisible(true);
            addTS.setText(msg);
        } else if (c == 1) {
            removeTS.setVisible(true);
            removeTS.setText(msg);
        } else if (c == 2) {
            updateTS.setVisible(true);
            updateTS.setText(msg);
        }
    }

    public String addToTime(String orgTime, String toAdd) {
        int newHourTime;
        int newMinTime;

        int hourTime = Integer.parseInt(orgTime.split(":")[0]);
        int minTime = Integer.parseInt(orgTime.split(":")[1]);
        int toAddTime = Integer.parseInt(toAdd);

        if (minTime + toAddTime >= 60) {
            newHourTime = hourTime + 1;
            newMinTime = (toAddTime + minTime) - 60;
        } else {
            newHourTime = hourTime;
            newMinTime = minTime + toAddTime;
        }

        return newHourTime + ":" + String.format("%02d", newMinTime) + ":00";
    }

    public String switchampmToMilitary(String ampm) {
        // e.g. 8:30:AM
        // taking strings like **:**:AM / **:**:PM
        String fixed = "";
        int temp = -1;
        String parse[] = ampm.split(":");

        if (parse[2].compareTo("PM") == 0) {
            temp = 12+Integer.parseInt(parse[0]);
        }
        if (temp > 0) {
            fixed += temp + ":" + parse[1] + ":" + parse[2];
        } else {
            fixed += parse[0] + ":" + parse[1] + ":" + parse[2];
        }

        return fixed;
    }

    /*
    calculate the (hours*60 + mins)
     */
    public int getNumberTime(String timestamp) {
        int hours = Integer.parseInt(timestamp.split(":")[0]);
        int mins  = Integer.parseInt(timestamp.split(":")[1]);

        return (hours*60 + mins);
    }

    public boolean apptOverlap(String sTime, String eTime) {
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from appointment;";
            ResultSet rs = line.executeQuery(q);
            while (rs.next()) {
                String apptDate = rs.getString("start").split(" ")[0];

                String apptStartTime = rs.getString("start").split(" ")[1];
                String apptEndTime = rs.getString("end").split(" ")[1];

                int sTimeCnt = getNumberTime(apptStartTime);
                int eTimeCnt = getNumberTime(apptEndTime);

                int sCheckTimeCnt = getNumberTime(sTime.split(" ")[1]);
                int eCheckTimeCnt = getNumberTime(eTime.split(" ")[1]);

                String checkDate = sTime.split(" ")[0];
                if (apptDate.compareTo(checkDate.replaceAll("/", "-")) == 0) { // the dates are the same
                    // starting in the middle of an appt
                    if (sCheckTimeCnt >= sTimeCnt && eCheckTimeCnt <= eTimeCnt) { // the start time of the new appt is in between the start/end of a different appt
                        return true;
                    }
                    // appt going in a parallel time
                    // such as 1:30 - 2:00   (a 30 min)
                    //      with 1:15 - 2:00 (a 45 min)
                    int midTime = (sCheckTimeCnt + eCheckTimeCnt) / 2;
                    if (midTime >= sTimeCnt && midTime <= eTimeCnt) {
                        return true;
                    }
                }
            }
            line.close();
        } catch (SQLException e) {

        }
        return false;
    }

    @FXML public void addAppt() {
        Customer c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            errorMsg("Please first select a customer!", 0);
            return;
        }

        String placeStr = "";
        String ampmStr, timeIncStr;
        int left, right;

        addTE.setVisible(false);
        removeTE.setVisible(false);
        updateTE.setVisible(false);

        addTS.setVisible(false);
        removeTS.setVisible(false);
        updateTS.setVisible(false);

        if (apptTitle.getText().isEmpty()) {
            errorMsg("Need Appointment Title", 0);
            return;
        }
        if (apptDesc.getText().isEmpty()) {
            errorMsg("Need Appointment Description", 0);
            return;
        }
        if (startTime.getText().isEmpty()) {
            errorMsg("Need Appointment Start Time", 0);
            return;
        }

        if (startTime.getText().contains(":")) {
            String spliter[] = startTime.getText().split(":");
            if (spliter.length == 2) {
                try {
                    left = Integer.parseInt(spliter[0]);
                    right = Integer.parseInt(spliter[1]);
                } catch (Exception e) {
                    errorMsg("Format like so: *:** (e.g. 8:30 or 12:00)", 0);
                    return;
                }
            } else {
                errorMsg("Format like so: *:** (e.g. 8:30)", 0);
                return;
            }
        } else {
            errorMsg("Format like so: *:** (e.g. 8:30)", 0);
            return;
        }

        try {
            RadioButton selectedTime  = (RadioButton) ampm.getSelectedToggle();
            ampmStr = selectedTime.getText();
        } catch (Exception e) {
            errorMsg("Please select either AM or PM", 0);
            return;
        }
        try {
            RadioButton selectedPlace  = (RadioButton) place.getSelectedToggle();
            placeStr = selectedPlace.getText();
        } catch (Exception e) {
            errorMsg("Please select a location", 0);
            return;
        }
        try {
            RadioButton selectedTimeInc  = (RadioButton) timeInc.getSelectedToggle();
            timeIncStr = selectedTimeInc.getText();
        } catch (Exception e) {
            errorMsg("Please select a 15, 30 or 45 minute time length", 0);
            return;
        }

        if (Integer.parseInt(startTime.getText().split(":")[0]) < 8 && ampmStr.compareTo("AM") == 0) {
            errorMsg("Please follow business hours", 0);
            return;
        }

        if (Integer.parseInt(startTime.getText().split(":")[0]) > 5 && ampmStr.compareTo("PM") == 0) {
            errorMsg("Please follow business hours", 0);
            return;
        }

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        //Date date = new Date();
        //String dt = dateFormat.format(date);

        String newDate = startDate.getText();
        try {
            int year  = Integer.parseInt(newDate.split("/")[0]);
            int month = Integer.parseInt(newDate.split("/")[1]);
            int day   = Integer.parseInt(newDate.split("/")[2]);
        } catch (Exception e) {
            errorMsg("Format like so: ****/**/** (e.g. 2019/11/30)", 0);
            return;
        }

        String sTime = newDate + " " + switchampmToMilitary(startTime.getText() + ":00");
        String eTime = newDate + " " + addToTime(sTime.split(" ")[1], timeIncStr.split(" ")[0]);

        if (ampmStr.compareToIgnoreCase("PM") == 0) {
            sTime = sTime.split(" ")[0] + " " + (Integer.parseInt(sTime.split(" ")[1].split(":")[0]) + 12) + ":" + sTime.split(" ")[1].split(":")[1] + ":00";
            eTime = eTime.split(" ")[0] + " " + (Integer.parseInt(eTime.split(" ")[1].split(":")[0]) + 12) + ":" + eTime.split(" ")[1].split(":")[1] + ":00";
        }

        if (apptOverlap(sTime, eTime)) {
            errorMsg("The appointments are overlapping", 0);
            return;
        }

        TimeZone tz = Calendar.getInstance().getTimeZone();
        String contactTZ = tz.getID(); // get the time zone of the appointment

        /*
        AdjustedTime aj = () -> {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getDefault());
            String adjustedTime = df.format(date);

            return adjustedTime;
        };

        String adjustedTime = aj.getAdjustedTime();
         */

        KeyIndex.setApptKey(KeyIndex.getApptKey()+1);
        String q = "insert into appointment(customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values ("+c.getAddressId()+", "+ KeyIndex.getApptKey() +", '"+apptTitle.getText()+"', '"+apptDesc.getText()+"', '"+placeStr+"', '"+contactTZ+"', 'typeArea', 'urlArea', '"+sTime+"', '"+eTime+"', CURRENT_TIMESTAMP, '"+AuthUser.getUser()+"', CURRENT_TIMESTAMP, '"+AuthUser.getUser()+"')";

        DatabaseConn.fireServer(q, true);

        updateViews();

        workedMsg("The appointment was added!", 0);
    }

    @FXML public void removeAppt() {
        Customer c = tableView.getSelectionModel().getSelectedItem();
        if (c == null) {
            errorMsg("Please first select a customer!", 1);
            return;
        }
        Appointment a = removeView.getSelectionModel().getSelectedItem();
        if (a == null) {
            errorMsg("Please first select an appointment!", 1);
            return;
        }
        DatabaseConn.fireServer("delete from appointment where userId = " + a.getAppointmentId() + ";", false);
        updateViews();
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

    public void loadCustomerData() {
        Customer c = tableView.getSelectionModel().getSelectedItem();
        if (c != null) {
            try {
                Statement line = DatabaseConn.getConnection().createStatement();
                String q = "select * from address where cityId = "+ c.getAddressId() +";";
                ResultSet rs = line.executeQuery(q);
                if (rs.next()) {
                    String zipData = rs.getString("postalCode");
                    String phoneData = rs.getString("phone");
                    String addressData = rs.getString("address");
                    c.setZip(zipData);
                    c.setPhone(phoneData);
                    c.setAddress(addressData);
                }
                line.close();

                Statement line1 = DatabaseConn.getConnection().createStatement();
                String q1 = "select * from country where countryId = "+ c.getAddressId() +";";
                ResultSet rs1 = line1.executeQuery(q1);
                if (rs1.next()) {
                    String countryData = rs1.getString("country");
                    c.setCountry(countryData);
                }
                line.close();
            } catch (SQLException e) {
                System.out.println("Error loading data");
            }
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));

        rTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        rStartTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startTime"));
        rDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        rLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));

        uTitle.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        uStartTime.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startTime"));
        uDescription.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        uLocation.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));

        am.setToggleGroup(ampm);
        pm.setToggleGroup(ampm);

        hp.setToggleGroup(place);
        ch.setToggleGroup(place);
        bna.setToggleGroup(place);
        tec.setToggleGroup(place);
        hjm.setToggleGroup(place);

        m15.setToggleGroup(timeInc);
        m30.setToggleGroup(timeInc);
        m45.setToggleGroup(timeInc);

        u_am.setToggleGroup(u_ampm);
        u_pm.setToggleGroup(u_ampm);

        u_hp.setToggleGroup(u_place);
        u_ch.setToggleGroup(u_place);
        u_bna.setToggleGroup(u_place);
        u_tec.setToggleGroup(u_place);
        u_hjm.setToggleGroup(u_place);

        u_m15.setToggleGroup(u_timeInc);
        u_m30.setToggleGroup(u_timeInc);
        u_m45.setToggleGroup(u_timeInc);

        timeInc.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                RadioButton rb = (RadioButton) timeInc.getSelectedToggle();
                if (rb != null) {
                    try {
                        //endTime.setText( addToTime(startTime.getText(), rb.getText().split(" ")[0]) );
                        String newETime = addToTime(startTime.getText(), rb.getText().split(" ")[0]);
                        endTime.setText(newETime.split(":")[0] + ":" + newETime.split(":")[1]);
                    } catch (Exception e) {
                        endTime.setText("Invalid start time!");
                    }
                }
            }
        });

        u_timeInc.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                RadioButton rb = (RadioButton) u_timeInc.getSelectedToggle();
                if (rb != null) {
                    try {
                        String newETime = addToTime(u_startTime.getText(), rb.getText().split(" ")[0]);
                        u_endTime.setText(newETime.split(":")[0] + ":" + newETime.split(":")[1]);
                    } catch (Exception e) {
                        u_endTime.setText("Invalid start time!");
                    }
                }
            }
        });

    }

}
