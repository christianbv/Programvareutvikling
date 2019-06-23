package JavaFX;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mainPackage.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import core.requestPackage.RequestConnection;

//Be om produkter fra andre
public class AskForProductsController implements Initializable, MapComponentInitializedListener {

    private static String path = "{null}";
    private GoogleMap map;
	private Document myLocation;
	private ArrayList<Document> positions;
	private ArrayList<Document> shownUsers;
	private ArrayList<Marker> markers;
	private LatLong myPosition;
    private static Request EditRequest;
    private ArrayList<Document> allRequests;
    List<String> markerIcons;
    
    private static String category = null;
    private static String item = null;

    // øverst i applikasjonen
    @FXML TextField nameBoks;

    // nederst i applikasjonen
    @FXML TextField statusBoks;
    @FXML TextField pathBoks;


    // for denne kontrolleren:
    @FXML TextField ratingBoks;
    @FXML TextField requestsBoks;
    @FXML TextField communityBoks;
    
    // NewNearbyProducts
    @FXML ComboBox<String> categoryBoks;
    @FXML ComboBox<String> itemBoks;
    
    @FXML TableView<NumberedRequest> tableId;
    @FXML TableColumn<NumberedRequest, String> nrCol;
    @FXML TableColumn<NumberedRequest,String> firstNameCol;
    @FXML TableColumn<NumberedRequest,String> surNameCol;
    @FXML TableColumn<NumberedRequest,String> ratingCol;
    @FXML TableColumn<NumberedRequest,String> categoryCol;
    @FXML TableColumn<NumberedRequest,String> productCol;
    
    //Maps:
    @FXML GoogleMapView mapView;



    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nameBoks.setText("Hei "+Main.profile.getFirstName()+"!");
		statusBoks.setText("Alt fungerer som normalt..");
		pathBoks.setText("../Menu/NewProducts");
		
		categoryBoks.getItems().setAll(loadCategories());
		itemBoks.setDisable(true);
		
		nrCol.setCellValueFactory(p->(p).getValue().nrProperty());
		firstNameCol.setCellValueFactory(p->p.getValue().firstnameProperty());
		surNameCol.setCellValueFactory(p->p.getValue().surnameProperty());
		ratingCol.setCellValueFactory(p->p.getValue().ratingProperty());
		categoryCol.setCellValueFactory(p->p.getValue().categoryProperty());
		productCol.setCellValueFactory(p->p.getValue().productProperty());

		
		tableId.setEditable(false);
		tableId.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableId.getItems().setAll(loadAllItems());
		
		setMarkerIcons();
		
