package JavaFX;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


// Dette er kun en hjelpeklasse for å kunne laste inn brukere til en tabell!
public class NumberedRequest {
	
	private SimpleStringProperty NrCol;
	private SimpleStringProperty IdCol;
	private SimpleStringProperty FirstNameCol;
	private SimpleStringProperty SurNameCol;
	private SimpleStringProperty RatingCol;
	private SimpleStringProperty CategoryCol;
	private SimpleStringProperty ProductCol;
	private SimpleStringProperty ItemIdCol;
	
	
	private SimpleStringProperty StatusCol;
	private SimpleIntegerProperty PhoneCol;
	private SimpleStringProperty DateCol;
	private SimpleStringProperty NameCol;
	
	// To konstruktører med vilje!
	public NumberedRequest (String nr, String id, String fn, String sn, String rat, String cat, String itemId, String prod) {
		this.NrCol = new SimpleStringProperty(nr);
		this.IdCol = new SimpleStringProperty(id);
		this.FirstNameCol = new SimpleStringProperty(fn);
		this.SurNameCol = new SimpleStringProperty(sn);
		this.RatingCol = new SimpleStringProperty(rat);
		this.CategoryCol = new SimpleStringProperty(cat);
		this.ProductCol = new SimpleStringProperty(prod);
		this.ItemIdCol = new SimpleStringProperty(itemId);
	}
	/*
	public Request (String id, String fn, String sn, String rat, String cat, String itemId, String prod,Integer phone) {
		this(id,fn,sn,rat,cat,itemId,prod);

		System.out.println("er i 2 konstruktør");
		this.PhoneCol = new SimpleIntegerProperty(phone);
	}
	public Request2(String id, String name,String rat,String cat, String produkt, String date,String status,Integer phone  ) {
		this(id,name.split(" ")[0],name.split(" ")[1],rat,cat,"foo",produkt);
		
		StatusCol = new SimpleStringProperty(status);
		PhoneCol = new SimpleIntegerProperty(phone);
		DateCol = new SimpleStringProperty(date);
		NameCol = new SimpleStringProperty(name);
	}
	// Gjør det slik for å unngå duplisering av konstruktører - forferdelig dårlig kode
	public Request2(String id,String fn, String sn, String produkt, String date, String status,String foo,String userId) {
		this.IdCol = new SimpleStringProperty(id);
		this.FirstNameCol = new SimpleStringProperty(fn);
		this.SurNameCol = new SimpleStringProperty(sn);
		this.DateCol = new SimpleStringProperty(date);
		this.StatusCol = new SimpleStringProperty(status);
		this.ProductCol = new SimpleStringProperty(produkt);
		this.ItemIdCol = new SimpleStringProperty(userId);
	}
	*/
	
	//Getters:
	
	
	public String getNr() {
		return NrCol.get();
	}
	public String getId() {
		return IdCol.get();
	}

	public String getFirstName() {
		return FirstNameCol.get();
	}
	
	public String getSurName() {
		return SurNameCol.get();
	}

	public String getRating() {
		return RatingCol.get();
	}

	public String getCategory() {
		return CategoryCol.get();
	}

	public String getProduct() {
		return ProductCol.get();
	}
	
	public String getItemId() {
		return ItemIdCol.get();
	}

	public Integer getPhone() {
		return PhoneCol.get();
	}
	
	public String getStatus() {
		return StatusCol.get();
	}
	
	public String getDate() {
		return DateCol.get();
	}
	
	public String getName() {
		return NameCol.get();
	}
	//Setters:

	public void setId(String id) {
		this.IdCol.set(id);
	}
	
	public void setFirstName(String id) {
		this.FirstNameCol.set(id);
	}
	
	public void setSurName(String id) {
		this.SurNameCol.set(id);
	}

	public void setRating(String id) {
		this.RatingCol.set(id);
	}
	
	public void setCategory(String id) {
		this.CategoryCol.set(id);
	}
	public void setProduct(String id) {
		this.ProductCol.set(id);
	}
	public void setItemId(String itemid) {
		this.ItemIdCol.set(itemid);
	}
	
	public void setPhoneCol(Integer phone) {
		this.PhoneCol.set(phone);
	}
	
	public void setStatusCol(String string) {
		this.StatusCol.set(string);
	}
	
	public void setNameCol(String name) {
		this.NameCol.set(name);
	}
	
	public void setDateCol(String date) {
		this.DateCol.set(date);
	}
	
	//StringProperty:
	public StringProperty nrProperty() {
		return NrCol;
	}
	
	public StringProperty idProperty() {
		return IdCol;
	}
	
	public StringProperty firstnameProperty() {
		return FirstNameCol;
	}
	
	public StringProperty surnameProperty() {
		return SurNameCol;
	}
	
	public StringProperty ratingProperty() {
		return RatingCol;
	}
	public StringProperty categoryProperty() {
		return CategoryCol;
	}
	public StringProperty productProperty() {
		return ProductCol;
	}
	public StringProperty itemidProperty() {
		return ItemIdCol;
	}
	public IntegerProperty phoneProperty() {
		return PhoneCol;
	}
	
	public StringProperty statusProperty() {
		return StatusCol;
	}
	
	public StringProperty dateProperty() {
		return DateCol;
	}
	public StringProperty nameProperty() {
		return NameCol;
	}

}
