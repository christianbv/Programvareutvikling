package core.userPackage;
import core.serverPackage.*;

public class UserEditor {
	
	//Missing all logic to connect the methods to the database	
	public static void setNewEmail(String email, UserProfile p) {
		try {
			p.setEmail(email);
		}
		catch (Exception e){
			throw e;
			}
	}
	public static void setNewFirstName(String fName, UserProfile p) {
		try {
			p.setFirstName(fName);
		}
		catch (Exception e){
			throw e;
			}
	}
	public static void setNewLastName(String lName, UserProfile p) {
		try {
			p.setLastName(lName);
		}
		catch (Exception e){
			throw e;
			}
	}
	public static void setNewPassword(String pw, UserProfile p) {
		try {
			p.setPassword(pw);
		}
		catch (Exception e){
			throw e;
			}
	}
	public static void setNewPhoneNr (String phoneNr, UserProfile p) {
		try {
			p.setPassword(phoneNr);
		}
		catch (Exception e) {
			throw e;
		}
	}
}
