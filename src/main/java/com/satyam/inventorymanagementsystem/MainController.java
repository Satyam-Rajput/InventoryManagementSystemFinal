package com.satyam.inventorymanagementsystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.xssf.usermodel.*;

public class MainController implements Initializable {

    //Regexes to validate the price & quantity
    private static final String REGEX_PATTERN_INTEGER = "^\\d+$";
    private static final String REGEX_PATTERN_DOUBLE = "[+]?([0-9]*[.])?[0-9]+";

    public static DAO dao = null;

    //Variables related to medicine
    @FXML
    private TextField medicinePriceTxt;
    @FXML
    private Tab medicineTab;

    @FXML
    private TextField medicineQuantityTxt;
    @FXML
    private TextField medicineNameTxt;

    @FXML
    private TextField searchTxt;

    @FXML
    private TableView<Item> medicineTable;

    @FXML
    private TableColumn<Item, String> medicineNameCol;
    @FXML
    private TableColumn<Item, Integer> medicineQuantityCol;
    @FXML
    private TableColumn<Item, Double> medicinePriceCol;

    @FXML
    private TableColumn<Item, Double> medicineTotalCol;

    ObservableList<Item> medicineList = FXCollections.observableArrayList();

    //User related functions
    @FXML
    private Tab userTab;

    @FXML
    private TextField userNameTxt;

    @FXML
    private ChoiceBox<String> accessTypeTxt;

    @FXML
    private PasswordField passwordTxt;
    @FXML
    private TableColumn<User, String> usernameCol;
    @FXML
    private TableColumn<User, Integer> accessTypeCol;
    @FXML
    private TableColumn<User, Double> lastLoginCol;
    @FXML
    private TableView<User> userTable;
    ObservableList<String> accessTypeList = FXCollections.observableArrayList();
    ObservableList<User> userList = FXCollections.observableArrayList();

    //Patient related variables
    @FXML
    private TextField patientNameTxt;
    @FXML
    private DatePicker purchasedDateTxt;

    @FXML
    private ChoiceBox<String> medicineChoicesTxt;
    @FXML
    private TextField patientMedicineQuantityTxt;
    ObservableList<String> availableMedicineList = FXCollections.observableArrayList();

    ObservableSet<Medicine> purchasedMedicineList = FXCollections.observableSet();

    @FXML
    private TableView<Medicine> patientMedicineTableList;

    @FXML
    private TableColumn<Medicine, String> patientMedicineNameCol;

    @FXML
    private TableColumn<Medicine, Integer> patientMedicineQuantityCol;

    //Variable for file uploader
    @FXML
    private ListView files;

    @FXML
    private TabPane tableTabPane;
    @FXML
    private TabPane userTabPane;

    @FXML
    private TableColumn<Patient, String> patientNameCol;
    @FXML
    private TableColumn<Patient, String> purchasedDateCol;

    @FXML
    private TableColumn<Patient, Double> patientTotalAmountCol;

    @FXML
    private TableView<Patient> patientDetailTable;

    ObservableList<Patient> patientDetailList = FXCollections.observableArrayList();
    
    
    @FXML
    private DatePicker deleteDateTxt;
    
    @FXML
    private Button deleteBtn;

