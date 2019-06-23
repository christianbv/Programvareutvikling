package JavaFX;

import java.awt.Color;
import java.io.IOException;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.stage.Stage;
import mainPackage.AdminMain;
import mainPackage.Main;

public class LoginController {
	

	private boolean isAdmin = false;
	private static boolean booleanDelete = false;
	private static String path = "{null}";

	String adminbruker = "admin@admin.no";
	String adminpassord = "thisistheadmin";
	    
    @FXML TextField epost;
    @FXML TextField passord;
    

	public LoginController() {
		// TODO Auto-generated constructor stub
	}
	
	public String getError() {
    	String good = "Alt fungerer normalt...";
    	return good;
    	
    }
	
	 // Bruker logger inn:  
    @FXML
    public void onLogIn(ActionEvent event) throws IOException {
    	String un = epost.getText().toLowerCase();
    	String pw = passord.getText();
    	
    	// Sjekker om bruker er admin
    	if ((un.equals(adminbruker) && pw.equals(adminpassord))||(un.equals(adminbruker.split("@")[0]) && pw.equals("passord"))) {
    		this.isAdmin = true;
    		toAdmin(event);
    		return;
    	}
    	// Bruker er ikke Admin, pr�ver logge inn..
    	try {	
    		if (Main.checkLogin(un, pw)) {
        		System.out.println("logget inn.");
        		toMenu(event);
        		return;

    		}
    		else {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setContentText("Feil brukernavn/passord!");
    			alert.setX(800);
    			alert.setY(300);
    			alert.showAndWait();
    			passord.setText("");

    			return;
    		}
    		
    	}catch(Exception e) {
    		System.out.println("feil passord, feil username eller ingen bruker registrert! ");
    	}
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
        	TextField since = (TextField) menyScene.lookup("#userSinceBoks");
        	since.setText(AdminMain.dateToString((Date) Main.getUserDocument(Main.profile.getEmail()).get("time_joined")));
        	
        	TextField paths = (TextField) menyScene.lookup("#pathBoks");
        	paths.setText("../"+path);
        	
        	TextField com = (TextField) menyScene.lookup("#communityBoks");
        	com.setText("Gløshaugen");
        	
        	TextField req = (TextField) menyScene.lookup("#requestsBoks");
        	req.setText("0");
        	
        	
        }catch (Exception e){
        	e.getCause();
        	System.out.println(e.getCause());
        }
        
        window.show();
        
     
    }
    
    @FXML 
    public void toAdmin(ActionEvent event) throws IOException {
    	path = "Admin";
    	Parent menyParent = FXMLLoader.load(getClass().getResource("NewAdminUsers.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setMaximized(true);

        window.setScene(menyScene);
        window.show();
    }
    
    public void toRegistrer(ActionEvent event) throws IOException {
    	System.out.println("prøver gå til registrer..");

        Parent menyParent = FXMLLoader.load(getClass().getResource("registrerBruker.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(menyScene);
        window.show();
    }
    
    
    
	
	
	

}
