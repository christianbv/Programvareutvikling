package JavaFX;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mainPackage.Main;
import javafx.fxml.Initializable;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.bson.Document;
import org.bson.types.ObjectId;

import core.requestPackage.RequestConnection;

//Mine mottate forespørsler som kan godtas/avslås
public class RequestsController implements Initializable{// implements Initializable {

    public RequestsController(){

    }
    private Main main;

    private static String path = "{null}";
    private String status = "{Crashed}";
    static String category = null;
    static String item = null;

    @FXML TextField ratingBoks;
    @FXML TextField requestsBoks;
    @FXML TextField communityBoks;


    // nederst i applikasjonen
    @FXML TextField statusBoks;
    @FXML TextField pathBoks;


    //øverst i applikasjonen
    @FXML TextField nameBoks;


    //Products:
    @FXML public TableView<Request> tableId;
    @FXML public TableColumn<Request,String> firstNameCol;
    @FXML public TableColumn<Request,String> productCol;
    @FXML public TableColumn<Request,String> dateCol;
    @FXML TableColumn<Request,String> statusCol;

 
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	nameBoks.setText("Hei " + Main.profile.getFirstName() + "!");

    	firstNameCol.setCellValueFactory(p->p.getValue().firstnameProperty());
    	productCol.setCellValueFactory(p->p.getValue().productProperty());
    	dateCol.setCellValueFactory(p->p.getValue().dateProperty());
    	statusCol.setCellValueFactory(p->p.getValue().statusProperty());
    	tableId.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	
    	
