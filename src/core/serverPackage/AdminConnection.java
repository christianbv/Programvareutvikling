package core.serverPackage;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;




import java.util.logging.Level;
import java.util.logging.Logger;

/*	Logging-convention: 


The logp() methods work like the log() methods, except each method take an extra two parameters: 
The sourceClass and sourceMethod parameter.
	  
logger.logp(Level.SEVERE, "Halla loggen: {0},{1}",new Object[]{var0,var1}); - kan endre warning osv. 

*/

// Non-static class, to be called upon from different classes to initiate connection. 

public class AdminConnection {
	
	protected String username;
	protected String password;
	private MongoClient client;
	private String userId;
	
	// Creates a logger
	private final static Logger logger = Logger.getLogger(AdminConnection.class.getName()); 
	
	
	// Constructor from another class for log-in
	// returns the connection (for a query-handler)?
	
	public AdminConnection(String username, String password) {
		this.username = username;
		this.password = password; 
	}
	
	protected String getConnectionString() {
		String un = this.username;
		String pw = this.password; 
		String serverUrl = "@programvareutvikling-server-shard-00-00-qdsgr.mongodb.net";
		String portNr = "27017";
		String rest = "programvareutvikling-server-shard-00-01-qdsgr.mongodb.net:27017,"
				+ "programvareutvikling-server-shard-00-02-qdsgr.mongodb.net:27017/test?"
				+ "ssl=true&replicaSet=Programvareutvikling-" 
				+ "Server-shard-0&authSource=admin&retryWrites=true";
		return "mongodb://"+un+":"+pw+serverUrl+":"+portNr+","+rest;
	}
	
	public boolean initiateConnection() {
		
		System.out.println("Prøver opprette en tilkobling..");
		logger.log(Level.INFO, "Prøver opprette en tilkobling...");

		
		
		try {
			MongoClient mongoClient = MongoClients.create(getConnectionString());
			this.client = mongoClient;
			
			logger.log(Level.FINE, "Tilkoblet til databasen!");
			System.out.println("Databasen er tilkoblet.. ");
			return true;
			
			
		}catch (Exception e){ // Denne bør fikses på... 
			
			logger.log(Level.WARNING, "Klarte ikke logge på! Forventet Admin-user, fikk: Uname = {0}, PW = {1}. Feilen skjedde i: {2}", 
					new Object[] {this.username, this.password,AdminConnection.class.getName()});
			System.out.println("Klarte ikke opprette en tilkobling.. Er du sikker på at du har logget inn med admin-user? ");
			
		}
		return false;	
	}
	
	public void terminateConnection() {
		logger.log(Level.FINE, "Terminerer tilkoblingen for: {0} i klassen: {1}", new Object[] {this.username, AdminConnection.class.getName()});
		this.client.close();
	}
	
}
