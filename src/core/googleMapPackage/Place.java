package core.googleMapPackage;

import java.util.ArrayList;

public class Place {
	private String place;
	private double latitude;
	private double longitude;


	public Place(String place, double lat,double longitude) {
		this.place = place;
		this.latitude = lat;
		this.longitude = longitude;
	}

	
	// Getters og setters
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public static ArrayList<Place> getPlacesAtGlos(){
		ArrayList<Place> places = new ArrayList<Place>();
		places.add(new Place("Kjelhuset",63.418627,10.404077));
		places.add(new Place("Hangaren",63.416690,10.404802));
		places.add(new Place("Realfagskantina",63.415439,10.404752));
		places.add(new Place("Sit-Rapido",63.417627,10.404057));
		places.add(new Place("Hovedbiblioteket",63.419169,10.402320));
		places.add(new Place("Drivhuset",63.416437,10.403570));
		places.add(new Place("Lise",63.418008,10.403662));
		places.add(new Place("EL-kantina",63.417999,10.401467));
		
		return places;
	}
	
	

}
