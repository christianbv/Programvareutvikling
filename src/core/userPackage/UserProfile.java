package core.userPackage;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.itemPackage.Item;

import java.util.Calendar;
import java.util.Collection;

public class UserProfile {
	
	/**
	 * 	@author Borger
	 * 	@author Christian
	 * 	@author Hanne
	 */
	
	private String firstName;
	private String lastName;
	private String email;
	private int birthDay;
	private int birthMonth;
	private int birthYear;
	private String password; //Need encryption logic
	private String [] location; //[Longtitude, Latitude]
	private int rating;
	private Set <Item> items;
	private int phoneNr;
	
	private String community;
	// Need constraints for length of variables
	
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	
	
	
	//Constructor
	//Names: non-empty and start with capital  letter
	//Birth date specs: day: DD, month: MM, year: YYYY
	//Password minimum 7 char and contain a digit
	
	protected UserProfile (String firstName, String lastName, String email, 
			String birthDay, String birthMonth, String birthYear, 
			String password, String phoneNr,String community) {
		
		//Testing that the name contains characers, and starts with upper case
		if (nameTester(firstName) && nameTester(lastName)){
			this.firstName = firstName;
			this.lastName = lastName;
		}
		if (emailTester(email)) { //Testing that the e-mail adress is valid
			this.email = email;
		}
		if (dateTest (birthDay, birthMonth, birthYear)) { //Testing for valid age and numerical input
			this.birthDay = Integer.parseInt(birthDay);
			this.birthMonth = Integer.parseInt(birthMonth);
			this.birthYear = Integer.parseInt(birthYear);
		}
		if (passwordTester(password)) {
			this.setPassword(password);
		}
		// ny kode:
		if(phoneTester(phoneNr)) {
			this.phoneNr = Integer.parseInt(phoneNr);
		}
		this.rating = 0; //Initial value for the rating
		this.community = community;
	}
	
	
	
	
	
	//*** Main test classes***

	/**	Tests is email is valid, based on a regex for emails.
	 * 	
	 * 	@param emailStr	The email to be tested, as a string
	 * 	@return			true if email is valid, throws new IllegalArgumentException if email is invalid
	 */	
	private static boolean emailTester(String emailStr) {
		       Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
		       if (!matcher.find()) {
		    	   throw new IllegalArgumentException("Invalid e-mail");
		       }
		        return true;
		}
	
	
	/**	Checks if password is valid, i.e. that it contains atleast a digit and that its length > 5. 
	 * 
	 * 	@param	pw	the password that is to be checked, as a string
	 * 	@return	 	returns true if password is valid, throws new illegalargumentexception if not. 
	 */
	private boolean passwordTester(String pw) {
		if (pw.length() < 6) {
			throw new IllegalArgumentException("Invalid password. It must be longer than 5 letters..");
		}
		if (!(containsDigit(pw))) {
			throw new IllegalArgumentException("Invalid password. It must contain an integer..");
		}
		return true;	
	}

	

	/**	Checks the name of the new user. Checks if the name contains valid characters based on the string "expression". 
	 *	Checks that the name is not longer/shorter than 30/2 chars and that it starts with an uppercase. 
	 *
	 *	@param name the name of the person to be checked (can be surname or lastname).
	 *	@return 	returns true if name is valid, throws an illegalargumentexception with the appropriate desc. if not. 
	 * 
	 */
	private boolean nameTester(String name) {	
		String expression = "^[\\sÆØÅæøåa-zA-Z'-]*$";
		
		if (!name.matches(expression)) {
			throw new IllegalArgumentException("Invalid name");
		}
	
		if (name.isEmpty() || name==" ") {
			throw new IllegalArgumentException("Name cannot be empty");
		}
		if (!Character.isUpperCase(name.charAt(0))) {
			throw new IllegalArgumentException("Invalid name, must begin with an uppercase letter");
		}
		if (name.length() > 30) {
			throw new IllegalArgumentException("Invalid name. The name is too long.");
		}
		if(name.length() < 2) {
			throw new IllegalArgumentException("Invalid name. The name is too short..");
		}
		
		return true;
	}
	
	
	
	
	/**	A tester that checks whether or not the age is valid and that the params are on a valid form (dd.mm.yyyy). 
	 * 	
	 * 	@param dayString 	a string on the form dd
	 * 	@param monthString	a string on the form mm
	 * 	@param yearString 	a string on the form yyyy
	 * 	@return				returns true if the date is valid, throws an illegalargumentexception with appropriate desc. if not.
	 */	
	private boolean dateTest (String dayString , String monthString, String yearString) {
		//stringToInt converts String to int, returns -1 if input is non-nummerical
		int day = stringToInt (dayString); 
		int month = stringToInt (monthString);
		int year = stringToInt (yearString);
		
		//Calendar.MONTH has 0 index -> January = 0 -> added +1 at every step to get the right index
		if (day < 1 || month <1 || year < 1900) {
			throw new IllegalArgumentException("Invalid date of birth");
		}
		if (year > (Calendar.getInstance().get(Calendar.YEAR))) {
			throw new IllegalArgumentException("Invalid date of birth");
		}
		if (month > 12) {
			throw new IllegalArgumentException("Invalid date of birth");
		}
		
		//nr of days in month
		List<Integer> thirties = Arrays.asList( 4, 6, 9, 11); //months with 30 days
		List<Integer> thirtyOnes = Arrays.asList( 1, 3, 5, 7, 8, 10, 12); //months with 31 days
		if (thirtyOnes.contains(month)) {
			if (day > 31) {
				throw new IllegalArgumentException("Invalid date of birth");
			}
		}
		else if (thirties.contains(month)) {
			if (day > 30) {
				throw new IllegalArgumentException("Invalid date of birth");
			}
		}
		else { //February
			if (day > 29) {
				throw new IllegalArgumentException("Invalid date of birth");
			}
		}
		//Test of age. Throws exception for users <18 years old
		if ((Calendar.getInstance().get(Calendar.YEAR)-year)< 18) {
			throw new IllegalArgumentException("User not old enough");
		}
		else if ((Calendar.getInstance().get(Calendar.YEAR)-year)== 18) {
			if (Calendar.getInstance().get(Calendar.MONTH) + 1 < month) {
				throw new IllegalArgumentException("User not old enough");
			}
			else if (Calendar.getInstance().get(Calendar.MONTH) + 1 == month) {
				if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < day) {
					throw new IllegalArgumentException("User not old enough");
				}
			}
		}
		
		
		
