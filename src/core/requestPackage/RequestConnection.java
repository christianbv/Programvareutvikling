package core.requestPackage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.sun.net.httpserver.Filter;

import mainPackage.*;

public class RequestConnection {
	
	
	private static MongoDatabase db = Main.getDatabase();
	private static MongoCollection<Document> col = db.getCollection("Requests");
	
	
	//GiverID will be the current user
	public static void addRequestDBInstance (String giverID, String itemID) {
		try {		
			
			Document doc = new Document();
			doc.put("giver", new ObjectId (giverID));
			doc.put("receiver", Main.profileId);
			doc.put("item", new ObjectId (itemID));
			doc.put("isAccepted", 0);
			doc.put("reqTime", new Date());
			
			col.insertOne(doc);
			System.out.println("Request added to the requestDB");
		}
		catch (Error e) {
			System.out.println("Could not add request");
			throw e;
		}
		
	}
	
	
	//Input: MongoDB-requestID and int Status (-1 = rejected, 0 = active, 1 = accepted)
	public static void changeStatus(String requestID, int status) {
		if (status < -1 || status > 1) {
			throw new IllegalArgumentException ("Invalid status input");
		}
		try {
			Document doc = col.find(Filters.eq("_id", new ObjectId(requestID))).first();
			doc.replace("isAccepted", status);
			col.findOneAndReplace(Filters.eq("_id",new ObjectId(requestID)), doc);			
		}
		catch (Error e) {
			System.out.println("Could not add request");
			throw e;
		}	
	}
	
	
	public static void deleteRequest(String requestID) {
		try {
			Document doc = col.find(Filters.eq("_id",new ObjectId(requestID))).first();
			col.deleteOne(doc);
		}
		catch (Error e) {
			System.out.println("Could not delete request");
			throw e;
		}
		
		
	}
	
	@SuppressWarnings("null")
	//returns collection of all requests where the current user is the reciever
	public static ArrayList<Document> getUsersRequests(){
		
		ArrayList<Document> requestsCol = new ArrayList<>();
		
		try {			
			AggregateIterable<Document> documents = col.aggregate(Arrays.asList(Aggregates.match(Filters.eq("reciever", Main.profileId.toString()))));
			
			for (Document document : documents) {
				requestsCol.add(document);
			}
			return requestsCol;
		}
		catch (Error e) {
			System.out.println("Could not get user requests");
			throw e;
		}
	}
	
	public static ArrayList<Document> getAllItemsAvailable(){
		MongoCollection<Document> userItems = db.getCollection("UserItems");
		return userItems.find().into(new ArrayList<>());	
	}
	
	// Metoder til "giveShare"
	
	// Henter bare ut "pending"-requests
	public static ArrayList<Document> getMyShares(ObjectId id){
		MongoCollection<Document> col = db.getCollection("Requests");
		ArrayList<Document> agg = col.aggregate(Arrays.asList(
							Aggregates.lookup("Users", "receiver", "_id","UserCol"),
							Aggregates.unwind("$UserCol"),
							Aggregates.match(
								Filters.and(
								Filters.eq("giver",id),
								Filters.eq("isAccepted",0))),
									Aggregates.lookup("Items", "item", "_id","ItemCol"),
									Aggregates.unwind("$ItemCol"),
										Aggregates.project(
												Projections.include(
														"UserCol.firstname",
														"UserCol.lastname",
														"UserCol._id",
														"UserCol.rating",
														"ItemCol.Name",
														"giver",
														"receiver",
														"reqTime",
														"isAccepted",
														"_id"
														)))).into(new ArrayList<Document>());
		return agg;
	}
	
	// - mye raskere metoder ved joining av flere tabeller!!
	// - bug: her vil man få ut sine egne produkter.
	public static ArrayList<Document> allUserItemsAggregated(){
		MongoCollection<Document> col = db.getCollection("UserItems");
		
		ArrayList<Document> agg = col.aggregate(Arrays.asList(
				Aggregates.lookup("Items", "item_id", "_id","ItemCol"),
				Aggregates.unwind("$ItemCol"),
						Aggregates.lookup("Categories", "ItemCol.category_id","_id","CatCol"),
						Aggregates.unwind("$CatCol"),
							Aggregates.lookup("Users", "user_id", "_id","UserCol"),
							Aggregates.unwind("$UserCol"),
								Aggregates.project(
										Projections.exclude(
												"UserCol.password",
												"UserCol.phonenumber",
												"UserCol.email",
												"UserCol.birthdate",
												"UserCol.time_joined",
												"CatCol._id",
												"ItemCol.category_id"
												)))).into(new ArrayList<Document>());
		
		return agg;
	}

	// Henter ut alle userItems tilhørende et community samt ikke "egne"-items
	public static ArrayList<Document> allUserItemsAggregated(ObjectId id, String community){
	MongoCollection<Document> col = db.getCollection("UserItems");	
	ArrayList<Document> agg = col.aggregate(Arrays.asList(
				Aggregates.lookup("Items", "item_id", "_id","ItemCol"),
				Aggregates.unwind("$ItemCol"),
						Aggregates.lookup("Categories", "ItemCol.category_id","_id","CatCol"),
						Aggregates.unwind("$CatCol"),
							Aggregates.lookup("Users", "user_id", "_id","UserCol"),
							Aggregates.match(Filters.and(Filters.eq("UserCol.community", community),Filters.ne("UserCol._id",id))),
							Aggregates.unwind("$UserCol"),
								Aggregates.project(
										Projections.exclude(
												"UserCol.password",
												"UserCol.phonenumber",
												"UserCol.email",
												"UserCol.birthdate",
												"UserCol.time_joined",
												"CatCol._id",
												"ItemCol.category_id"
												)))).into(new ArrayList<Document>());
		
		return agg;
	}
	
