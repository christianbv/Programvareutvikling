package mainPackage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


public class AdminMain {
	
	
	private static MongoDatabase db = null;
	
	// Returns the currently connected db
	public static MongoDatabase getDatabase() {
		return db;
	}
	
	protected static void setDatabase(MongoDatabase database) {
		db = database;
	}
	
	public static List<Document> getAllUsers(){
		MongoCollection<Document> col = db.getCollection("Users");
		return col.find().into(new ArrayList<Document>());
	}

	public static void deleteUser(ObjectId obj) {
		try {
			db.getCollection("Users").deleteOne(Filters.eq("_id",obj));
			System.out.println("Bruker slettet.");
			
		}catch(Exception e) {
			System.out.println("Klarte ikke slette bruker i AdminMain..");
			System.out.println(e.getCause());
		}
	}

	public static void updateUser(Document doc) {
		System.out.println("Oppdaterer bruker");
		MongoCollection<Document> col = db.getCollection("Users");
		col.replaceOne(Filters.eq("_id",doc.get("_id")), doc);
	}
	
	public static Document getUser(ObjectId id) {
		return db.getCollection("Users").find(Filters.eq("_id", id)).first();
		
	}
	
	
	
	
	
	
	
	
	
	
	// hjelpemetoder
	
	// @returns dd-MM-yyyy
	public static String dateToString(Date date) {
		String dateformat = new SimpleDateFormat("dd-MM-yyyy").format(date);
		return dateformat;
	}
	
	//@PARAM = dd-MM-yyyy
	public static Date stringToDate(String string) throws ParseException {
		try {
			System.out.println(string);
			Date dato = new SimpleDateFormat("dd-MM-yyyy").parse(string);
			return dato;
		}catch(Exception e){
			System.out.println("Feil i stringToDate, AdminMain");
		}
		return null;
	}
	
	
	public static LocalDate dateToLocalDate(Date date) {
		return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	public static Date localdateToDate(LocalDate localdate) {
		return java.sql.Date.valueOf(localdate);
	}
	
	

}
