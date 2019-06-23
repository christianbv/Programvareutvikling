package core.userPackage;

public class UserCreator {
	
	//Constructor:
	public static UserProfile createUser(String firstName, String lastName, String email, 
			String birthDay, String birthMonth, String birthYear, String password, String phoneNr, String community) {
		
		try {
			UserProfile p1 = new UserProfile (firstName, lastName, email, 
					birthDay, birthMonth, birthYear, password, phoneNr,community);
			return p1;
			}
		
		catch (Exception e){
			throw e;
			}
		
	}
}