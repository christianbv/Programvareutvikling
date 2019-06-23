package JavaFX;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mainPackage.Main;


public class ProductController implements Initializable{

	public ProductController() {
	}
	private Main main;
	
	private static String path = "{null}";
	private static String category = null;
	private static String item = null;
	private static List<Document> chosenItems;
	private static List<Document> usersItems;
	private static String itemId = null;
	static Integer id_int = 1;
    
    @FXML TextField ratingBoks;
    @FXML TextField requestsBoks;
    @FXML TextField communityBoks;
    
    @FXML HBox senterBoks;
    
    // nederst i applikasjonen
    @FXML TextField statusBoks;
    @FXML TextField pathBoks;
    
    //øverst i applikasjonen
    @FXML TextField nameBoks;


    //Products:
    @FXML public TableView<Item> tableId;
    @FXML public TableColumn<Item,String> idCol;
    @FXML public TableColumn<Item,String> nameCol;
    @FXML public TableColumn<Item,String> categoryCol;
    @FXML Button deleteItemButton;
    
    //Add products:
    @FXML ComboBox<String> categoryBoks;
    @FXML ComboBox<String> itemBoks;
    @FXML Button addItemButton;
    
    
    @Override
    public void initialize(URL foo, ResourceBundle arg) {
    	idCol.setCellValueFactory(p->p.getValue().idProperty());
    	nameCol.setCellValueFactory(p->p.getValue().nameProperty());
    	categoryCol.setCellValueFactory(p->p.getValue().categoryProperty());
    	
    	tableId.getItems().setAll(getItem());
    	tableId.setEditable(true);
    	tableId.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // denne fungerer ikke av en eller annen rar grunn

    	nameBoks.setText("Hei " + Main.profile.getFirstName() + "!");
    	pathBoks.setText("../Menu/Products");
    	
    	itemBoks.setDisable(true);
    	categoryBoks.getItems().addAll(getCategories());
    	
    	
    	// denne kjøres før javafx vises!
    	statusBoks.setText("Alt fungerer som normalt..");
    }
    
    
    // Gets all the items of current users
    public ObservableList<Item> getItem() {
    	ObservableList<Item> items = FXCollections.observableArrayList();
    	
    	List<Document> foo = Main.getAllUserItems(Main.profileId);
    	usersItems = foo;
    	for(Document doc:foo) {
    		items.add(new Item(doc.get("Id").toString(),doc.get("Kategori").toString(),doc.get("Item").toString()));
    		id_int++;
    	}
    	
    	
    	
		return items;
    }
   	
    @FXML
    public void onCategoryChosen(ActionEvent event) {
    	String foo = categoryBoks.getValue();
    	category=foo;
    	
    	itemBoks.setDisable(false);
    	itemBoks.getItems().clear();
    	itemBoks.getItems().addAll(getItems(foo));
    }
    
    @FXML 
    public void onItemChosen(ActionEvent event) {
    	String foo = itemBoks.getValue();
    	item = foo;
    }
    
    @FXML 
    public void addItem(ActionEvent event) {
    	List<Document> foo = new ArrayList<Document>();
    	chosenItems.stream().filter(p->p.get("Name").equals(item)).map(p->foo.add(p)).collect(Collectors.toList());
    	ObjectId itemId = new ObjectId(foo.get(0).get("_id").toString());
    	
    	// Her må det logikk inn for å unngå at man legger til et item som ikke har kategori feks...
    	Item it = new Item(itemId.toString(),category,item);
    	
    	//Logikk for å sjekke at man ikke kan legge til et item man allerede har
    	boolean itemExists = usersItems.stream()
    			.anyMatch(p->p.get("Item").toString().equals(it.getNavn()));
    	
    	
    	if(!itemExists && !(category.equals(null) && !(item.equals(null))))  {
    		System.out.println("Item does not exist, and is therefore added");
        	tableId.getItems().add(it);
        	Main.newUserItem(itemId);
    	}else {
    		System.out.println("Item already exist!");
    		statusBoks.setText("Item already exist!");
    		displayError("foo",3);
    	}		
    }
	
    
    // Gets all categories from database
    public ObservableList<String> getCategories() {
		ObservableList<String> foo = FXCollections.observableArrayList();
		Main.getAllCategoriesAsStrings().stream().map(p->foo.add(p)).collect(Collectors.toList());
		return foo;
	}
	
    //Gets all items from database for @Param category
    public ObservableList<String> getItems(String category) {
		ObservableList<String> foo = FXCollections.observableArrayList();
		List<Document> itemsFromCat = Main.getAllItemsOnStringPred(category);
		chosenItems = itemsFromCat;
		itemsFromCat.stream().map(p->foo.add(p.get("Name").toString())).collect(Collectors.toList());
		return foo;
	}
 
    @FXML   
    public void deleteItem(ActionEvent event) {
    	ObservableList<Item> rader = tableId.getSelectionModel().getSelectedItems();
    	tableId.getItems().removeAll(rader);
    	
    	for(Item item : rader) {
    		ObjectId id = new ObjectId(item.getId());
    		Main.deleteRowUserItems(id);
    	}
    	
    	System.out.println("Fjernet radene: "+rader);
    	
    	
    }
	
   
    @FXML
    public void removeSelected(ActionEvent event) {
    	tableId.getSelectionModel().clearSelection();
    }
    
   
    public String getError() {
    	String good = "Alt fungerer normalt...";
    	return good;
    	
    }
    
    
    // Change-stage-methods: 
    
    
   
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
    
    public void toGiveShare(ActionEvent event) throws IOException {
        Parent kategoriParent = FXMLLoader.load(getClass().getResource("NewRequests.fxml")); //
        Scene kategoriScene = new Scene(kategoriParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(kategoriScene);
        window.show();
    }
    
    public void toActiveShares (ActionEvent event) throws IOException { 
    	Parent foresprslParent = FXMLLoader.load(getClass().getResource("NewMyRequests.fxml"));
        Scene foresprslScene = new Scene(foresprslParent);

        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(foresprslScene);
        window.show();
    }


    public void toTakeShare(ActionEvent event) throws IOException { 
    	Parent foresprslParent = FXMLLoader.load(getClass().getResource("NewNearbyProducts.fxml"));
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
	private void toPosition(ActionEvent event) throws IOException {
  		String produkter = "../JavaFX/NewMyPosition.fxml";
  		Parent produktParent = FXMLLoader.load(getClass().getResource(produkter));
  		Scene produktScene = new Scene(produktParent);
  		Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
  		window.setScene(produktScene);
  		
  		
  		
  		window.show();
	}
    
    public void displayError(String errorMsg, int tid) {
    	
    }
    
   
    
    
    
}
	
	
    

































    

    
   
    
    
    

	
	
    


