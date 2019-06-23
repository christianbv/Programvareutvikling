package mainPackage;

import java.util.ArrayList;
import java.util.Date;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import core.userPackage.*;

public class ConverterClass {
	
	@SuppressWarnings("deprecation")
	public static ArrayList<String> dateToDayMonthYear(Date date){
		ArrayList<String> dateList = new ArrayList<>();	
		
		dateList.add("" + date.getDate()); //day
		dateList.add("" + (date.getMonth() + 1)); //month +1 to get correct index
		dateList.add("" + (date.getYear() +1900)); //year +  1900 to get correct format
		
		return dateList;
	}
	public static Document userProfileToDocument (UserProfile user) {
		String email = user.getEmail();
		MongoCollection<Document> foo = Main.getCollection();
		try {
			Document doc = foo.find(Filters.eq("email", email)).first();
			return doc;
		}
		catch (Error e) {
			return null;
		}
		
		
		
	}
	

}
