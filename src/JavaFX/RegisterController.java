

package JavaFX;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mainPackage.AdminMain;
import mainPackage.Main;

public class RegisterController implements Initializable{
	
	private static String path = "../Registrer";

	
	@FXML TextField fornavn;
	@FXML TextField etternavn;
	@FXML TextField epostadresse;
	@FXML TextField telefon;
	@FXML ChoiceBox<String> communities;
	@FXML TextField passordet;
	@FXML DatePicker date;
    @FXML Text feilmelding;

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	communities.getItems().setAll(loadCommunities());
		
	}

	
    
    private ObservableList<String> loadCommunities() {
    	ObservableList<String> communities = FXCollections.observableArrayList();
    	MongoDatabase db = Main.getDatabase();
    	MongoCollection<Document> col = db.getCollection("Communities");
    	ArrayList<Document> agg = col.aggregate(Arrays.asList(
    								)).into(new ArrayList<Document>());
    	
    	for(Document doc : agg) {
    		communities.add(doc.getString("Navn"));
    	}	
    	return communities;
    	
    	
    }
    
    
    private void addLocationRow(ObjectId userid, String community) {
    	MongoCollection<Document> col = Main.getDatabase().getCollection("Locations");
		Document doc = new Document();
		doc.append("userid", userid);
		doc.append("isActive", true);
		doc.append("time", new Date());
		
    	if(community.equals("Gløshaugen")){
    		doc.append("latitude", 63.41804674250877);
    		doc.append("longitude", 10.403588712215425);	
    		
    	}else if(community.equals("Solsiden")) {
    		doc.append("latitude", 63.4304703102783);
    		doc.append("longitude", 10.40561478585005);		
    		
    		
    	}else if (community.equals("Kalvskinnet")) {
    		doc.append("latitude", 63.42836317090364);
    		doc.append("longitude", 10.38881497923285);		
    		
    	}else if(community.equals("Dragvoll")){
    		doc.append("latitude", 63.407562409724704);
    		doc.append("longitude", 10.46894794330001);		
    		
    	}else {
    		doc.append("latitude", 63.42373135879483);
    		doc.append("longitude", 10.393927395343782);		
    		
    	}
    	col.insertOne(doc);
    	
    	
    }
	
    // Change-stage controller:

	@FXML 
	public void onRegistrering(ActionEvent event) throws NoSuchAlgorithmException {
		

        Document newUser = new Document();
        newUser.append("firstname", fornavn.getText());
        newUser.append("lastname", etternavn.getText());
        newUser.append("email", epostadresse.getText().toLowerCase());
        newUser.append("phonenumber", Integer.parseInt(telefon.getText()));
        newUser.append("password", core.encryptionPackage.PasswordHasher.hashPassword(passordet.getText().toString()));
        newUser.append("time_joined", new Date());
        newUser.append("rating", 0);
        newUser.append("birthdate", AdminMain.localdateToDate(date.getValue()));
        newUser.append("community", communities.getSelectionModel().getSelectedItem());
        
        
        
        /*
        String fn = fornavn.getText();
        String en = etternavn.getText();
        String ep = epostadresse.getText();
        String fd = date.getText();
        String[] parts = fd.split("-");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        */
        
        try { 
        	
        	// Bruker legges inn i main..
        	if (Main.checkUser(newUser)) {
        		addLocationRow(Main.getUserOnEmail(newUser.getString("email")),newUser.getString("community"));
        		toLogin(event);
                //toMenu(event);
        	}
        	else {
        	}
        	
        }catch(Exception e) {
        	System.out.println("Klarte ikke legge til ny bruker..");
        	System.out.println(e.getCause());
        	
        	/* - Stine som har lagt inn:
        	 * 
        	if (!UserProfile.nameTester(fn) || !UserProfile.nameTester(en)) {
                feilmelding.setText("Ugyldig navn.");
            }
            if (!UserProfile.dateTest(part1,part2,part3)) {
                feilmelding.setText("Ugyldig f�dselsdato.");
            }
            if (!UserProfile.emailTester(ep)) {
                feilmelding.setText("Ugyldig e-post.");
            }
            */

        	
        	
        	
        	
        }
        
        	
    }
	
	@FXML
	public void toLogin(ActionEvent event) throws IOException {
    	Parent menyParent = FXMLLoader.load(getClass().getResource("logginn.fxml"));
        Scene menyScene = new Scene(menyParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(menyScene);
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
	
	public String getError() {
		String good = "Alt fungerer normalt...";
	    return good;
	    	
	}

	@FXML
	public void toRequestCommunity(ActionEvent event) throws IOException {
		Parent menyParent = FXMLLoader.load(getClass().getResource("RequestCommunity.fxml"));
		Scene menyScene = new Scene(menyParent);
		System.out.println("RequestCommunity");
		//This line gets the Stage information
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(menyScene);
		window.show();
	}
	
}