    public void initializeMedicineTableView() {
        medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        medicineQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        medicinePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        medicineTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public void initializePatientMedicineTableView() {
        patientMedicineNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientMedicineQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void initializePatientTableView() {
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        purchasedDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        patientTotalAmountCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public void initializeAccessTypeChoiceBox() {
        accessTypeList.add("Admin");
        accessTypeList.add("Normal");
        accessTypeTxt.setItems(accessTypeList);
        accessTypeTxt.getSelectionModel().selectFirst();
    }

    public void initializeUserTableView() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        accessTypeCol.setCellValueFactory(new PropertyValueFactory<>("accessType"));
        lastLoginCol.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
    }

    public void initializeAllList() {
        try {
            dao = new DAO();
            medicineList = FXCollections.observableArrayList(dao.getAllItems());
            userList = FXCollections.observableArrayList(dao.getAllUsers());
            availableMedicineList = FXCollections.observableArrayList(dao.getMedicineNames());
            patientDetailList = FXCollections.observableArrayList(dao.getAllPatients());

            this.initializeAllTables();
        } catch (Exception e) {
            
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Cannot connect to Database");
            
            try {
                this.logout();
            } catch (Exception ex) {

            }
        }
    }

    public void initializeAllTables() {

        medicineChoicesTxt.setItems(availableMedicineList);
        medicineChoicesTxt.getSelectionModel().selectFirst();
        medicineTable.setItems(medicineList);
        userTable.setItems(userList);
        patientDetailTable.setItems(patientDetailList);
    }

    public void initializeMedicineFields(Item item) {
        medicineNameTxt.setText(item.getName());
        medicinePriceTxt.setText(String.valueOf(item.getPrice()));
        medicineQuantityTxt.setText(String.valueOf(item.getQuantity()));
    }

    public void initializePatientFields(Patient patient) {
        patientNameTxt.setText(patient.getName());
        purchasedDateTxt.setValue(LocalDate.parse(String.valueOf(patient.getDate())));
        patientMedicineTableList.setItems(FXCollections.observableArrayList(patient.getList()));
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.initializeMedicineTableView();
        this.initializePatientMedicineTableView();
        this.initializePatientTableView();
        this.initializeAccessTypeChoiceBox();

        this.initializeUserTableView();

        purchasedDateTxt.setValue(LocalDate.now());
        deleteDateTxt.setValue(LocalDate.now());
        this.initializeAllList();
        userTable.setItems(userList);

        if (User.user.getAccessType().equals("Normal")) {
            
            tableTabPane.getTabs().remove(2);
            userTabPane.getTabs().remove(0);
            userTabPane.getTabs().remove(1);
            deleteBtn.setDisable(true);
        }

    }

    @FXML
    private void logout() throws IOException {

        App.stage.setHeight(200.0);
        App.stage.setWidth(250.0);
        App.stage.centerOnScreen();
        App.setRoot("login");
    }

    @FXML
    public void close() {
        App.stage.close();
    }

    @FXML
    public void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About us");
        alert.setHeaderText("This software is designed to manage medicine stock");
        alert.showAndWait();
    }

    @FXML
    public void search() {

        try {
            Item item = dao.getItem(searchTxt.getText().toUpperCase());
            if (item == null) {
                throw new Exception();
            } else {
                this.initializeMedicineFields(item);
            }
        } catch (Exception e) {
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Record not found");
        }

    }

    @FXML
    public void updateMed() {

        if (!(medicineNameTxt.getText().equals("") || medicineQuantityTxt.getText().equals("")
                || medicinePriceTxt.getText().equals("")) && medicinePriceTxt.getText().matches(REGEX_PATTERN_DOUBLE)
                && medicineQuantityTxt.getText().matches(REGEX_PATTERN_INTEGER)) {
//            medicineList.add();
            try {
                Item item = new Item();
                item.setName(medicineNameTxt.getText());
                item.setPrice(Double.parseDouble(medicinePriceTxt.getText()));
                item.setQuantity(Integer.parseInt(medicineQuantityTxt.getText()));

                dao.addNewItem(item);
                this.initializeAllList();
            } catch (Exception e) {
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to update record");

            }

            medicineTable.setItems(medicineList);
            ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Record updated successfully");

        } else {
            if (!medicinePriceTxt.getText().matches(REGEX_PATTERN_DOUBLE)) {
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Price must be a positive number");

            } else if (!medicineQuantityTxt.getText().matches(REGEX_PATTERN_INTEGER)) {
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Quantity must be a positive number");

            } else {
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Some required details are missing");

            }
        }

    }

    @FXML
    public void updateUser() {

        if (!(userNameTxt.getText().equals("") || passwordTxt.getText().equals(""))) {
            //userList.add(new User(userNameTxt.getText(), accessTypeTxt.getSelectionModel().getSelectedItem(), passwordTxt.getText(), ""));

            try {
                User user = new User();
                user.setUsername(userNameTxt.getText());
                user.setPassword(user.encode(passwordTxt.getText()));
                user.setAccessType(accessTypeTxt.getSelectionModel().getSelectedItem());

                dao.addNewUser(user);
                userList = FXCollections.observableArrayList(dao.getAllUsers());
            } catch (Exception e) {
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to update record");

            }

            userTable.setItems(userList);
            ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Record updated successfully");

        } else {
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Some required details are missing");

        }
    }

    @FXML
    public void exportMedicineList() throws FileNotFoundException, IOException {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
            Date date = new Date();

            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("Meds");

            XSSFRow header = sheet.createRow(0);
            XSSFCell nameHeader = header.createCell(0);
            XSSFCell quantityHeader = header.createCell(1);
            XSSFCell priceHeader = header.createCell(2);
            XSSFCell totalHeader = header.createCell(3);
            nameHeader.setCellValue("Name");
            quantityHeader.setCellValue("Quantity");
            priceHeader.setCellValue("Price");
            totalHeader.setCellValue("Total");

            for (int i = 0; i < medicineList.size(); i++) {
                XSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(medicineList.get(i).getName());
                row.createCell(1).setCellValue(medicineList.get(i).getQuantity());
                row.createCell(2).setCellValue(medicineList.get(i).getPrice());

                row.createCell(3).setCellFormula("PRODUCT(B" + (i + 2) + ",C" + (i + 2) + ")");

            }
            String path = System.getProperty("user.home")+"\\Downloads\\MedicineList_" + formatter.format(date) + ".xlsx";
            FileOutputStream outputStream = new FileOutputStream(new File(path));
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

            ShowAlert.showAlert(Alert.AlertType.INFORMATION, "File saved to " + path);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    public void tableRowClicked(Event event) {
        Item item = medicineTable.getSelectionModel().getSelectedItem();
        this.initializeMedicineFields(item);

    }

    @FXML
    public void uploadFile() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("/"));
        fc.getExtensionFilters().addAll(new ExtensionFilter("XLSX Files", "*.xlsx"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {

            if (ShowAlert.showConfirmationAlert("Do you want to upload " + selectedFile.getName())) {

                try {
                    FileInputStream inputStream = new FileInputStream(selectedFile.getAbsoluteFile());
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    Iterator iterator = sheet.iterator();
                    if (iterator.hasNext()) {
                        iterator.next();
                    }
                    while (iterator.hasNext()) {
                        XSSFRow row = (XSSFRow) iterator.next();
                        Iterator cellIterator = row.cellIterator();
                        Cell name = (Cell) cellIterator.next();
                        Cell quantity = (Cell) cellIterator.next();
                        Cell price = (Cell) cellIterator.next();

                        name.getCellType();
                        if (!String.valueOf(name.getCellType()).equals("STRING")) {
                            ShowAlert.showAlert(Alert.AlertType.ERROR, " Invalid name found at row:" + (name.getRowIndex() + 1));

                            continue;
                        }
                        if (!String.valueOf(quantity.getCellType()).equals("NUMERIC")) {
                            ShowAlert.showAlert(Alert.AlertType.ERROR, "Invalid quantity found at row:" + (quantity.getRowIndex() + 1));

                            continue;
                        }
                        if (!String.valueOf(price.getCellType()).equals("NUMERIC")) {
                            ShowAlert.showAlert(Alert.AlertType.ERROR, "Invalid price found at row:" + (price.getRowIndex() + 1));

                            continue;
                        }

                        try {
                            Item item = new Item();
                            item.setName(name.getStringCellValue());
                            item.setPrice(price.getNumericCellValue());
                            item.setQuantity((int) quantity.getNumericCellValue());
                            dao.addNewItem(item);
                        } catch (Exception e) {
                            ShowAlert.showAlert(Alert.AlertType.ERROR, "Failed to add row:" + (price.getRowIndex() + 1));

                        }

                    }
                    this.initializeAllList();
                       ShowAlert.showAlert(Alert.AlertType.INFORMATION, "File uploaded successfully");
                } catch (Exception e) {
                    ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to upload file");

                }
            }

        } else {
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to upload file");
        }

    }

    @FXML
    public void AddPatientMedicineToList() {
        if (!patientMedicineQuantityTxt.getText().matches(REGEX_PATTERN_INTEGER)) {
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Quantity must be positive number");

        } else {
            Medicine m = new Medicine();
            m.setName(medicineChoicesTxt.getSelectionModel().getSelectedItem());
            m.setQuantity(Integer.parseInt(patientMedicineQuantityTxt.getText()));
            int  quantity=0;
            try {
               quantity=dao.getQuantity(m.getName());
                if ( quantity< m.getQuantity()) {

                    throw new ArithmeticException();
                }
                m.setPrice(dao.getPrice(m.getName()));
                purchasedMedicineList.add(m);
                patientMedicineTableList.setItems(FXCollections.observableArrayList(purchasedMedicineList));

            } catch (ArithmeticException ex) {
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Quantity Should be less than "+quantity);

            } catch (Exception e) {
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to add medicine");

            }
        }

    }

    @FXML
    public void addPatient() {
        if (!(patientNameTxt.getText().equals("") || purchasedMedicineList.size() < 1)) {
            LocalDate date = purchasedDateTxt.getValue();
            String currDate = String.valueOf(date);
            System.out.println(patientNameTxt.getText() + " " + currDate);

            Patient patient = new Patient();
            patient.setName(patientNameTxt.getText());
            patient.setDate(java.sql.Date.valueOf(currDate));
            patient.setList(new ArrayList<>(purchasedMedicineList));
            double total = 0;
            for (Medicine m : purchasedMedicineList) {

                total += m.calculateTotal();

            }
            patient.setTotal(total);

            try {
                if (dao.addPatient(patient)) {

                    try {
                        for (Medicine m : purchasedMedicineList) {
                            Item i = dao.getItem(m.getName());
                            i.setQuantity(i.getQuantity() - m.getQuantity());
                            dao.addNewItem(i);
                            // System.out.println(dao.setQuantity(m.getName(), dao.getQuantity(m.getName())-m.getQuantity()));
                        }
                    } catch (Exception e) {
                        ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to update record");

                    }
                }
            } catch (Exception e) {
                ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to add patient");

            }

            this.initializeAllList();
            this.resetPatientData();

            ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Record updated successfully");
        } else {

            ShowAlert.showAlert(Alert.AlertType.ERROR, "Some required details are missing");
        }

    }

    @FXML
    public void patientTableRowClicked(Event event) {
        Patient patient = patientDetailTable.getSelectionModel().getSelectedItem();
        List<Patient> patientList = null;
        try {
            patientList = dao.getPatient(patient.getName(), String.valueOf(patient.getDate()));
            this.initializePatientFields(patientList.get(0));
        } catch (Exception e) {
            ShowAlert.showAlert(Alert.AlertType.ERROR,"Record not found");
            
        }

    }
    
    @FXML
    public void deletePatients()
    {
    
        if(ShowAlert.showConfirmationAlert("Do you want to delete patient data till "+deleteDateTxt.getValue()+" ?")){
        try{
        String date=String.valueOf(deleteDateTxt.getValue());
        if(dao.deletePatients(date)){
        patientDetailList = FXCollections.observableArrayList(dao.getAllPatients());
         patientDetailTable.setItems(patientDetailList);
            ShowAlert.showAlert(Alert.AlertType.INFORMATION, "Successfully deleted");
        
        
        }
        else
        { ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to delete data");
        }
        }
        catch(Exception e)
        {
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Failed to delete data");
        }}
    }

    @FXML
    public void resetPatientData() {

        patientNameTxt.setText("");
        purchasedDateTxt.setValue(LocalDate.now());
        purchasedMedicineList.clear();
        medicineChoicesTxt.getSelectionModel().selectFirst();
        medicineQuantityTxt.setText("");
        patientMedicineTableList.setItems(FXCollections.observableArrayList(purchasedMedicineList));

    }

}
