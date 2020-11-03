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
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import classes.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modules.AdjustedTime;
import modules.DatabaseConn;
import modules.CalendarSegment;

/**
 * FXML Controller class
 *
 * @author Soap
 */
public class CalendarController implements Initializable {

    @FXML private GridPane gp;
    @FXML private GridPane gpweek;

    @FXML private TextField dateZone;
    @FXML private TextField dateZone1;
    @FXML private TextField weekZone;

    @FXML private Text monthText;
    @FXML private Text yearText;

    private int calNum = 2;
    private int calYear = 2019;
    private int calWeek = 1;

    public void loadWeek() {
        boolean worked = false;
        try {
            String dz = weekZone.getText();
            calWeek = Integer.parseInt(dz);
            if (calWeek > 5) {
                weekZone.setText("");
                weekZone.setPromptText("Must choose 1-5");
                return;
            }
            if (calWeek < 0) {
                int bad = 1/0;
            }
            drawGrid();
            weekZone.setText("");
            weekZone.setPromptText("e.g. 1");
            worked = true;
            if (worked) {
                //yearText.setText(Integer.toString(calWeek));
            }
        } catch (Exception e) {
            weekZone.setText("");
            weekZone.setPromptText("Invalid Format");
        }
    }

    public void loadYear() {
        boolean worked = false;
        try {
            String dz = dateZone1.getText();
            calYear = Integer.parseInt(dz);
            if (calYear < 0) {
                int bad = 1/0;
            }
            drawGrid();
            dateZone1.setText("");
            dateZone1.setPromptText("e.g. 2019");
            worked = true;
            if (worked) {
                yearText.setText(Integer.toString(calYear));
            }
        } catch (Exception e) {
            dateZone1.setText("");
            dateZone1.setPromptText("Invalid Format");
        }
    }

    public void loadMonth() {
        boolean worked = false;
        try {
            String dz = dateZone.getText();
            calNum = Integer.parseInt(dz);
            if (calNum < 0 || calNum > 12) {
                int bad = 1/0;
            }
            drawGrid();
            dateZone.setText("");
            dateZone.setPromptText("e.g. 02");
            worked = true;
            if (worked) {
                monthText.setText(Integer.toString(calNum));
            }
        } catch (Exception e) {
            dateZone.setText("");
            dateZone.setPromptText("Invalid Format");
        }
    }

    ArrayList<CalendarSegment> dates = new ArrayList<CalendarSegment>();

    public String getData(int dayNumber) {
        for (CalendarSegment temp : dates) {
            if (Integer.parseInt(temp.getSegmentDate()) == dayNumber) {
                return temp.getSegmentData();
            }
        }
        return "";
    }

    // avoid the potential lag of searching database more than a needed amount of times
    public void populateDatesDatas() {
        dates.clear();
        try {
            Statement line = DatabaseConn.getConnection().createStatement();
            String q = "select * from appointment;";
            ResultSet rs = line.executeQuery(q);
            while (rs.next()) {
                String apptDate = rs.getString("start").split(" ")[0];
                if (Integer.parseInt(apptDate.split("-")[1]) == calNum && Integer.parseInt(apptDate.split("-")[0]) == calYear) {
                    CalendarSegment cs = new CalendarSegment(rs.getString("title"), apptDate.split("-")[2]);
                    dates.add(cs);
                }
            }
            line.close();
        } catch (SQLException e) {

        }
    }

    public void drawGrid() {

        populateDatesDatas();

        YearMonth yearMonthObject = YearMonth.of(calYear, calNum);
        int days = yearMonthObject.lengthOfMonth();

        gp.getChildren().clear();
        gpweek.getChildren().clear();

        int dayNumber = 1;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 7; x++) {
                int weekIndex = calWeek - 1;

                Label txt1 = new Label(Integer.toString(dayNumber));
                Label txt2 = new Label("");
                txt2.setVisible(false);

                GridPane.setHalignment(txt1, HPos.LEFT);
                GridPane.setValignment(txt1, VPos.TOP);
                txt1.setStyle("-fx-border-color: black; -fx-font-size: 15;");

                GridPane.setHalignment(txt2, HPos.CENTER);
                GridPane.setValignment(txt2, VPos.CENTER);
                txt2.setStyle("-fx-border-color: black; -fx-font-size: 12;");
                txt2.setWrapText(true);

                gp.add(txt1, x, y);
                gp.add(txt2, x, y);

                String dateData = getData(dayNumber);
                if (dateData.compareTo("") != 0) {
                    txt2.setText(dateData);
                    txt2.setVisible(true);
                } else {
                    txt2.setVisible(false);
                }

                if (dayNumber >= days) {
                    return;
                } else {
                    dayNumber++;
                }

                if (y == weekIndex) {
                    Label txt3 = new Label(Integer.toString(dayNumber));
                    txt3.setStyle("-fx-border-color: black; -fx-font-size: 15;");
                    GridPane.setHalignment(txt3, HPos.LEFT);
                    GridPane.setValignment(txt3, VPos.TOP);
                    Label txt4 = new Label("");
                    txt4.setStyle("-fx-border-color: black; -fx-font-size: 15;");
                    GridPane.setHalignment(txt4, HPos.CENTER);
                    GridPane.setValignment(txt4, VPos.CENTER);
                    gpweek.add(txt3, x, 0);
                    gpweek.add(txt4, x, 0);

                    if (dateData.compareTo("") != 0) {
                        txt4.setText(dateData);
                        txt4.setVisible(true);
                    } else {
                        txt4.setVisible(false);
                    }
                }

            }
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
        drawGrid();
    }
    
}
