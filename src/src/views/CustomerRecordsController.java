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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modules.*;

import classes.Customer;

/**
 * FXML Controller class
 *
 * @author Soap
 */
public class CustomerRecordsController implements Initializable {
    
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField country;
    @FXML private TextField zip;
    @FXML private TextField phoneNumber;
    
    @FXML private TextField name1;
    @FXML private TextField address1;
    @FXML private TextField country1;
    @FXML private TextField zip1;
    @FXML private TextField phoneNumber1;

    @FXML private Text addCe;
    @FXML private Text addCs;
    @FXML private Text updateCe;
    @FXML private Text updateCs;

    private Customer loadedCustomer = null;

    @FXML private TableView<Customer> tableView;
    @FXML private TableColumn<Customer, String> cName;
    
    ObservableList<Customer> customerNames = FXCollections.observableArrayList();

    /*
    First lambda expression, checks to see if a string is empty
    uses the .java file, StringChecker.java
     */
    public static boolean sEmpty(String myString) {
        StringChecker sc = (text) -> {
            if (myString.compareTo("") == 0) {
                return false;
            } else {
                return true;
            }
        };
        return sc.check(myString);
    }

    public void errorMsg(String msg, int c) {
        if (c == 0) {
            addCe.setVisible(true);
            addCe.setText(msg);
        } else if (c == 1) {
            updateCe.setVisible(true);
            updateCe.setText(msg);
        }
    }

