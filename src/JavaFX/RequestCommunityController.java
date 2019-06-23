package JavaFX;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.bson.Document;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainPackage.Main;


public class RequestCommunityController implements Initializable{
	

    @FXML
    TextField communityNavn;
    @FXML
    TextArea beskrivelse;

    private Main main;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {


    }
    
    /** Checks if the community that is suggested is valid, i.e. that it has a name and a description. 
     * 	Both the name and the description of the community are strings, whereas the name can only contain letters. 
     * 	
     * 	@param name 			the name of the requested community
     *  @param description		description of the requested community. Can for instance contain coordinates etc.
     */
    private boolean checkRequestedCommunity(String name, String description) {
    	if(name.length() < 3 || name.length() > 30) {
    		throw new IllegalArgumentException("The length of the communityname must be between 3-30 chars.");
    	}
    	if(!(name.matches("[0-9]+"))) {
    		throw new IllegalArgumentException("The name of the community cannot contain numbers!");
    	}
    	if(description.length() < 5) {
    		throw new IllegalArgumentException("The requested community must have a thorough description ");
    	}
    	return true;    	
    }
    
    
    @FXML
    public void suggest(ActionEvent event) throws IOException {
        //List<Document> col = Main.getAllRequestedCommunities();

        String comNavn = communityNavn.getText();
        String comBeskrivelse = beskrivelse.getText();
        if(checkRequestedCommunity(comNavn,comBeskrivelse)) {
            System.out.println("ComNavn: " + comNavn + "-----  Beskrivelse: " + comBeskrivelse);
            Main.newRequestedCommunity(comNavn, comBeskrivelse);
        }

        //Sender deg tilbake til registreringen.
        Parent menyParent = FXMLLoader.load(getClass().getResource("registrerBruker.fxml"));
        Scene menyScene = new Scene(menyParent);
        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();


    }


    @FXML
    public void toRegister(ActionEvent event) throws IOException {
        Parent menyParent = FXMLLoader.load(getClass().getResource("registrerBruker.fxml"));
        Scene menyScene = new Scene(menyParent);
        System.out.println("RequestCommunity");
        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
        window.show();
    }
	
	
	
	
	
	

}