	public static ArrayList<Document> userItemsOnCategory(String kategori){
		MongoCollection<Document> col = db.getCollection("UserItems");
		
		ArrayList<Document> agg = col.aggregate(Arrays.asList(
				Aggregates.lookup("Items", "item_id", "_id","ItemCol"),
				Aggregates.unwind("$ItemCol"),
						Aggregates.lookup("Categories", "ItemCol.category_id","_id","CatCol"),
						Aggregates.match(Filters.eq("CatCol.Kategori",kategori)),
						Aggregates.unwind("$CatCol"),
							Aggregates.lookup("Users", "user_id", "_id","UserCol"),
							Aggregates.match(Filters.and(Filters.eq("UserCol.community", Main.profile.getCommunity()),Filters.ne("UserCol._id",Main.profileId))),
							Aggregates.unwind("$UserCol"),
								Aggregates.project(
										Projections.exclude(
												"UserCol.password",
												"UserCol.phonenumber",
												"UserCol.email",
												"UserCol.birthdate",
												"UserCol.time_joined",
												"CatCol._id",
												"ItemCol.category_id"
												)))).into(new ArrayList<Document>());
		
		
		return agg;
	}
	
	public static ArrayList<Document> userItemsOnItem(String item){
		MongoCollection<Document> col = db.getCollection("UserItems");
		
		ArrayList<Document> agg = col.aggregate(Arrays.asList(
				Aggregates.lookup("Items", "item_id", "_id","ItemCol"),
				Aggregates.unwind("$ItemCol"),
				Aggregates.match(Filters.eq("ItemCol.Name",item)),
						Aggregates.lookup("Categories", "ItemCol.category_id","_id","CatCol"),
						Aggregates.unwind("$CatCol"),
							Aggregates.lookup("Users", "user_id", "_id","UserCol"),
							Aggregates.match(Filters.and(Filters.eq("UserCol.community", Main.profile.getCommunity()),Filters.ne("UserCol._id",Main.profileId))),
							Aggregates.unwind("$UserCol"),
								Aggregates.project(
										Projections.exclude(
												"UserCol.password",
												"UserCol.phonenumber",
												"UserCol.email",
												"UserCol.birthdate",
												"UserCol.time_joined",
												"CatCol._id",
												"ItemCol.category_id"
												)))).into(new ArrayList<Document>());
		
		return agg;
	}
	
	//---
	
	
	// Metoder for Active-shares:
	// Henter ut alle forespørsler "jeg" har sendt til andre fra Requests og joiner med tablene (Users, Items, Kategori)
	public static ArrayList<Document> getActiveShares(ObjectId myId){
		MongoCollection<Document> col = db.getCollection("Requests");
		
		ArrayList<Document> agg = col.aggregate(Arrays.asList(
										Aggregates.lookup("Users", "giver","_id", "UserCol"),
										Aggregates.unwind("$UserCol"),
										Aggregates.match(Filters.eq("receiver",myId)),

											Aggregates.lookup("Items","item","_id", "ItemCol"),
											Aggregates.unwind("$ItemCol"),
												Aggregates.lookup("Categories", "ItemCol.category_id","_id", "CatCol"),
												Aggregates.unwind("$CatCol"),
													Aggregates.project(
														Projections.exclude(
															"UserCol.password",
															"UserCol.email",
															"UserCol.birthdate",
															"UserCol.time_joined",
															"CatCol._id",
															"ItemCol.category_id"
																)))).into(new ArrayList<Document>());
		
		return agg;
	}
	
	
	
	public static Document getUserOnObjectId(ObjectId id) {
		return AdminMain.getUser(id);
	}
 	
	public static Document getItemJoinCategories(ObjectId itemId) {
		Document ut = new Document();

		// Henter ut fra Items (Navn, KategoriId)
		MongoCollection<Document> items = db.getCollection("Items");
		Document doc = items.find().filter(Filters.eq("_id",itemId)).first();
		
		// Henter Kategori(KategoriNavn)
		ObjectId categoryId = doc.getObjectId("category_id");
		MongoCollection<Document> cats = db.getCollection("Categories");
		Document catDoc = cats.find().filter(Filters.eq("_id",categoryId)).first();
		
		ut.put("KategoriId",categoryId);
		ut.put("KategoriNavn", catDoc.get("Kategori").toString());
		ut.put("ItemId", itemId);
		ut.put("ItemNavn", doc.get("Name").toString());
		return ut;
		
	}

	public static Document getReceiver(ObjectId id) {
		MongoCollection<Document> users = db.getCollection("Users");
		return users.find(Filters.eq("_id",id)).first();	
	}


	public static String convertStatus(Integer state) {
		if (state < -1 || state > 1) throw new IllegalStateException("");
		else if(state == -1) return "Rejected";
		else if(state == 1) return "Accepted";
		else return "Pending";
	}

	
	// Henter ut alle requests som ikke er akseptert eller rejected, dvs. pending requests
	// Returns the number of requests the "giverId" has got for his product(s)
	public static Integer getNumRequests(ObjectId giverId) {
		ArrayList<Document> agg = col.aggregate(Arrays.asList(
									Aggregates.match(Filters.and(Filters.eq("giver",giverId),Filters.eq("isAccepted", 0))),
									Aggregates.count("antall")
									)).into(new ArrayList<Document>());
		if(agg.isEmpty()) return 0;
		Document doc = agg.get(0);
		return doc.getInteger("antall");
	}
	
	
}