    public void workedMsg(String msg, int c) {
        if (c == 0) {
            addCs.setVisible(true);
            addCs.setText(msg);
        } else if (c == 1) {
            updateCs.setVisible(true);
            updateCs.setText(msg);
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
    
    public void deleteCustomer() {
        Customer c = tableView.getSelectionModel().getSelectedItem();
        if (c != null) {
            DatabaseConn.fireServer("delete from customer where addressId = " + c.getAddressId() + ";", false);
            DatabaseConn.fireServer("delete from country where countryId = " + c.getAddressId() + ";", false);
            DatabaseConn.fireServer("delete from address where cityId = " + c.getAddressId() + ";", false);

            // delete the appointments linked to the customer
            DatabaseConn.fireServer("delete from appointment where customerId = " + c.getAddressId() + ";", false);
        }
        
        updateExsistingCustomers();
    }
    
    public void loadCustomerData() {
        Customer c = tableView.getSelectionModel().getSelectedItem();
        loadedCustomer = tableView.getSelectionModel().getSelectedItem();

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
                    //System.out.println(countryData);
                    c.setCountry(countryData);
                }
                line.close();
            } catch (SQLException e) {
                System.out.println("Error loading data");
            }
        
        name1.setText(c.getName());
        address1.setText(c.getAddress());
        zip1.setText(c.getZip());
        phoneNumber1.setText(c.getPhone());
        country1.setText(c.getCountry());
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
                String cName1 = rs.getString("customerName");
                int cId = rs.getInt("addressId");
                Customer x = new Customer();
                x.setName(cName);
                x.setName(cName1);
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
    
    public void modifyNewCustomer() {

        if (loadedCustomer == null) {
            errorMsg("Must select a customer to update", 1);
            return;
        }

        String nameV = name1.getText();
        String addressV = address1.getText();
        String countryV = country1.getText();
        String zipV = zip1.getText();
        String phoneNumberV = phoneNumber1.getText();

        updateCe.setVisible(false);
        updateCs.setVisible(false);

        if (!sEmpty(nameV)) {
            errorMsg("Customer needs to have a name!", 1);
            return;
        }
        if (!sEmpty(addressV)) {
            errorMsg("Customer needs to have an address!", 1);
            return;
        }
        if (!sEmpty(countryV)) {
            errorMsg("Customer needs to have a country!", 1);
            return;
        }
        if (!sEmpty(zipV)) {
            errorMsg("Customer needs to have a zip code!", 1);
            return;
        }
        if (!sEmpty(phoneNumberV)) {
            errorMsg("Customer needs to have a phone number!", 1);
            return;
        }

        KeyIndex.setKey(KeyIndex.getKey()+1);

        DatabaseConn.fireServer("delete from customer where addressId = " + loadedCustomer.getAddressId() + ";", false);
        DatabaseConn.fireServer("delete from country where countryId = " + loadedCustomer.getAddressId() + ";", false);
        DatabaseConn.fireServer("delete from address where cityId = " + loadedCustomer.getAddressId() + ";", false);
        DatabaseConn.fireServer("delete from appointment where customerId = " + loadedCustomer.getAddressId() + ";", false);

        String q = "insert into country(country, createDate, createdBy, lastUpdate, lastUpdateBy) values ('" + countryV + "', CURRENT_TIMESTAMP, '" + AuthUser.getUser() + "', CURRENT_TIMESTAMP, '" + AuthUser.getUser() + "')";
        DatabaseConn.fireServer(q, true);

        String q1 = "insert into address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values ('" + addressV + "', '" + addressV + "', " + KeyIndex.getKey() + ", '" + zipV + "', '" + phoneNumberV + "', CURRENT_TIMESTAMP, '"+AuthUser.getUser()+"', CURRENT_TIMESTAMP, '"+AuthUser.getUser()+"')";
        DatabaseConn.fireServer(q1, true);

        String q2 = "insert into customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values ('" + nameV + "', " + KeyIndex.getKey() + ", 1, CURRENT_TIMESTAMP, '" + AuthUser.getUser() + "', CURRENT_TIMESTAMP, '" + AuthUser.getUser() + "')";
        DatabaseConn.fireServer(q2, true);

        name1.setText("");
        address1.setText("");
        country1.setText("");
        zip1.setText("");
        phoneNumber1.setText("");

        workedMsg(nameV + " was updated in the database!", 1);

        updateExsistingCustomers();
    }
    
    public void addNewCustomer() {
        String nameV = name.getText();
        String addressV = address.getText();
        String countryV = country.getText();
        String zipV = zip.getText();
        String phoneNumberV = phoneNumber.getText();

        addCe.setVisible(false);
        addCs.setVisible(false);

        System.out.println(!sEmpty(nameV));
        if (!sEmpty(nameV)) {
            errorMsg("Customer needs to have a name!", 0);
            return;
        }
        if (!sEmpty(addressV)) {
            errorMsg("Customer needs to have an address!", 0);
            return;
        }
        if (!sEmpty(countryV)) {
            errorMsg("Customer needs to have a country!", 0);
            return;
        }
        if (!sEmpty(zipV)) {
            errorMsg("Customer needs to have a zip code!", 0);
            return;
        }
        if (!sEmpty(phoneNumberV)) {
            errorMsg("Customer needs to have a phone number!", 0);
            return;
        }
        
        KeyIndex.setKey(KeyIndex.getKey()+1);

        String q = "insert into country(country, createDate, createdBy, lastUpdate, lastUpdateBy) values ('" + countryV + "', CURRENT_TIMESTAMP, '" + AuthUser.getUser() + "', CURRENT_TIMESTAMP, '" + AuthUser.getUser() + "')";
        DatabaseConn.fireServer(q, true);

        String q1 = "insert into address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values ('" + addressV + "', '" + addressV + "', " + KeyIndex.getKey() + ", '" + zipV + "', '" + phoneNumberV + "', CURRENT_TIMESTAMP, '"+AuthUser.getUser()+"', CURRENT_TIMESTAMP, '"+AuthUser.getUser()+"')";
        DatabaseConn.fireServer(q1, true);

        String q2 = "insert into customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values ('" + nameV + "', " + KeyIndex.getKey() + ", 1, CURRENT_TIMESTAMP, '" + AuthUser.getUser() + "', CURRENT_TIMESTAMP, '" + AuthUser.getUser() + "')";
        DatabaseConn.fireServer(q2, true);

        name.setText("");
        address.setText("");
        country.setText("");
        zip.setText("");
        phoneNumber.setText("");

        workedMsg(nameV + " was added to the database!", 0);

        updateExsistingCustomers();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //lambda_code

        System.out.println(sEmpty("okay"));

        cName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
    }
    
}
