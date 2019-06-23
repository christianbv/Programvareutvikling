package mainPackage;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.MongoQueryException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;

import JavaFX.Item;
import core.serverPackage.ServerConnect;
import core.userPackage.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	public static ServerConnect serverConnection;
	private static MongoDatabase db = null;
	private static MongoCollection<Document> collection;
	public static UserProfile profile;
	public static ObjectId profileId;
	//

	public Main(ServerConnect servercon) {
		serverConnection = servercon;
		db = servercon.getDataBase();

		// kobler til collection: "Users".
		collection = db.getCollection("Users");
	}

	public Main() {
	}

	// Returns the currently connected db
	public static MongoDatabase getDatabase() {
		return db;
	}

	// Returns the currently connected collection:
	public static MongoCollection<Document> getCollection(){
		return collection;
	}

	// Closes connection with the Server:
	private void closeConnection() {
		serverConnection.terminateConnection();
	}

	// Change Collection:
	public void setCollection(String col) {
		try {
			collection = db.getCollection(col);
			System.out.println("Changed connection to Collection: "+collection);
		}
		catch (MongoQueryException e){
			System.out.println("Could not connect to the Collection:"+collection+".");
			e.printStackTrace();
		}
	}



	// Queries for productController:
	
	/**	Returns all the categories in the collection Categories
	 * 	
	 * 	@returns List<Document> list of documents, each containing a category
	 */
	public static List<Document> getAllCategories() {
		MongoCollection<Document> col = db.getCollection("Categories");
		return (List<Document>) col.find().into(
				new ArrayList<Document>());
	}

	
	/**	Returns all categories in db as strings
	 * 	
	 * 	@return List<String> a list of strings, where each is the categoryname 
	 */
	public static List<String> getAllCategoriesAsStrings(){
		MongoCollection<Document> col = db.getCollection("Categories");
		List<String> tom = new ArrayList<>();
		List<Document> foo = (List<Document>) col.find().into(
				new ArrayList<Document>());
		foo.stream().map(p->tom.add(p.get("Kategori").toString())).collect(Collectors.toList());
		return tom;

	}

	/** Gets the id of the row in the collection Categories that matches the argument passed to the method
	 * 
	 * @param	kategori	the category in collection Categories
	 * @return				the ObjectId of the specified kategori
	 */
	public static ObjectId getCategoryId(String kategori) {
		MongoCollection<Document> col = db.getCollection("Categories");
		Document foo = col.find(Filters.eq("Kategori", kategori)).first();
		return (ObjectId) foo.get("_id");
	}
	
	/**	Sets the userObjectId of the current user
	 * 
	 * 	@param mail the mail of this user
	 *  	
	 */
	public static void setUserObjectId(String mail) {
		MongoCollection<Document> col = db.getCollection("Users");
		Document doc = col.find(Filters.eq("email",mail)).first();
		profileId = (ObjectId) doc.get("_id");
	}

	// Items-collection
	
	/**	Returns all the items in the collection Items
	 * 	@return List<Document> a list of documents, each document containing an item
	 */	
	public static List<Document> getAllItems(){
		MongoCollection<Document> col = db.getCollection("Items");
		return (List<Document>) col.find().into(
				new ArrayList<Document>());
	}


	// Henter ut alle items som matcher categoryID:
	public static List<Document> getAllItems(ObjectId categoryID){
		MongoCollection<Document> col = db.getCollection("Items");
		return (List<Document>) col.aggregate(Arrays.asList(
				Aggregates.match(Filters.eq("category_id", categoryID)))).into(
						new ArrayList<Document>());

	}

	public static List<String> getAllItemsAsStrings(){
		MongoCollection<Document> col = db.getCollection("Items");
		List<String> tom = new ArrayList<>();
		List<Document> foo = (List<Document>) col.find().into(
				new ArrayList<Document>());
		foo.stream().map(p->tom.add(p.get("Name").toString()));
		return tom;
	}

	public static ObjectId getUserOnEmail(String email) {
		Document doc = db.getCollection("Users").find(Filters.eq("email",email)).first();
		ObjectId id = new ObjectId(doc.get("_id").toString());
		return id;
	}



	// Gets all the items an user posess.
	public static List<Document> getAllUserItems(ObjectId user_id){
		// Dette er en treg query, burde benytte join isteden pÃƒÂ¥ useritems og items...

		MongoCollection<Document> col = db.getCollection("UserItems");

		List<Document> foo = (List<Document>) col.aggregate(Arrays.asList(
				Aggregates.match(Filters.eq("user_id",user_id)),
				Aggregates.lookup("Items","item_id","_id","UIwithItems"))).into(
						new ArrayList<Document>());

		ArrayList<Document> ferdig = new ArrayList<Document>();

		// Legger man navn pÃƒÂ¥ kategori inn i Items slipper man denne...
		for(Document doc : foo) {
			ArrayList<Document> embedded = (ArrayList<Document>) doc.get("UIwithItems");
			Document docen = new Document();
			docen.append("Id", doc.get("_id"));
			docen.append("Kategori",db.getCollection("Categories").find(
					Filters.eq("_id",embedded.get(0).get("category_id"))).first().get("Kategori"));
			docen.append("Item", embedded.get(0).get("Name"));
			ferdig.add(docen);
		}

		return ferdig;
	}


	public static List<Document> getAllItemsOnStringPred(String kategori){
		ObjectId id = getCategoryId(kategori);
		return getAllItems(id);

	}



	/**	Deletes an useritem from the collection UserItem
	 * 	
	 * 	@param id	the id of the useritem
	 */
	public static void deleteRowUserItems(ObjectId id) {
		MongoCollection<Document> col = db.getCollection("UserItems");
		col.deleteOne(Filters.eq("_id", id));
	}

	
	/**	Creates a new useritem for the this user, and adds it to the collection UserItems.
	 * 	Checks if user is logged in
	 * 	
	 * 	@param itemId the id of the item that is to be added to the collection UserItems
	 */	
	public static void newUserItem(ObjectId itemId) {
		MongoCollection<Document> col = db.getCollection("UserItems");
		Document foo = new Document();
		foo.append("user_id", Main.profileId);
		foo.append("item_id", itemId);
		foo.append("added", new Date());

		if(Main.profileId == null) {
			throw new IllegalStateException("Du er ikke logget inn... stor bug");
		}else {
			col.insertOne(foo);
			System.out.println("Item inserted!");

		}

	}

	
	/**	Takes in email and already encrypted password, and checks if it exists in collection Users
	 * 	
	 * 	@param email the email of the person trying to log in
	 * 	@param pw the encrypted password
	 */
	public static Document checkPwInDatabase(String email,String pw) {
		return db.getCollection("Users").find(Filters.and(Filters.eq("email",email),Filters.eq("password", pw))).first();
	}


	// Returns user ut fra felt og verdi:
	public static UserProfile getUser(String felt, String verdi) {

		try {

			// Her bÃƒÂ¸r det kjÃƒÂ¸res en hjelpemetode for ÃƒÂ¥ sjekke om verdien som kommer inn matcher med det som skal hentes ut!
			Document foo = collection.find(Filters.eq(felt,verdi)).first();


			String firstName = foo.get("firstname").toString();
			String lastName = foo.get("lastname").toString();
			String email = foo.get("email").toString();
			String password = foo.get("password").toString();
			ArrayList<String> date = ConverterClass.dateToDayMonthYear(foo.getDate("birthdate"));
			String phoneNr = foo.get("phonenumber").toString();
			String community = "Gløshaugen";
			if(foo.getString("community") != null) {
				community = foo.getString("community");
			}
			try {
				profile =  UserCreator.createUser(firstName, lastName, email,
						date.get(0), date.get(1), date.get(2), password, phoneNr,community);
				return profile;
			}
			catch (Exception e) {
				System.out.println("Det skjedde noe feil i i metoden getUser");
				throw e;
			}
		}
		catch (Exception e){
			System.out.println("User not found.."+e);

		}
		return null;
	}

	
	// Registrer en ny bruker
	public static UserProfile registerUser (String firstName, String lastName, String email,
			String birthDay, String birthMonth, String birthYear, String password, String phoneNumber,String community) {
		try {
			profile = UserCreator.createUser(firstName, lastName, email,
					birthDay, birthMonth, birthYear, password, phoneNumber,community);
			return profile;
		}
		catch (Exception e) {
			throw e;
		}
	}

	
	/**	Deletes the this.user from the database
	 *  
	 */
	public static void deleteUser() {
			try {
				String felt = "email";
				String verdi = profile.getEmail();
				Document doc = collection.find(Filters.eq(felt,verdi)).first();
				collection.deleteOne(doc);
			}

			catch (Exception e) {
				throw e;
			}
		}

	
	/**	Gets an UserDocument from the collection Users from the param email. Since email is primary key in database,
	 * 	it is considered unique.
	 * 
	 * 
	 * 	@param	email	The email of the user that is to be get
	 * 	@return Document	The Document containing all the userinformation
	 */	
	public static Document getUserDocument(String email) {
		return db.getCollection("Users").find(Filters.eq("email",email)).first();
	}

	//Updates the information of the current user
	public static void updateUser(String fName, String lName, String pw, String phoneNr) {

		try {
			BasicDBObject query = new BasicDBObject();
			query.put("email", profile.getEmail());
			BasicDBObject newDocument = new BasicDBObject();

			if (!fName.isEmpty()) {
				core.userPackage.UserEditor.setNewFirstName(fName, profile);
				newDocument.put("firstname", fName);;
			}
			if (!lName.isEmpty()) {
				core.userPackage.UserEditor.setNewLastName(lName, profile);
				newDocument.put("lastname", lName);
			}
			if (!pw.isEmpty()) {
				core.userPackage.UserEditor.setNewPassword(pw, profile);
				newDocument.put("password", pw);
			}
			if (!phoneNr.isEmpty()) {
				core.userPackage.UserEditor.setNewPhoneNr(phoneNr, profile);
				int pn = Integer.parseInt(phoneNr);
				newDocument.put("phonenumber", pn);
			}




			BasicDBObject updateObject = new BasicDBObject();
			updateObject.put("$set", newDocument);
			collection.updateOne(query, updateObject);
		}
		catch(Exception e) {
			throw e;
		}
	}

	// Inserts document into database
	public void insertDocument(Document doc,String collection) {
		setCollection(collection);
		MongoCollection<Document> foo = getCollection();
		foo.insertOne(doc);
	}

	// Returnerer typene i fÃƒÂ¸rste raden av den tilkoblede Collectionen
	public List<String> getDocumentTypes(MongoCollection<Document> col) {
		List<String> typer = col.find().first().values().stream().map(Object::getClass).map(
							p->p.toString()).collect(Collectors.toList());
		return typer;
	}



	public static boolean checkLogin(String username,String pw) {
		try {
			Document user1 = null;
			try {
				// Returns null if there's no document in mongodb that matches un,pw
				user1 = Main.checkPwInDatabase(username, core.encryptionPackage.PasswordHasher.hashPassword(pw));
			}catch(NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			// This line should be deleted - only for unencrypted passwords..
			Document user2 = Main.checkPwInDatabase(username, pw);
			
			// Tre totalt unødvendig kall til DB her imo..
			if (user1 != null || user2 != null) {
				UserProfile user = getUser("email",username);
				Main.setUserObjectId(user.getEmail());
				profile = user;
				profileId = Main.getUserOnEmail(user.getEmail());
				System.out.println("Riktig brukernavn og passord.");
				return true;
				
			}else {
				System.out.println("Passord eller brukernavn er feil");
				return false;
			}	
		}catch(MongoException e) {
			return false;
		}
	}


	public static void deleteUser(String email) {
		try{
			collection.deleteOne(Filters.eq("email",email));
		}catch (Exception e) {
			System.out.println("Brukeren finnes ikke i databasen. Krise");
		}
	}


	public static boolean userExists(String felt, String value) {
		try {
			ArrayList<String> foo = new ArrayList<>();
			collection.aggregate(Arrays.asList(Aggregates.match(Filters.eq(felt, value)),Projections.include(felt))).map(p->foo.add(p.toJson().toString()));
			if (foo.size() == 0) {
				// bruker eksisterer ikke..
				return false;
			}
			else {
				System.out.println("User does exist!");
				return true;
			}

		}
		catch (Exception e){
			System.out.println("User exists.."+e);
			return true;
		}
	}


	public static boolean checkUser(Document doc) {
		String firstName = doc.get("firstname").toString();
		String lastName = doc.get("lastname").toString();
		String email = doc.get("email").toString();
		String password = doc.get("password").toString();
		ArrayList<String> date = ConverterClass.dateToDayMonthYear(doc.getDate("birthdate"));
		String phoneNr = doc.get("phonenumber").toString();
		String community = doc.getString("community");

		//Tester UserProfile inn her
		try {
			profile = UserCreator.createUser(firstName, lastName, email,
					date.get(0), date.get(1), date.get(2), password, phoneNr,community);
		}
		catch (Exception e) {
			throw e;
		}
		
		
		if (!userExists("email",doc.get("email").toString())) {
			try {
				
				collection.insertOne(doc);
				System.out.println("Bruker lagt til");
				return true;
			}
			catch (Exception e) {
				return false;
			}
		}
		return false;

	}

	public static void locationUpdater(boolean alreadyMember,Document doc) {
		if (alreadyMember) {
			// Document updatedLocation = new Document("location",);
		}
	}


	public static ObservableList<Item> getItems(){
		ObservableList<Item> items = FXCollections.observableArrayList();
		items.add(new Item("1","snus","fresh"));
		items.add(new Item("2","snus","g3 sterk"));
		return items;


	}

    // Request-collection
    public static ObjectId getReceiverID(ObjectId _id){
        MongoCollection<Document> col = db.getCollection("Requests");

        Document foo = col.find(Filters.eq("_id",_id)).first();
        ObjectId receiverID = (ObjectId) foo.get("receiver");
        return receiverID;
    }

    public static String getRecieverUserName(ObjectId reciever) {

        MongoCollection<Document> col = db.getCollection("Users");
        Document foo = col.find(Filters.eq("_id", reciever)).first();
        String firstname = (String) foo.get("firstname");
        return firstname;


    }

    // Henter ut alle Requests som matcher reciever:
    public static List<Document> getAllRequestsRecieverID(ObjectId receiverID){
        MongoCollection<Document> col = db.getCollection("Requests");
        return (List<Document>) col.aggregate(Arrays.asList(
                Aggregates.match(Filters.eq("receiver", receiverID)))).into(
                new ArrayList<Document>());

    }



    public static List<Document> getUserHistory(ObjectId user_id, String searchWord){

        MongoCollection<Document> col = db.getCollection("ExchangeHistory");

        List<Document> foo = (List<Document>) col.aggregate(Arrays.asList(
                Aggregates.match(Filters.or(Filters.eq("receiver",user_id),Filters.eq("giver",user_id))))).into(
                new ArrayList<Document>());  //(Filters.eq("reciever",user_id),Filters.eq("giver",user_id)))));

        ArrayList<Document> ferdig = new ArrayList<Document>();

        // Legger man navn pÃƒÂ¥ kategori inn i Items slipper man denne...
        for(Document doc : foo) {
            ArrayList<Document> embedded = (ArrayList<Document>) doc.get("ExchangeHistory");
            Document docen = new Document();
            Object shareBuddy;
            int rating;
            
            if (user_id.equals(doc.get("receiver"))){

                shareBuddy = doc.get("giver");
                rating = -1;
            }
            else {
                shareBuddy = doc.get("receiver");
                rating = 1;
            }
            if (searchWord.isEmpty()) {
                docen.append("Dato", doc.get("date"));
                docen.append("Navn", db.getCollection("Users").find(
                        Filters.eq("_id", shareBuddy)).first().get("firstname"));
                docen.append("Etternavn", db.getCollection("Users").find(
                        Filters.eq("_id", shareBuddy)).first().get("lastname"));
                docen.append("+/-", rating);
                docen.append("Bytte", db.getCollection("Items").find(
                        Filters.eq("_id", doc.get("item"))).first().get("Name"));
                docen.append("Community", doc.get("community"));
                ferdig.add(docen);
            }

            else {
                MongoCollection<Document> item = db.getCollection("Items");

                List<Document> itemfoo = item.aggregate(Arrays.asList(
                        Aggregates.lookup("Categories","category_id","_id","categoriesWithItems"))).into(
                        new ArrayList<Document>());


                if (!searchWord.equals(db.getCollection("Users").find(
                        Filters.eq("_id", shareBuddy)).first().get("lastname")) & !searchWord.equals(db.getCollection("Items").find(
                        Filters.eq("_id", doc.get("item"))).first().get("Name"))){
                    continue;
                }
                else if (searchWord.equals(db.getCollection("Items").find(
                        Filters.eq("_id", doc.get("item"))).first().get("Name")))
                {
                    docen.append("Dato", doc.get("date"));
                    docen.append("Navn", db.getCollection("Users").find(
                            Filters.eq("_id", shareBuddy)).first().get("firstname"));
                    docen.append("Etternavn", db.getCollection("Users").find(
                            Filters.eq("_id", shareBuddy)).first().get("lastname"));
                    docen.append("+/-", rating);
                    docen.append("Bytte", db.getCollection("Items").find(
                            Filters.eq("_id", doc.get("item"))).first().get("Name"));
                    docen.append("Community", doc.get("community"));
                    ferdig.add(docen);
                }
                else{
                    docen.append("Dato", doc.get("date"));
                    docen.append("Navn", db.getCollection("Users").find(
                            Filters.eq("_id", shareBuddy)).first().get("firstname"));
                    docen.append("Etternavn", db.getCollection("Users").find(
                            Filters.eq("_id", shareBuddy)).first().get("lastname"));
                    docen.append("+/-", rating);
                    docen.append("Bytte", db.getCollection("Items").find(
                            Filters.eq("_id", doc.get("item"))).first().get("Name"));
                    docen.append("Community", doc.get("community"));
                    ferdig.add(docen);
                }
            }
        }
        return ferdig;
    }
    
    //Her blir det feil om community ikke er oppe og nikker i users
    //Method to add document to ExchangeHistory
    public static void addToExchangehistory (String requestId) {
    	
    	try {
        	MongoCollection<Document> requestCol = db.getCollection("Requests");
        	Document request = requestCol.find(Filters.eq("_id",new ObjectId(requestId))).first();

        	ObjectId giverId = request.getObjectId("giver");
        	ObjectId receiverId = request.getObjectId("receiver");
        	ObjectId itemId = request.getObjectId("item");
        	
        	MongoCollection<Document> usersCol = db.getCollection("Users");
        	Document receiver = usersCol.find(Filters.eq("_id",receiverId)).first();
        	String community = receiver.getString("community");
        	//String community = "CommunityTest"; //receiver.getString("community");
        	int rating = receiver.getInteger("rating", 0);
        	
        	MongoCollection<Document> exchangeHistoryCol = db.getCollection("ExchangeHistory");
        	Document exchange = new Document();
        	
        	exchange.put("giver", giverId);
        	exchange.put("receiver", receiverId);
        	exchange.put("rating", rating);
        	exchange.put("item", itemId);
        	exchange.put("date", new Date());
        	exchange.put("community", community);
        	
        	exchangeHistoryCol.insertOne(exchange);
        	
    	}
    	catch (Exception e) {
    		throw e;
    	}
    	
    }
    
    public static List<Document> getAdminHistory(String searchWord){

        MongoCollection<Document> col = db.getCollection("ExchangeHistory");

        List<Document> foo = (List<Document>) col.find().into(
                new ArrayList<Document>());
        ArrayList<Document> ferdig = new ArrayList<Document>();

        // Legger man navn pÃƒÂ¥ kategori inn i Items slipper man denne...
        for(Document doc : foo) {
            Document docen = new Document();
            if (searchWord.isEmpty()) {
                docen.append("Produkt", db.getCollection("Items").find(
                        Filters.eq("_id", doc.get("item"))).first().get("Name"));
                docen.append("Dato", doc.get("date"));
                docen.append("Community", doc.get("community"));
                ferdig.add(docen);
            }
            else {
                String community = doc.get("community").toString();
                if (!searchWord.equals(community)){
                    continue;
                }
                else{
                    docen.append("Produkt", db.getCollection("Items").find(
                            Filters.eq("_id", doc.get("item"))).first().get("Name"));
                    docen.append("Dato", doc.get("date"));
                    docen.append("Community", doc.get("community"));
                    ferdig.add(docen);

                }
            }
        }
        return ferdig;
    }

    public static List<Document> getAdminHistoryOnDate(String searchWord, LocalDate fromDate, LocalDate toDate) throws Exception{
        MongoCollection<Document> col = db.getCollection("ExchangeHistory");

        List<Document> foo = (List<Document>) col.find().into(
                new ArrayList<Document>());
        ArrayList<Document> ferdig = new ArrayList<Document>();

        // Legger man navn pÃƒÂ¥ kategori inn i Items slipper man denne...
        for(Document doc : foo) {
            Document docen = new Document();
            if (searchWord.isEmpty() & fromDate == null & toDate == null) {
                docen.append("Produkt", db.getCollection("Items").find(
                        Filters.eq("_id", doc.get("item"))).first().get("Name"));
                docen.append("Dato", doc.get("date"));
                docen.append("Community", doc.get("community"));
                ferdig.add(docen);
            }
            else {
                String community = doc.get("community").toString();
                String datoen = doc.get("date").toString();
                String fraDato = fromDate.toString();
                String tilDato = toDate.toString();

				DateFormat dateFormat = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = dateFormat.parse(datoen);

				Date datoFrom = new SimpleDateFormat("yyyy-MM-dd").parse(fraDato);

				Date datoTo = new SimpleDateFormat("yyyy-MM-dd").parse(tilDato);



                if (!searchWord.equals(community) | datoFrom.compareTo(date) > 0 | datoTo.compareTo(date) < 0){
                    continue;
                }
                else{
                    docen.append("Produkt", db.getCollection("Items").find(
                            Filters.eq("_id", doc.get("item"))).first().get("Name"));
                    docen.append("Dato", doc.get("date"));
                    docen.append("Community", doc.get("community"));
                    ferdig.add(docen);
                }
            }
        }
        return ferdig;
    }
    
  //Updates rating: + 1 for user who gives, -1 for user who gets. Har testet, den fungerer
  	public static void updateRating (ObjectId requestId) {
  		//Document giver = db.getCollection("Users").find(Filters.eq("email", "emilneby@online.no")).first(); //hardkode for testing
  		//int nyrating1 = Integer.parseInt(giver.get("rating").toString()) +1; //hardkode for testing
  		
  		
  		// "Deg"
  		BasicDBObject query1 = new BasicDBObject();
  		//query1.put("email", giver.get("email").toString());//hardkode for testing
  		query1.put("email", profile.getEmail());
  		BasicDBObject newDocument1 = new BasicDBObject();
  		
  		
  		//Giver fr +1
  		Document giver = db.getCollection("Users").find(Filters.eq("_id", profileId)).first(); 
  		int giverRating = giver.getInteger("rating") + 1;
  		
  		newDocument1.put("rating", giverRating);  		
  		BasicDBObject updateObject1 = new BasicDBObject();
		updateObject1.put("$set", newDocument1);			 
		collection.updateOne(query1, updateObject1);

  		
  		
  		
		//"Motpart"
  		//Reciever fr -1:
  		ObjectId receiverid = getReceiverID(requestId);
  		Document receiver = db.getCollection("Users").find(Filters.eq("_id", receiverid)).first(); 
  		//recieverid m vre av type ObjectId for at den skal klare  filtrere
  		int nyrating2 = receiver.getInteger("rating") -1;
  		
  		BasicDBObject query2 = new BasicDBObject();
  		query2.put("email", receiver.get("email").toString());
  		BasicDBObject newDocument2 = new BasicDBObject();
  		
  		newDocument2.put("rating", nyrating2);
  		
  		BasicDBObject updateObject2 = new BasicDBObject();
  		updateObject2.put("$set", newDocument2);			 
  		collection.updateOne(query2, updateObject2);
  		//denne metoden kan sikkert gjres mye enklere, brukte mye fra updateUser-metoden
  		//NB! Har ikke med hndtering av feil
  	
  		
  		//deleteRowRequest(requestId);
  		System.out.println("Ratingendring vellyket");
      }
//  	public static void deleteRowRequest(ObjectId id) {
//		MongoCollection<Document> col = db.getCollection("Requests");	
//		col.deleteOne(Filters.eq("_id", id));
//	}
  	public static Integer getUserRating(ObjectId id) {
  		MongoCollection<Document> col = Main.getDatabase().getCollection("Users");
  		Document doc = col.find().filter(Filters.eq("_id", id)).first();
  		if(doc.getInteger("rating") == null) {
  			return 0;
  		}else {
  			return doc.getInteger("rating");
  		}
   	}
  	
  	
    public static List<Document> getAdminCommunity(){

        MongoCollection<Document> col = db.getCollection("RequestedCommunities");

        List<Document> foo = (List<Document>) col.find().into(
                new ArrayList<Document>());
        ArrayList<Document> ferdig = new ArrayList<Document>();

        // Legger man navn pÃƒÂ¥ kategori inn i Items slipper man denne...
        for(Document doc : foo) {
            Document docen = new Document();
            docen.append("Navn på community", doc.get("Navn"));
            docen.append("Beskrivelse", doc.get("Beskrivelse"));
            ferdig.add(docen);
        }
        return ferdig;
    }

    public static void deleteCommunity(String community){
        MongoDatabase db = Main.getDatabase();
        MongoCollection<Document> col = db.getCollection("RequestedCommunities");
        try {
            Document doc = col.find(Filters.eq("Navn",new String(community))).first();
            col.deleteOne(doc);
        }
        catch (Error e) {
            System.out.println("Could not delete community");
            throw e;
        }
    }

    public static void acceptCommunity(String community){
        MongoDatabase db = Main.getDatabase();
        MongoCollection<Document> col = db.getCollection("RequestedCommunities");
        MongoCollection<Document> col2 = db.getCollection("Communities");
        try {
            Document doc = col.find(Filters.eq("Navn",new String(community))).first();
            col2.insertOne(doc);
            col.deleteOne(doc);
        }
        catch (Error e) {
            System.out.println("Could not accept community");
            throw e;
        }
    }

	public static void main(String[] args) throws ParseException {


		Main con = new Main(new ServerConnect(true));

		// Ser visst ut som man kan dele DB-instanser
		// Gjor at vi kan rydde opp i Mainfilen senere
		AdminMain.setDatabase(getDatabase());

	
    
		launch(args);


		con.closeConnection();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		  String login = "../JavaFX/logginn.fxml";
		  Parent panel = FXMLLoader.load(getClass().getResource(login));
	      Scene scene = new Scene(panel);
	      Stage stage = new Stage();
	      stage.setScene(scene);
	      stage.show();

	      stage.setOnCloseRequest(e -> {
	    	  Platform.exit();
	    	  System.out.println("Programmet ble lukket.");
	      });

		}

	
	public static void newRequestedCommunity(String comNavn, String beskrivelse) {
        MongoCollection<Document> col = db.getCollection("RequestedCommunities");
        Document foo = new Document();


        foo.append("Navn", comNavn);
        foo.append("Beskrivelse", beskrivelse);

        col.insertOne(foo);
        System.out.println("New CommunityRequest is inserted");

    }


		
		
		
}