		mapView.addMapInitializedListener(this);
		
		
	}
    
    //Maps:
	@Override
	public void mapInitialized(){
		MapOptions mapOptions = new MapOptions();
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
		        .minZoom(13)
		        .zoom(15);
        
        
        
        map = mapView.createMap(mapOptions);
        
        
        MarkerOptions mopt = new MarkerOptions();
        mopt.icon(markerIcons.get(0));
        mopt.position(new LatLong(63.41808,10.403562));
        
        LatLong glos = new LatLong(63.41808,10.403562);
        map.setCenter(glos);

     
        
        //Marker marker = new Marker(mopt);    
       
        //map.addMarker(marker);
        
        //map.addMarker(createMarker(getMyPosition()));
        
        //////////////////////

        System.out.println("Number of shown users: "+shownUsers.size());
        loadPositions(shownUsers);

        for (Marker marker : markers) {
        	map.addMarker(marker);
        }
        

	}

	
	private Marker createMarker(LatLong latlong) {
		MarkerOptions mopt = new MarkerOptions();
		mopt.label("You");
		Marker marker = new Marker(mopt);
		marker.setPosition(latlong);
		return marker;
	}
	
	private void loadPositions(ArrayList<Document> users) {
		
		MongoCollection<Document> col = Main.getDatabase().getCollection("Locations");
		int counter = 0;
		ArrayList<Marker> markers = new ArrayList<Marker>();
		
			for(Document user : users) {
				try {
					Document doc = col.find(Filters.eq("userid", new ObjectId(user.get("_id").toString()))).first();
					if(!(doc.getBoolean("isActive"))) {
						counter++;
						continue;
					}
					Marker marker = new Marker(new MarkerOptions().icon(markerIcons.get(counter)));
					marker.setPosition(new LatLong(doc.getDouble("latitude"),doc.getDouble("longitude")));
					markers.add(marker);
					
					
				}
				catch (Error e) {
					counter ++;
					continue;
				}
				counter ++;
				if (counter > 5) {
					break;
				}
			}
		
		
		this.markers = markers;
		
	}
	
	private void setMarkerIcons() {
		List<String> markerIcons = new ArrayList<>();
        markerIcons.add("http://maps.google.com/mapfiles/kml/paddle/1.png");
        markerIcons.add("http://maps.google.com/mapfiles/kml/paddle/2.png");
        markerIcons.add("http://maps.google.com/mapfiles/kml/paddle/3.png");
        markerIcons.add("http://maps.google.com/mapfiles/kml/paddle/4.png");
        markerIcons.add("http://maps.google.com/mapfiles/kml/paddle/5.png");
        markerIcons.add("http://maps.google.com/mapfiles/kml/paddle/6.png");
        this.markerIcons = markerIcons;		
	}
	

   
    
    private ObservableList<NumberedRequest> loadAllItems(){
    	// Henter ut alle fra tilhørende community!
    	ArrayList<Document> all = RequestConnection.allUserItemsAggregated(Main.profileId, Main.profile.getCommunity());
    	//ArrayList<Document> all = RequestConnection.allUserItemsAggregated();
    	return docToObservableList(all);
    }
    
   
    private ObservableList<NumberedRequest> loadItemsOnCategory(String kategori){
    	ArrayList<Document> all = RequestConnection.userItemsOnCategory(kategori);
    	return docToObservableList(all);
    }
    
    private ObservableList<NumberedRequest> loadItemsOnItem(String itm){
    	ArrayList<Document> all = RequestConnection.userItemsOnItem(itm);
    	return docToObservableList(all);
    }
    
    private ObservableList<NumberedRequest> docToObservableList(ArrayList<Document> docs) {
    	ObservableList<NumberedRequest> reqs = FXCollections.observableArrayList();
    	int number = 1;
    	ArrayList<Document> userArray = new ArrayList<>();
    	
    	for(Document doc : docs) {
    		Document usr = (Document) doc.get("UserCol");
    		Document cat = (Document) doc.get("CatCol");
    		Document item = (Document) doc.get("ItemCol");
    		NumberedRequest req = new NumberedRequest(
    							Integer.toString(number),
    							usr.get("_id").toString(),
    							usr.getString("firstname"),
    							usr.getString("lastname"),
    							usr.get("rating").toString(),
    							cat.getString("Kategori"),
    							item.get("_id").toString(),
    							item.getString("Name")
    									);
    		reqs.add(req);
    		userArray.add(usr);
    		number ++;
    	}
    	this.shownUsers = userArray;
    	return reqs;
    }
    

    
    @FXML
    private void onProductAsked(ActionEvent event) throws IOException{
    	NumberedRequest rad = tableId.getSelectionModel().getSelectedItem();
    	// her må det legges til metode for å legge til i requests-collection.. 
    	RequestConnection.addRequestDBInstance(rad.getId(), rad.getItemId());
    	toActiveShares(event);
    	
    }
      
   
    @FXML
    private void onCategoryChosen(ActionEvent event) throws IOException {
    	String foo = categoryBoks.getValue();
    	category=foo;
    	
    	itemBoks.setDisable(false);
    	itemBoks.getItems().clear();
    	itemBoks.getItems().addAll(getItemsOnCategory(foo));
    	
    	// legg inn logikk her for å vise alle useritems etter valgt kategori..
    	
    	tableId.getItems().clear();
    	tableId.getItems().setAll(loadItemsOnCategory(foo));
    	mapInitialized();
    }
    
    
    @FXML  
    private void onItemChosen(ActionEvent event) throws IOException {
    	String foo = itemBoks.getValue();
    	item = foo;
    	
    	tableId.getItems().clear();
    	tableId.getItems().setAll(loadItemsOnItem(foo));
    	mapInitialized();
    }
    
    
    // Backend-metoder:
    
    private ObservableList<String> getItemsOnCategory(String s) {
    	ObservableList<String> foo = FXCollections.observableArrayList();
		List<Document> itemsFromCat = Main.getAllItemsOnStringPred(s);
		itemsFromCat.stream().map(p->foo.add(p.get("Name").toString())).collect(Collectors.toList());
		return foo;
    }
 
   
    private ObservableList<String> loadCategories() {
    	ObservableList<String> foo = FXCollections.observableArrayList();
		Main.getAllCategoriesAsStrings().stream().map(p->foo.add(p)).collect(Collectors.toList());
		return foo;
    }
    
   
    
    
    
    
    
    
    // Change-stage-methods:

    
    @FXML
    private void toCategory(ActionEvent event) throws IOException{
    	
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



          /*
          try {







              // Fjerner raden som er valgt
              Button delButton = (Button) produktScene.lookup("#deleteItemButton");
              delButton.setOnAction(e->{
            	  Item rad = table.getSelectionModel().getSelectedItem();
            	  table.getItems().remove(rad);

            	  // bør komme et kall til databasen her om å fjerne item
              });
              */
        /*

         */

        window.show();
        //Tråden går hele veien hit før den flytter til productcontroller!

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
    private void toActiveShares(ActionEvent event) throws IOException {
        Parent kategoriParent = FXMLLoader.load(getClass().getResource("NewMyRequests.fxml"));
        Scene kategoriScene = new Scene(kategoriParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(kategoriScene);
        window.show();
    }
    
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
