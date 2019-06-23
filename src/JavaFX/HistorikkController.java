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
import javafx.scene.control.*;
import javafx.stage.Stage;
import mainPackage.Main;
import javafx.scene.layout.HBox;

import org.bson.Document;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HistorikkController implements Initializable {

    private Main main;
    private static String path = "{null}";
    static String category = null;
    static String history = null;

    // øverst i applikasjonen
    @FXML TextField nameBoks;

    // nederst i applikasjonen
    @FXML TextField statusBoks;
    @FXML TextField pathBoks;


    // for denne kontrolleren:
    @FXML TextField searchHistory;
    @FXML public TableView<History> historyTableId;
    @FXML public TableColumn<History,String> dateCol;
    @FXML public TableColumn<History,String> nameCol;
    @FXML public TableColumn<History,String> lastnameCol;
    @FXML public TableColumn<History,String> ratingCol;
    @FXML public TableColumn<History,String> shareCol;
    @FXML public TableColumn<History,String> communityCol;

    private static List<Document> usersHistories;
    static Integer id_int = 1;

    public HistorikkController() {
        // TODO Auto-generated constructor stub
    }

    public String getError() {
        String good = "Alt fungerer normalt...";
        return good;

    }

    @Override
    public void initialize(URL foo, ResourceBundle arg) {
        dateCol.setCellValueFactory(p->p.getValue().dateProperty());
        nameCol.setCellValueFactory(p->p.getValue().nameProperty());
        ratingCol.setCellValueFactory(p->p.getValue().ratingProperty());
        shareCol.setCellValueFactory(p->p.getValue().shareProperty());
        //communityCol.setCellValueFactory(p->p.getValue().communityProperty());
        lastnameCol.setCellValueFactory(p->p.getValue().lastnameProperty());

        historyTableId.getItems().setAll(getHistory());
        //historyTableId.getItems().setAll(getHistory());

        historyTableId.setEditable(true);
        historyTableId.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // denne fungerer ikke av en eller annen rar grunn

        nameBoks.setText("Hei "+Main.profile.getFirstName());
        pathBoks.setText("../Menu/History");


        // denne kjøres før javafx vises!
        statusBoks.setText("Alt fungerer som normalt..");
    }

    // Gets all the items of current users
    public ObservableList<History> getHistory() {
        ObservableList<History> histories = FXCollections.observableArrayList();
        String search = searchHistory.getText();
        List<Document> foo = Main.getUserHistory(Main.profileId, search);
        for(Document doc:foo) {
            histories.add(new History(doc.get("Dato").toString(),doc.get("Navn").toString(),doc.get("Etternavn").toString(),doc.get("+/-").toString(),doc.get("Bytte").toString(),doc.get("Community").toString()));
            id_int++;
        }
        return histories;
    }

    public void searchHistory(ActionEvent event)throws SQLException {
        historyTableId.getItems().setAll(getHistory());
    }
    // Change-stage-methods:



	// Change-stage methods:
	    
    public void toLogin(ActionEvent event) throws IOException {
    	Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }
    
    
    
    public void toGiveShare(ActionEvent event) throws IOException {
        Parent kategoriParent = FXMLLoader.load(getClass().getResource("NewRequests.fxml"));
        Scene kategoriScene = new Scene(kategoriParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(kategoriScene);
        window.show();
    }
    
    @FXML
    private void toTakeShare(ActionEvent event) throws IOException {
        Parent kategoriParent = FXMLLoader.load(getClass().getResource("NewNearbyProducts.fxml"));
        Scene kategoriScene = new Scene(kategoriParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(kategoriScene);
        window.show();
    }
    
    @FXML
    private void toActiveShares(ActionEvent event) throws IOException {
        Parent kategoriParent = FXMLLoader.load(getClass().getResource("NewMyRequests.fxml"));
        Scene kategoriScene = new Scene(kategoriParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(kategoriScene);
        window.show();
    }
    
    public void toProducts(ActionEvent event) throws  IOException {
    	
  		String produkter = "../JavaFX/NewProducts.fxml";
  		Parent produktParent = FXMLLoader.load(getClass().getResource(produkter));
  		Scene produktScene = new Scene(produktParent);
  		Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
  		window.setScene(produktScene);
  		
  		
  		
  		window.show();
  		//Tråden går hele veien hit før den flytter til productcontroller!
        
    }
	
    public void toSettings(ActionEvent event) throws IOException {
    	//changeCenter(event);
    	path += "/Settings";
    	
    	Parent innstillingParent = FXMLLoader.load(getClass().getResource("NewSettings.fxml"));
        Scene innstillingScene = new Scene(innstillingParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(innstillingScene);
        window.show();
        
        
        
    }
	
    @FXML 
    public void toMenu(ActionEvent event) throws IOException {
    	path = "Menu";
    	
    	Parent menyParent = FXMLLoader.load(getClass().getResource("NewMenu.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        
        try {
        	
        	//TableView<ItemTable> tab = (TableView<ItemTable>) menyScene.lookup("#tableId");
        	
        	
        	TextField rating = (TextField) menyScene.lookup("#ratingBoks");
        	rating.setText(String.valueOf(Main.profile.getRating()));
        	
        	TextField name = (TextField) menyScene.lookup("#nameBoks");
        	name.setText(String.valueOf("Hei "+Main.profile.getFirstName()+"!"));
        	
        	TextField status = (TextField) menyScene.lookup("#statusBoks");
        	status.setText("Status: "+getError());
        	
        	//TextField reqs = (TextField) menyScene.lookup("#requestsBoks");
        	
        	
        	TextField paths = (TextField) menyScene.lookup("#pathBoks");
        	paths.setText("../"+path);
        	
        	
        	
        }catch (Exception e){
        	e.getCause();
        	System.out.println(e.getCause());
        }
        
        window.show();
        
     
    }
    
    @FXML
	private void toPosition(ActionEvent event) throws IOException {
  		String produkter = "../JavaFX/NewMyPosition.fxml";
  		Parent produktParent = FXMLLoader.load(getClass().getResource(produkter));
  		Scene produktScene = new Scene(produktParent);
  		Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
  		window.setScene(produktScene);
  		
  		
  		
  		window.show();
	}

}
