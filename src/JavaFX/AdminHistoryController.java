package JavaFX;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mainPackage.Main;
import javafx.scene.layout.HBox;

import org.bson.Document;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminHistoryController implements Initializable {

    private Main main;
    private static String path = "{null}";
    static String category = null;
    static String history = null;

    // øverst i applikasjonen
    @FXML
    TextField nameBoks;

    // nederst i applikasjonen
    @FXML
    TextField statusBoks;
    @FXML
    TextField pathBoks;


    // for denne kontrolleren:
    @FXML
    TextField searchField;
    @FXML
    public TableView<AdminHistory> historyTable;
    @FXML
    public TableColumn<AdminHistory, String> produktCol;
    @FXML
    public TableColumn<AdminHistory, String> datoCol;
    @FXML
    public TableColumn<AdminHistory, String> communityCol;

    @FXML public DatePicker fromDate;
    @FXML public DatePicker toDate;

    static Integer id_int = 1;

    public AdminHistoryController() {
        // TODO Auto-generated constructor stub
    }

    public String getError() {
        String good = "Alt fungerer normalt...";
        return good;

    }

    @Override
    public void initialize(URL foo, ResourceBundle arg) {
        produktCol.setCellValueFactory(p -> p.getValue().produktProperty());
        datoCol.setCellValueFactory(p -> p.getValue().datoProperty());
        communityCol.setCellValueFactory(p -> p.getValue().communityProperty());

        historyTable.getItems().setAll(getHistory());
        //historyTableId.getItems().setAll(getHistory());

        historyTable.setEditable(true);
        historyTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // denne fungerer ikke av en eller annen rar grunn

        nameBoks.setText("Hei Admin ");
        pathBoks.setText("../Menu/History");


        // denne kjøres før javafx vises!
        statusBoks.setText("Alt fungerer som normalt..");
    }

    // Gets all the items of current users
    public ObservableList<AdminHistory> getHistory() {
        ObservableList<AdminHistory> histories = FXCollections.observableArrayList();
        String search = searchField.getText();
        List<Document> foo = Main.getAdminHistory(search);
        for (Document doc : foo) {
            histories.add(new AdminHistory(doc.get("Produkt").toString(), doc.get("Dato").toString(), doc.get("Community").toString()));
            id_int++;
        }
        return histories;
    }

    public void searchHistory(ActionEvent event) throws SQLException {
        historyTable.getItems().setAll(getHistory());
    }

    public void historyOnDates(ActionEvent event) throws Exception{
        historyTable.getItems().setAll(getHistoryOnDates());
    }

    public ObservableList<AdminHistory> getHistoryOnDates() throws Exception{
        ObservableList<AdminHistory> histories = FXCollections.observableArrayList();
        String search = searchField.getText();
        LocalDate fromDato = fromDate.getValue();
        LocalDate toDato = toDate.getValue();
        try {
            List<Document> foo = Main.getAdminHistoryOnDate(search, fromDato, toDato);
            for (Document doc : foo) {
                histories.add(new AdminHistory(doc.get("Produkt").toString(), doc.get("Dato").toString(), doc.get("Community").toString()));
                id_int++;
            }
        }
        catch (Exception e){
            e.getCause();
            System.out.println(e.getCause());
        }
        return histories;
    }

    public void showStatistics(ActionEvent event) throws Exception{
        if (searchField.getText().isEmpty() | fromDate == null | toDate == null){
            statusBoks.setText("Du må filtrere på dato og community!");
        }
        Stage stage = new Stage();
            stage.setTitle("Statistikk for valgt dato");
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final BarChart<String, Number> bc =
                    new BarChart<String, Number>(xAxis,yAxis);
            bc.setTitle("Statistikk for " + searchField.getText());
            xAxis.setLabel("Produkt");
            xAxis.setTickLabelRotation(90);
            yAxis.setLabel("Antall transaksjoner");

            ObservableList<AdminHistory> histories = getHistoryOnDates();
            System.out.println(histories);
            HashMap<String, Integer> counter = new HashMap<String, Integer>();

            histories.forEach((AdminHistory) ->{
                String productName = AdminHistory.getProdukt();
                if (counter.containsKey(productName)){
                    counter.put(productName,counter.get(productName)+1);
                }
                else{
                    counter.put(productName, 1);
                }
            });
            System.out.println(counter);


            XYChart.Series series1 = new XYChart.Series();
            counter.forEach((key, value) -> {
                Integer intObj = new Integer(value);
                Number numObj = (Number)intObj;
                series1.getData().add(new XYChart.Data(key, numObj));
                    });

            Scene scene  = new Scene(bc,800,600);
            bc.getData().addAll(series1);
            stage.setScene(scene);
            stage.show();
    }

    @FXML
    private void toAdminMenu(ActionEvent event) throws IOException {
        Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }


    @FXML
    private void toAdminCategory(ActionEvent event) throws IOException{
        Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }

    @FXML
    private void toAdminCommunity(ActionEvent event) throws IOException {
        Parent menyParent = FXMLLoader.load(getClass().getResource("AdminCommunity.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }

    @FXML
    private void toAdminUser(ActionEvent event) throws IOException{
        Parent menyParent = FXMLLoader.load(getClass().getResource("NewAdminUsers.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }

    @FXML
    private void toAdminHistory(ActionEvent event) throws IOException {
        Parent menyParent = FXMLLoader.load(getClass().getResource("NewAdminHistory.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }

    @FXML
    private void toLogin(ActionEvent event) throws IOException {
        Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }
}