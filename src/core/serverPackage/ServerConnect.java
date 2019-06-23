package core.serverPackage;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.session.ServerSession;

public class ServerConnect {
	private boolean admin;
	private MongoClient client;
	private MongoDatabase db;
	
	// Creates a logger
	private final static Logger logger = Logger.getLogger(ServerConnect.class.getName()); 
	// Burde lage en handler her for å handle alle loggene (skrive til fil eller konsoll eller hva som er ønskelig). 
	
	// denne skal kalles fra andre klasser. Skal bare kunne gjøres én gang?
	public ServerConnect(boolean admin) {
		this.admin = admin;
		newConnection();
	}
	
	public MongoDatabase getDataBase() {
		return this.db;
	}
	
	public MongoClient getClient() {
		return this.client;
	}
	
	private void newConnection() {
		System.out.println("Prøver opprette en tilkobling..");
		logger.log(Level.INFO, "Prøver opprette en tilkobling...");

		
		
		try {
			MongoClient mongoClient = MongoClients.create(getConnectionString());
			this.client = mongoClient;
			com.mongodb.session.ClientSession derp = mongoClient.startSession();

			this.db = mongoClient.getDatabase("PU");
			
			logger.log(Level.FINE, "Tilkoblet til databasen PU!");
			System.out.println("Databasen er tilkoblet.. ");
			
			
		}catch (Exception e){ // Denne bør fikses på... 
			logger.log(Level.WARNING, "Tilkoblingsetablering feilet! Feilen skjedde i: {0}. Grunnet: {1}",
					new Object[] {ServerConnect.class.getName(),e.getCause()});
			
			//shitty log
			System.out.println("Klarte ikke opprette en tilkobling.. Er du sikker på at IP er whitelistet?");
			
		}
		
		
	}
	
	private String getConnectionString() {
		String un = "FOOBAR";
		String pw = "FOOBAR";
		
		if(this.admin) {
			 un = "FOOBAR";
			 pw = "FOOBAR";
		}
		// Dette er meget dårlig programmeringspraksis og svært lite sikkert, API-keys kan ikke ligge slik i klartekst. 
		// Forbedringspotensiale til senere sprinter!
		String serverUrl = "@programvareutvikling-server-shard-00-00-qdsgr.mongodb.net";
		String portNr = "27017";
		String rest = "programvareutvikling-server-shard-00-01-qdsgr.mongodb.net:27017,"
				+ "programvareutvikling-server-shard-00-02-qdsgr.mongodb.net:27017/test?"
				+ "ssl=true&replicaSet=Programvareutvikling-" 
				+ "Server-shard-0&authSource=admin&retryWrites=true";
		
		logger.log(Level.FINE,"Henter ut API-key fra klassen: {0}",ServerConnect.class.getName());
		return "mongodb://"+un+":"+pw+serverUrl+":"+portNr+","+rest;
	}
	
	public void terminateConnection() {
		logger.log(Level.FINE,"Terminerer tilkoblingen i klassen: {0}",ServerConnect.class.getName());
		System.out.println("Terminerer tilkoblingen til DB..");
		this.client.close();
	}

}
