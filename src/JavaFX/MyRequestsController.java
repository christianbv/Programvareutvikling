package JavaFX;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.bson.Document;
import org.bson.types.ObjectId;

import core.requestPackage.RequestConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import mainPackage.Main;

//Oversikt over mine forespørsler fra andre
public class MyRequestsController implements Initializable{

	private static String path;
	@FXML TextField nameBoks;
	@FXML TextField statusBoks;
	@FXML TextField pathBoks;
	
	@FXML TableView<Request> table;
	@FXML TableColumn<Request, String> nameCol;
	@FXML TableColumn<Request, String> ratingCol;
	@FXML TableColumn<Request, String> categoryCol;
	@FXML TableColumn<Request, String> productCol;
	@FXML TableColumn<Request,String> tidCol;
	@FXML TableColumn<Request, String> statusCol;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
    	nameBoks.setText("Hei " + Main.profile.getFirstName() + "!");
		
		nameCol.setCellValueFactory(p->p.getValue().nameProperty());
		ratingCol.setCellValueFactory(p->p.getValue().ratingProperty());
		categoryCol.setCellValueFactory(p->p.getValue().categoryProperty());
		productCol.setCellValueFactory(p->p.getValue().productProperty());
		tidCol.setCellValueFactory(p->p.getValue().dateProperty());
		statusCol.setCellValueFactory(p->p.getValue().statusProperty());
		
		loadActiveRequests();
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.setPath("../Menu/ActiveShares");
		this.setStatus("Alt fungerer som normalt..");
		this.setName();
		
	}
	
	// On-action metoder:
	@FXML 
	private void removeRequest() {
		Request req = table.getSelectionModel().getSelectedItem();
		String reqId = req.getId();
		RequestConnection.deleteRequest(reqId);
		System.out.println("Slettet request!");
		table.getItems().remove(table.getSelectionModel().getSelectedItem());
	}
	
	@FXML 
	private void getPhoneNumber(ActionEvent event) {
		ObservableList<Request> reqs = table.getSelectionModel().getSelectedItems();
		if(reqs.size() != 1) {
			return;
		}else {
			Request req = reqs.get(0);
			
			if(req.getStatus() != "Accepted") {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Sharen er ikke godkjent av motparten ennå!");
				alert.setContentText("Du må vente til sharen er godkjent før du kan få telefonnummer...");
				alert.showAndWait();
				return;
			}else {
			Integer nr = req.getPhone();
			String fornavn = req.getFirstName();
						 
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Telefonnummer for ny Share");
			alert.setHeaderText("Tlf til "+fornavn+" er: "+nr);
			alert.setContentText("Gratulerer med en ny Share!");
			alert.showAndWait();
			}
		}
	}
	

	// Hjelpemetoder:
	
	private void loadActiveRequests() {
		//ObjectId id = new ObjectId("5c8789de5aca9e7719ba921d");
		if(Main.profileId == null) return;
		
		ArrayList<Document> res = RequestConnection.getActiveShares(Main.profileId);
		ObservableList<Request> list = FXCollections.observableArrayList();

		for(Document doc : res) {
			Document UserCol = (Document) doc.get("UserCol");
			Document ItemCol = (Document) doc.get("ItemCol");
			Document CatCol = (Document) doc.get("CatCol");
			
			String reqId = doc.getObjectId("_id").toString();
			
			String giverId = doc.get("giver").toString();
			
			String fn = UserCol.getString("firstname");
			String sn = UserCol.getString("lastname");
			String name = fn+" "+sn;
			
			String rat = String.valueOf(UserCol.getInteger("rating"));
			String kategori = CatCol.getString("Kategori");
			String produkt = ItemCol.getString("Name");
			Integer phone =UserCol.getInteger("phonenumber");
			
			String status = RequestConnection.convertStatus(doc.getInteger("isAccepted"));
			String reqTime = doc.getDate("reqTime").toString();
			
			Request req = new Request(reqId,name,rat,kategori,produkt,reqTime,status,phone);
			
			list.add(req);
		
			
		}
		table.getItems().setAll(list);
	}
	
	
	
	
	
	
	
	// Change-stage methods:	
	
	@FXML  
    private void toGiveShare(ActionEvent event) throws IOException {
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
	private void toAskForProducts(ActionEvent event) throws IOException {
		Parent menyParent = FXMLLoader.load(getClass().getResource("NewNearbyProducts.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
	}
	
	public void toLogin(ActionEvent event) throws IOException {
	        Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
	        Scene menyScene = new Scene(menyParent);

	        //This line gets the Stage information
	        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	        window.setScene(menyScene);
	        window.show();
	}

	public void toRequests(ActionEvent event) throws IOException {
	        Parent foresprslParent = FXMLLoader.load(getClass().getResource("NewRequests.fxml"));
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

    public void toProducts(ActionEvent event) throws  IOException {
        String produkter = "../JavaFX/NewProducts.fxml";
        Parent produktParent = FXMLLoader.load(getClass().getResource(produkter));
        Scene produktScene = new Scene(produktParent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(produktScene);
        System.out.println("Er i toProducts");

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
	private void toPosition(ActionEvent event) throws IOException {
  		String produkter = "../JavaFX/NewMyPosition.fxml";
  		Parent produktParent = FXMLLoader.load(getClass().getResource(produkter));
  		Scene produktScene = new Scene(produktParent);
  		Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
  		window.setScene(produktScene);
  		
  		
  		
  		window.show();
	}
	

    
    public String getError() {
        String good = "Alt fungerer normalt...";
        return good;

    }
	
    public void setStatus(String string) {
    	statusBoks.setText(string);
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
