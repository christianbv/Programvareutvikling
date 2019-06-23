package JavaFX;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import org.bson.Document;
import org.bson.types.ObjectId;

import core.requestPackage.RequestConnection;
import core.userPackage.UserProfile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainPackage.AdminMain;
import mainPackage.Main;

public class MenuController implements Initializable {
	
	private static String path = "{null}";
	
    // øverst i applikasjonen
    @FXML TextField nameBoks;
    
    // nederst i applikasjonen
    @FXML TextField statusBoks;
    @FXML TextField pathBoks;


    // for denne kontrolleren:
    @FXML TextField nyRatingBoks;
    @FXML TextField requestsBoks;
    @FXML TextField communityBoks;
    @FXML TextField userSinceBoks;

	
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//ratingBoks.setText(String.valueOf(Main.getUserRating(Main.profileId)));
		
		requestsBoks.setText(loadNumberOfRequests(Main.profileId).toString());
		communityBoks.setText(Main.profile.getCommunity());
		
		Document doc = Main.getUserDocument(Main.profile.getEmail());
		userSinceBoks.setText(AdminMain.dateToString(doc.getDate("time_joined")));
		
		pathBoks.setText("../Menu");
		statusBoks.setText("Alt fungerer som normalt..");
		nameBoks.setText("Hei "+Main.profile.getFirstName());
		
		nyRatingBoks.setText(""+Main.getUserRating(Main.profileId));
	}
	
	private Integer loadNumberOfRequests(ObjectId id) {
		return RequestConnection.getNumRequests(id);
	}
	
	@FXML
    public void getProfile() throws IOException {
    	UserProfile prof = Main.profile;
    	int rating = prof.getRating();
    	//ratingBoks.setText(String.valueOf(prof.getRating()));
    	
    	
    }
	
    public String getError() {
    	String good = "Alt fungerer normalt...";
    	return good;
    	
    }
    
    
    
    
    
    
    
    
	// Change-stage methods:
	
    public void toHistory(ActionEvent event) throws IOException {
   	 Parent historikkParent = FXMLLoader.load(getClass().getResource("historikk.fxml"));
        Scene historikkScene = new Scene(historikkParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(historikkScene);
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
