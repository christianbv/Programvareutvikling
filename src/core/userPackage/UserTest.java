package core.userPackage;

import junit.framework.TestCase;

public class UserTest extends TestCase {

	public UserTest() {
		
	}
	
	// Tester
	public void testConstructor(){
	     UserProfile user = new UserProfile("Hanne", "Olssen", "hanne.olssen@gmail.com", "12", "09", "1997", "passordetmitt1",null,null);
		 assertEquals("Hanne", user.getFirstName());
		 assertEquals("Olssen", user.getLastName());
         assertEquals("hanne.olssen@gmail.com", user.getEmail());
         assertEquals("12", user.getBirthDay());
	     assertEquals("09", user.getBirthMonth());
	     assertEquals("1997", user.getBirthYear());
	     assertEquals("passordetmitt1", user.getPassword());
	}
	
	

		
	//Method to check if the setFirstName() works like we want it to
	public void testSetFirstNameException() {
		UserProfile up = new UserProfile("Hanne", "Olssen", "hanne.olssen@gmail.com", "12", "09", "1997", "passordetmitt1",null,null);
		try {
            up.setFirstName(" ");
	        fail();
	   }
	    catch(IllegalArgumentException iae) {
            fail("Expected IllegalArgumentException, was " + iae.getClass());
		}	
	    catch(Exception e){
            fail("Expected IllegalArgumentException, was " + e.getClass());
        }
	        
	    
	}
	  
		
	//Method to check if the setLastName() works like we want it to
	public void testSetLastNameException() {
		UserProfile up = new UserProfile("Hanne", "Olssen", "hanne.olssen@gmail.com", "12", "09", "1997", "passordetmitt1",null,null);
		try {
		    up.setLastName(" ");
		    fail();
		    
		} 
		catch (IllegalArgumentException iae) {
		} 
		catch (Exception e) {
			fail("Expected IllegalArgumentException, was " + e.getClass());
	    }
	}
		
	//Method to check if the setEmail() works like we want it to
	public void testSetEmailException() {
	    UserProfile up = new UserProfile("Hanne", "Olssen", "hanne.olssen@gmail.com", "12", "09", "1997", "passordetmitt1",null,null);
	    try {
		   //no @
		   up.setEmail("hanne.olssen.gmail.com");
	       fail();
	    } 
		catch (IllegalArgumentException iae) {
        } 
        catch (Exception e) {
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        }
           
        try {
           //no .no/.com etc
           up.setEmail("hanne.olssen@gmail");
           fail();
        } catch (IllegalArgumentException iae) {
        
        } catch (Exception e) {
        
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        
        }
			   
        try {
           //nothing behind @
           up.setEmail("hanne.olssen@");
           fail();
        
        } catch (IllegalArgumentException iae) {
        
        } catch (Exception e) {
        
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        
        }
        
        try {
           //nothing before @
           up.setEmail("@gmail.com");
           fail();
        
        } catch (IllegalArgumentException iae) {
        
        } catch (Exception e) {
        
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        
        }
        try {
           //two @'s
           up.setEmail("hanne.olssen@gmail@com");
           fail();
        
        } catch (IllegalArgumentException iae) {
        
        } catch (Exception e) {
        
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        
        }
        try {
           //signs that should not be allowed in an email address
           up.setEmail("hanne.olssen@gmai!?.com");
           fail();
        
        } catch (IllegalArgumentException iae) {
        
        } catch (Exception e) {
        
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        
        }
        /*try {
           // two .'s
           up.setEmail("hanne.olssen@gmail..com");
           fail();
        
        } catch (IllegalArgumentException iae) {
        
        } catch (Exception e) {
        
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        
        }*/
        }
        
    //Method to check if the setBirthDay() works like we want it to
    public void testSetBirthDayException() {
        UserProfile up = new UserProfile("Hanne", "Olssen", "hanne.olssen@gmail.com", "12", "09", "1997", "passordetmitt1",null,null);
        try {
           //not 0
          up.setBirthDay("0");
          fail();
        } 
        catch (IllegalArgumentException iae) {
        } 
        catch (Exception e) {
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        }
        
        try {
           //not negative
              up.setBirthDay("-23");
              fail();
           } 
           catch (IllegalArgumentException iae) {
           } 
           catch (Exception e) {
        		fail("Expected IllegalArgumentException, was " + e.getClass());
           }
        try {
           //not more than there are days in any month
              up.setBirthDay("33");
              fail();
           } 
           catch (IllegalArgumentException iae) {
           } 
           catch (Exception e) {
        		fail("Expected IllegalArgumentException, was " + e.getClass());
           }
        }
        
    //Method to check if the setBirthMonth() works like we want it to
    public void testSetBirthMonthException() {
        UserProfile up = new UserProfile("Hanne", "Olssen", "hanne.olssen@gmail.com", "12", "09", "1997", "passordetmitt1",null,null);
        try {
           //not 0
          up.setBirthMonth("0");
          fail();
        } 
        catch (IllegalArgumentException iae) {
        } 
        catch (Exception e) {
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        }
        
        try {
           //not negative
              up.setBirthMonth("-23");
              fail();
           } 
           catch (IllegalArgumentException iae) {
           } 
           catch (Exception e) {
        		fail("Expected IllegalArgumentException, was " + e.getClass());
           }
        try {
           //not more than there are months in a year
              up.setBirthMonth("13");
              fail();
           } 
           catch (IllegalArgumentException iae) {
           } 
           catch (Exception e) {
        		fail("Expected IllegalArgumentException, was " + e.getClass());
           }
        }
        
    //Method to check if the setBirthYear() works like we want it to
    public void testSetBirthYearException() {
        UserProfile up = new UserProfile("Hanne", "Olssen", "hanne.olssen@gmail.com", "12", "09", "1997", "passordetmitt1",null,null);
        try {
           //not 0
          up.setBirthYear("0000");
          fail();
        } 
        catch (IllegalArgumentException iae) {
        } 
        catch (Exception e) {
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        }
        
        try {
           //not negative
              up.setBirthYear("-1995");
              fail();
           } 
           catch (IllegalArgumentException iae) {
           } 
           catch (Exception e) {
        		fail("Expected IllegalArgumentException, was " + e.getClass());
           }
        try {
           //less than 18 years ago
              up.setBirthYear("2002");
              fail();
           } 
           catch (IllegalArgumentException iae) {
           } 
           catch (Exception e) {
        		fail("Expected IllegalArgumentException, was " + e.getClass());
           }
        }
        
    public void testSetPasswordException() {
        UserProfile up = new UserProfile("Hanne", "Olssen", "hanne.olssen@gmail.com", "12", "09", "1997", "passordetmitt1",null,null);
        try {
           //Shorther than 6 letters
          up.setPassword("hei");
          fail();
        } 
        catch (IllegalArgumentException iae) {
        } 
        catch (Exception e) {
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        }
        
        try {
           //Not containing a number
           up.setPassword("hahaha");
           fail();
        
        } catch (IllegalArgumentException iae) {
        
        } catch (Exception e) {
        
        	fail("Expected IllegalArgumentException, was " + e.getClass());
        
        }
			   
			   
		
	}
	


}