    	if(Main.profileId == null) {
    		System.out.println("Du er ikke logget inn!!");
    		try {
    			toLogin(new ActionEvent());
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	else {
        	tableId.getItems().setAll(loadMyRequests(Main.profileId));
        	System.out.println("Loader mine requests..");

    	}
    	this.setStatus("Alt fungerer som normalt..");
    	this.setPath("../Menu/GiveShare");
    	this.setName();

	}
    
    private ObservableList<Request> loadMyRequests(ObjectId id){
    	ObservableList<Request> reqs = FXCollections.observableArrayList();
    	
    	for(Document doc : RequestConnection.getMyShares(id)) {
    		Document usr = (Document) doc.get("UserCol");
    		Document itm = (Document) doc.get("ItemCol");
    		
    		String fn = usr.getString("firstname");
    		String sn = usr.getString("lastname");
    		String userId = usr.getObjectId("_id").toString();
    		String date = doc.getDate("reqTime").toString();
    		String status = RequestConnection.convertStatus(doc.getInteger("isAccepted"));
    		String produkt = itm.getString("Name");
    		String Id = doc.get("_id").toString();
    		reqs.add(new Request(Id,fn,sn,produkt,date,status,"foo",userId));
    	}
    	
    	return reqs;
    }
    
    @FXML
    private void rejectReq(ActionEvent event) {
    	Request req = tableId.getSelectionModel().getSelectedItem();
    	RequestConnection.changeStatus(req.getId(), -1);
    	req.setStatusCol("Rejected");
    	
    	
    }
   
    @FXML
    private void acceptReq(ActionEvent event) {
    	Request req = tableId.getSelectionModel().getSelectedItem();
    	String reqId = req.getId();
    	
    	//Logikk for å legge til dette i ExchangeHistory ligger i Main
    	Main.addToExchangehistory(reqId);
    	
    	Main.updateRating(new ObjectId(reqId));
    	
    	RequestConnection.changeStatus(reqId, 1);
    	req.setStatusCol("Accepted");
    	
    	tableId.getItems().setAll(loadMyRequests(Main.profileId));
    }
    
    /*
    @FXML
    private void deleteReq(ActionEvent event) {
    	Request req = tableId.getSelectionModel().getSelectedItem();
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setHeaderText("Sletting av forespørsel");
    	alert.setContentText("Er du sikker på at du vil slette forespørselen?");
    	
    	Optional<ButtonType> res = alert.showAndWait();
    	if(!res.isPresent()) {
    		// Bruker har trykket x på alerten
    		return;
    		
    	}
    	else if(res.get() == ButtonType.OK) {
    		// Sletter request
    		
       		//RequestConnection.deleteRequest(req.getId());
    		tableId.getItems().remove(req);
    		if(req.getStatus() == "Pending") {
    			System.out.println("Request hidden!");
    			return;
    		}
       		System.out.println("Request deleted!");
       		
       		return;
    		
    		
    	}else if(res.get() == ButtonType.CANCEL) {
    		
    		return;
    	}
    	
    }
    */
    
    
    
    
    
    
    
    // Change-stage metoder

    public void toLogin(ActionEvent event) throws IOException {
        Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }

    public void toCategory(ActionEvent event) throws IOException {
        Parent kategoriParent = FXMLLoader.load(getClass().getResource("AskForProducts.fxml"));
        Scene kategoriScene = new Scene(kategoriParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(kategoriScene);
        window.show();
    }

    public void toProducts(ActionEvent event) throws IOException {
        Parent foresprslParent = FXMLLoader.load(getClass().getResource("NewProducts.fxml"));
        Scene foresprslScene = new Scene(foresprslParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(foresprslScene);
        window.show();
    }

    public void toSettings(ActionEvent event) throws IOException {
        //changeCenter(event);
        path += "/Settings";

        Parent innstillingParent = FXMLLoader.load(getClass().getResource("NewSettings.fxml"));
        Scene innstillingScene = new Scene(innstillingParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(innstillingScene);


        try {

            TextField name = (TextField) innstillingScene.lookup("#nameBoks");
            name.setText(String.valueOf("Hei "+Main.profile.getFirstName()+"!"));

            TextField status = (TextField) innstillingScene.lookup("#statusBoks");
            status.setText("Status: "+getError());


            TextField paths = (TextField) innstillingScene.lookup("#pathBoks");
            paths.setText("../"+path);


            TextField fName = (TextField) innstillingScene.lookup("#firstNameBoks");
            fName.setText(Main.profile.getFirstName());

            TextField sName = (TextField) innstillingScene.lookup("#surNameBoks");
            sName.setText(Main.profile.getLastName());

            PasswordField pw = (PasswordField) innstillingScene.lookup("#newPasswordBoks");
            pw.setPromptText((Main.profile.getPassword()));

            // må fikses
            TextField age = (TextField) innstillingScene.lookup("#ageBoks");
            age.setText(String.valueOf(Main.profile.getBirthYear()));

            TextField email = (TextField) innstillingScene.lookup("#emailBoks");
            email.setText(Main.profile.getEmail());

            TextField phone = (TextField) innstillingScene.lookup("#phoneBoks");
            phone.setText("Denne må fikses");

            // DatePicker birthday = (DatePicker) innstillingScene.lookup("#birthdayBoks");

            TextField memberSince = (TextField) innstillingScene.lookup("#memberSinceBoks");
            memberSince.setText("Fiks! - "+ Main.profile.getBirthYear());


        }catch (Exception e){

        }
        window.show();



    }

    public void toHistory(ActionEvent event) throws IOException {
        Parent historikkParent = FXMLLoader.load(getClass().getResource("historikk.fxml"));
        Scene historikkScene = new Scene(historikkParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(historikkScene);
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
	
    
    @FXML
    private void toTakeShare(ActionEvent event) throws IOException {
        Parent foresprslParent = FXMLLoader.load(getClass().getResource("NewNearbyProducts.fxml"));
        Scene foresprslScene = new Scene(foresprslParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(foresprslScene);
        window.show();
    }
    
    @FXML
    private void toActiveShare(ActionEvent event) throws IOException {
        Parent foresprslParent = FXMLLoader.load(getClass().getResource("NewMyRequests.fxml"));
        Scene foresprslScene = new Scene(foresprslParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(foresprslScene);
        window.show();
    }
    	
    public String getError() {
        String good = "Alt fungerer normalt...";
        return good;

    }

    public String getStatus() {
    	return this.status;
    }
    
    public void setStatus(String string) {
    	statusBoks.setText(string);
    	this.status = string;
    }

    public void setPath(String string) {
    	this.pathBoks.setText(string);
    	path = string;
    }
    
    public void setName() {
    	if(Main.profileId == null || Main.profile == null) return;
    	else this.nameBoks.setText("Hei "+Main.profile.getFirstName()+"!");
    }



}
