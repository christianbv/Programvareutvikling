package JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


// Dette er kun en hjelpeklasse for å kunne laste inn brukere til en tabell!
public class User {
	
	private SimpleStringProperty IdCol;
	private SimpleStringProperty FirstNameCol;
	private SimpleStringProperty SurNameCol;
	private SimpleStringProperty EmailCol;
	private SimpleStringProperty PhoneCol;
	private SimpleStringProperty BirthdayCol;
	private SimpleStringProperty RatingCol;
	private SimpleStringProperty TimeCol;

	public User(String id, String fn, String sn, String mail,String phone, String birth, String rating,String timeJoined) {
		this.IdCol = new SimpleStringProperty(id);
		this.FirstNameCol = new SimpleStringProperty(fn);
		this.SurNameCol = new SimpleStringProperty(sn);
		this.EmailCol = new SimpleStringProperty(mail);
		this.PhoneCol = new SimpleStringProperty(phone);
		this.BirthdayCol = new SimpleStringProperty(birth);
		this.RatingCol = new SimpleStringProperty(rating);
		this.TimeCol = new SimpleStringProperty(timeJoined);
	}
	
	// Getters:
	
	public String getId() {
		return IdCol.get();
	}
	
	public String getFirstName() {
		return FirstNameCol.get();
	}
	
	public String getSurName() {
		return SurNameCol.get();
	}
	
	public String getEmail() {
		return EmailCol.get();
	}
	
	public String getPhone() {
		return PhoneCol.get(); 
	}
	
	public String getBirthday() {
		return BirthdayCol.get();
	}
	
	public String getRating() {
		return RatingCol.get();
	}
	
	public String getTimeJoined() {
		return TimeCol.get();
	}
	
	// Setters:
	
	public void setId(String id) {
		this.IdCol.set(id);
	}
	
	public void setFirstName(String id) {
		this.FirstNameCol.set(id);
	}
	
	public void setSurName(String id) {
		this.SurNameCol.set(id);
	}
	
	public void setPhone(String id) {
		this.PhoneCol.set(id);
	}
	
	public void setEmail(String id) {
		this.EmailCol.set(id);
	}
	
	public void setBirthday(String id) {
		this.BirthdayCol.set(id);
	}
	
	public void setRating(String id) {
		this.RatingCol.set(id);
	}
	
	public void setTimeJoined(String t) {
		this.TimeCol.set(t);
	}
	
	// StringProperty
	
	public StringProperty idProperty() {
		return IdCol;
	}

	public StringProperty firstnameProperty() {
		return FirstNameCol;
	}
	
	public StringProperty surnameProperty() {
		return SurNameCol;
	}
	
	public StringProperty phoneProperty() {
		return PhoneCol;
	}
	
	public StringProperty emailProperty() {
		return EmailCol;
	}
	
	public StringProperty birthdayProperty() {
		return BirthdayCol;
	}
	
	public StringProperty ratingProperty() {
		return RatingCol;
	}
	
	public StringProperty timeProperty() {
		return TimeCol;
	}
}
