package JavaFX;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.bson.Document;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapShape;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.shapes.Polyline;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import core.googleMapPackage.Place;
import core.googleMapPackage.UserPosition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainPackage.Main;
import netscape.javascript.JSException;

public class MyPositionController implements Initializable, MapComponentInitializedListener{
	private static String path = "{null}";
	private GoogleMap map;
	private Document myLocation;
	private LatLong myPosition;
	
    // øverst i applikasjonen
    @FXML TextField nameBoks;
    
    // nederst i applikasjonen
    @FXML TextField statusBoks;
    @FXML TextField pathBoks;
    
    // Denne siden:
    
    @FXML ChoiceBox<String> positionList;
    @FXML GoogleMapView mapView;
    @FXML CheckBox isActiveBoks;
    @FXML Button saveBoks;

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameBoks.setText("Hei "+Main.profile.getFirstName()+"!");
		statusBoks.setText("Alt fungerer som normalt..");
		pathBoks.setText("../Menu/MyPosition");
		
		loadPlaces();
		mapView.addMapInitializedListener(this);
		

		this.myLocation = loadMyPosition();
		//this.myPosition = new LatLong(myLocation.getDouble("latitude"),myLocation.getDouble("longitude"));
		//this.myPosition = new LatLong(63.4185,10.402);
		//this.myPosition = new LatLong((Double) myLocation.get("latitude"),(Double) myLocation.get("longitude"));
		//System.out.println("Loader inn posisjon");
		//this.myPosition = getMyPosition();
		//Double latitude = myLocation.getDouble("latitude");
		//Double longitude = myLocation.getDouble("longitude");
		//System.out.println("Alt good sålangt");
		//this.myPosition = new LatLong(10,40);
		
		
		isActiveBoks.setSelected(true);
		saveBoks.setDisable(false);
	}
	
	
	@Override
	public void mapInitialized(){
		MapOptions mapOptions = new MapOptions();
		System.out.println("Map initialize");
		double latitude = 0;
		double longitude = 0;
		LatLong latlong = new LatLong(latitude,longitude);
        
        mapOptions.center(latlong)
		        .mapType(MapTypeIdEnum.ROADMAP)
		        .overviewMapControl(false)
		        .panControl(false)
		        .rotateControl(false)
		        .scaleControl(false)
		        .streetViewControl(false)
		        .zoomControl(true)
		        .fullscreenControl(false)
		        .mapTypeControl(false)
		        .minZoom(14)
		        .zoom(16);
        /*
        MarkerOptions mopt = new MarkerOptions();
        mopt.label("You");
        mopt.position(new LatLong(63.41808,10.403562));
        */
        
        map = mapView.createMap(mapOptions);
        LatLong glos = new LatLong(63.41808,10.403562);
        map.setCenter(glos);

        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) ->{ 
        	this.saveBoks.setDisable(false);
        	LatLong pos = event.getLatLong();
        	this.myPosition = pos;
        	map.clearMarkers();
        	map.addMarker(createMarker(pos));
        	System.out.println(pos);
        });
        
       
        
        
        
        // Loader inn posisjon ved oppstart.
        Marker marker = createMarker(new LatLong((Double) myLocation.get("latitude"),(Double) myLocation.get("longitude")));
        map.addMarker(marker);

	}
	

	private Marker createMarker(LatLong latlong) {
		MarkerOptions mopt = new MarkerOptions();
		mopt.label("You");
		Marker marker = new Marker(mopt);
		marker.setPosition(latlong);
		return marker;
	}
	
	
	private Marker getStandardMarker() {
		MarkerOptions mopt = new MarkerOptions();
        mopt.label("You");
        mopt.position(new LatLong(63.41808,10.403562));
        return new Marker(mopt);
	}
	
	
	@FXML
	private void visibleChecker(ActionEvent events) {
		this.saveBoks.setDisable(false);
		if(isActiveBoks.isSelected()) {
			map.clearMarkers();
			MarkerOptions mopt = new MarkerOptions();
	        mopt.label("You");
	        mopt.position(new LatLong((Double) myLocation.get("latitude"),(Double) myLocation.get("longitude")));
	        Marker marker = new Marker(mopt);
			map.addMarker(marker);
			
		}else {
			map.clearMarkers();
		}
		
		Document doc = Main.getDatabase().getCollection("Locations").find().filter(Filters.eq("userid",Main.profileId)).first();
		doc.replace("isActive", isActiveBoks.isSelected());
		Main.getDatabase().getCollection("Locations").findOneAndReplace(Filters.eq("userid", Main.profileId), doc);
		System.out.println("Oppdatert synlighet!");
	}
	

	
	
	
	@FXML
	private void savePosition(ActionEvent event) throws IOException {
		if(!this.isActiveBoks.isSelected()) {
			return;
		}
		MongoCollection<Document> col = Main.getDatabase().getCollection("Locations");
		Document doc = col.find(Filters.eq("userid", Main.profileId)).first();
		System.out.println("Fått tak i ID");
		doc.replace("latitude", myPosition.getLatitude());
		doc.replace("longitude", myPosition.getLongitude());
		System.out.println("Latlong");
		doc.replace("isActive", isActiveBoks.isSelected());
		System.out.println("Prøver oppdatere posisjon..");
		
		col.findOneAndReplace(Filters.eq("userid", Main.profileId), doc);
		System.out.println("Oppdatert posisjon!");
		toMenu(event);
	}
	
	
	
	private Document loadMyPosition() {
		MongoCollection<Document> col = Main.getDatabase().getCollection("Locations");
		Document doc =  col.find(Filters.eq("userid", Main.profileId)).first();
		Document foo = new Document();
		foo.append("latitude", 10);
		foo.append("longitude", 40.3);
		if(doc.isEmpty()) {
			System.out.println("Tomt dokument..");
			return foo;
		}
		else {
			return doc;
		}
	}
	
		

	private void loadPlaces() {
		ObservableList<String> foo = FXCollections.observableArrayList();
		if(!Main.profile.getCommunity().equals("Gløshaugen")) {
		}else {
			ArrayList<Place> places = Place.getPlacesAtGlos();
			for(Place place : places) {
				foo.add(place.getPlace());
			}
		}
		
		this.positionList.setItems(foo);
	}
	
	@FXML
	private void positionChosen(ActionEvent event) {
		ArrayList<Place> places = Place.getPlacesAtGlos();
		for(Place place : places) {
			if(place.getPlace().equals(this.positionList.getSelectionModel().getSelectedItem())) {
				this.map.clearMarkers();
				Marker marker = this.getStandardMarker();
				
				LatLong latlong = new LatLong(place.getLatitude(),place.getLongitude());
				marker.setPosition(latlong);
				this.myPosition = latlong;
				this.map.addMarker(marker);
			}
		}
	}
	
	
	// Change-stage metoder:
	
	
	  
    
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
	
	public String getError() {
	    	String good = "Alt fungerer normalt...";
	    	return good;
	    	
	    }












	    


}
