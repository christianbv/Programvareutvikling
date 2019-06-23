package JavaFX;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import org.bson.Document;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainPackage.AdminMain;
import mainPackage.Main;

public class SettingsController implements Initializable{
	private static boolean booleanDelete = false;
	private static String path = "{null}";
	
    // øverst i applikasjonen
    @FXML TextField nameBoks;
    
    // nederst i applikasjonen
    @FXML TextField statusBoks;
    @FXML TextField pathBoks;

    
    // For settingscontroller:
    @FXML TextField firstNameBoks;
    @FXML TextField surNameBoks;
    @FXML PasswordField newPasswordBoks;
    @FXML TextField emailBoks;
    @FXML TextField phoneBoks;
    @FXML TextField birthdayBoks;
    @FXML TextField memberSinceBoks;
    @FXML CheckBox	deleteBoks;

	public SettingsController() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		System.out.println("init kalles");
		
		nameBoks.setText("Hei "+Main.profile.getFirstName()+"!");
		statusBoks.setText(getError());
		pathBoks.setText("../Meny/Settings");
		
		firstNameBoks.setText(Main.profile.getFirstName());
		surNameBoks.setText(Main.profile.getLastName());
		//newPasswordBoks.setPromptText(Main.profile.getPassword());
		//emailBoks.setText(Main.profile.getEmail());
		
		Document doc = Main.getUserDocument(Main.profile.getEmail());
		
		newPasswordBoks.setText(doc.get("password").toString());
		emailBoks.setText(doc.get("email").toString());
		phoneBoks.setText(doc.get("phonenumber").toString());
		memberSinceBoks.setText(AdminMain.dateToString((Date) doc.get("time_joined")));
		birthdayBoks.setText(AdminMain.dateToString((Date) doc.get("birthdate")));
		
		
		
	}
    
	
	@FXML 
    public void onUpdateSettings(ActionEvent event) throws IOException{
    	// Dette må sjekkes opp mot profile på en eller annen måte
    	String fName = firstNameBoks.getText();
    	String lName = surNameBoks.getText();
    	String pw = newPasswordBoks.getText();
    	String phoneNr = phoneBoks.getText();
	    try {
	    	Main.updateUser(fName, lName, pw, phoneNr);
	    	System.out.println("User updated:\nName: " + Main.profile.getFirstName() + " " + Main.profile.getLastName() + 
	    			"\npw: " + Main.profile.getPassword() + "\n");
	    	toMenu(event);
	    }
	    catch(Exception e){
	    	System.out.println("Klarte ikke oppdatere bruker...");
	    }
    }
	
    @FXML 
    public void onAbortSettings(ActionEvent event) throws IOException {
    	toMenu(event);
    }
    
    @FXML 
    public void onDeleteUser(ActionEvent event) throws IOException {
    	if(booleanDelete) {
    		// user gets deleted:
    		try {
    			Main.deleteUser();
        		System.out.println("Slettet brukeren: "+emailBoks.getText()+"--"+firstNameBoks.getText());
        		toLogin(event);

    		}
    		catch(Exception e) {
    			System.out.println("klarte ikke slette brukeren. noe er veldig galt!");
    		}
    		
    		
    	}else {
    		System.out.println("Dette bør skrives til status: Sjekkboks ikke huket av!");
    	}
    }
   
    @FXML
    public void booleanDelete(ActionEvent event) throws IOException{
    	
    	if (deleteBoks.isSelected()) {
    		booleanDelete = true;
    		System.out.println("huket av sjekkboks!");
    	}
    	else booleanDelete = false;
    	
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

	// Change-stage methods:
    public void toLogin(ActionEvent event) throws IOException {
    	Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
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


	
	
	

}