		return true;
	}
	
	
	
	/** Checks whether or not a phonenumber contains only digits and has exactly length 8. 
	 * 
	 * @param phoneNumber	the number that is to be checked
	 * @return				returns true if phonenumber is valid, throws illegalargumentexception if not
	 */
	private boolean phoneTester(String phoneNumber) {
		if(phoneNumber.matches("[0-9]+") && phoneNumber.length() == 8) {
			return true;
		}else {
			throw new IllegalArgumentException("PhoneNumber is invalid. 8 digits only!");
		}
	}
	
	
	
	
	//** Help classes **
	
	// Returns true if input string contains only letters
	private boolean isAlpha(String name) {
	    char[] chars = name.toCharArray();

	    for (char c : chars) {
	        if(!(Character.isLetter(c) || Character.isUnicodeIdentifierPart(c))) {
	            return false;
	        }
	    }
	    return true;
	}
	
	// Converts String to int, returns -1 if input is non-nummerical
	private int stringToInt (String num) {
		try {
			int output =  Integer.parseInt(num);
			return output;
		}
		catch (Exception e) {
			System.out.println("Date not numerical");
		}
		return -1;
	}
		
	
	// Checks if String contains a digit
	public final boolean containsDigit(String s) {
	    boolean containsDigit = false;

	    if (s != null && !s.isEmpty()) {
	        for (char c : s.toCharArray()) {
	            if (containsDigit = Character.isDigit(c)) {
	                break;
	            }
	        }
	    }

	    return containsDigit;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Getters and setters: 
	
	public String getFirstName() {
		return firstName;
	}
	protected void setFirstName(String firstName) {
		if (nameTester(firstName)) {
			this.firstName = firstName;
		}
	}
	public String getLastName() {
		return lastName;
	}
	protected void setLastName(String lastName) {
		if (nameTester(lastName)) {
			this.lastName = lastName;
		}
	}
	public String getEmail() {
		
		return email;
	}
	protected void setEmail(String email) {
		if(emailTester(email)) {
			this.email=email;
		}	
	}
	public String getPassword() {
		return password;
	}
	
	protected void setPassword(String password) {
		if (passwordTester(password)) {
			/*Den krypteringen her fører til bugs lengre ned
			BasicCrypto c1 = new BasicCrypto();
			String encPw = c1.encrypt(password);
			
			this.password = encPw;
			*/
			this.password=password;
		}
		
	}
	public String[] getLocation() {
		return location;
	}
	protected void setLocation(String[] location) {
		this.location = location;
	}
	public int getRating() {
		return rating;
	}
	public void increaseRating() {
		this.rating ++;
	}
	public void decreaseRating() {
		this.rating --;
	}
	public String getBirthDay() {
		return "" + birthDay;
	}
	protected void setBirthDay(String birthDay) {
		if (dateTest(birthDay, this.getBirthMonth(), this.getBirthYear())) {
			this.birthDay = Integer.parseInt(birthDay);
		}
	}
	public String getBirthMonth() {
		return "" + birthMonth;
	}
	protected void setBirthMonth(String birthMonth) {
		if (dateTest(this.getBirthDay(),birthMonth, this.getBirthYear())) {
			this.birthMonth = Integer.parseInt(birthMonth);
		}
	}
	public String getBirthYear() {
		return "" + birthYear;
	}
	protected void setBirthYear(String birthYear) {
		if (dateTest(this.getBirthDay(),this.getBirthMonth(), birthYear)) {
			this.birthYear = Integer.parseInt(birthYear);
		}
	}
	public void addItem (Item item) {
		items.add(item);
	}
	public void removeItem(Item item) {
		items.remove(item);
	}
	
	
	public void setPhoneNr(String phoneNr) {
		try {
			int number = Integer.parseInt(phoneNr);
			if ( number > 0) {
				this.phoneNr = number;
			}
			else {
				this.phoneNr = 0;
			}
		}
		catch (Error e) {
			throw e;
		};
	}
	
	public int getPhoneNr() {
		return phoneNr;
	}
	

	
	// Community:	
	public void setCommunity(String community) {
		this.community = community;
	}
	
	public String getCommunity() {
		return this.community;
	}
	

	// Converts the param UserProfile to a String
	private static String toString (UserProfile p) {
		String output = "";
		output += "Name: " + p.getFirstName() + " " +  p.getLastName() +  "\n";
		output += "E-mail: " +  p.getEmail() + "\n";
		output += "Birth date: " + p.getBirthDay() + "." + p.getBirthMonth()
		+ "." + p.getBirthYear() + "\n";
		return output;
		
	}
}