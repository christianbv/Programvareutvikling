package JavaFX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainPackage.Main;

import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminCommunityController implements Initializable {

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
    public TableView<AdminCommunity> communityTable;
    @FXML
    public TableColumn<AdminCommunity, String> nameCol;
    @FXML
    public TableColumn<AdminCommunity, String> descriptionCol;

    public AdminCommunityController() {
        // TODO Auto-generated constructor stub
    }

    public String getError() {
        String good = "Alt fungerer normalt...";
        return good;

    }

    @Override
    public void initialize(URL foo, ResourceBundle arg) {
        nameCol.setCellValueFactory(p -> p.getValue().nameProperty());
        descriptionCol.setCellValueFactory(p -> p.getValue().descriptionProperty());

        communityTable.getItems().setAll(getCommunities());

        communityTable.setEditable(true);
        communityTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // denne fungerer ikke av en eller annen rar grunn

        nameBoks.setText("Hei Admin ");
        pathBoks.setText("../Menu/History");


        // denne kjøres før javafx vises!
        statusBoks.setText("Alt fungerer som normalt..");
    }

    public ObservableList<AdminCommunity> getCommunities() {
        ObservableList<AdminCommunity> communities = FXCollections.observableArrayList();
        List<Document> foo = Main.getAdminCommunity();
        for (Document doc : foo) {
            communities.add(new AdminCommunity(doc.get("Navn på community").toString(), doc.get("Beskrivelse").toString()));
        }
        return communities;
    }

    @FXML private void acceptCommunity(ActionEvent event) throws IOException{
        AdminCommunity comm = communityTable.getSelectionModel().getSelectedItem();
        String commName = comm.getName();
        Main.acceptCommunity(commName);
        System.out.println("Godtok community!");
        communityTable.getItems().remove(communityTable.getSelectionModel().getSelectedItem());
    }

    @FXML private void declineCommunity(ActionEvent event) throws IOException{
        AdminCommunity comm = communityTable.getSelectionModel().getSelectedItem();
        String commName = comm.getName();
        Main.deleteCommunity(commName);
        System.out.println("Avslo community!");
        communityTable.getItems().remove(communityTable.getSelectionModel().getSelectedItem());
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