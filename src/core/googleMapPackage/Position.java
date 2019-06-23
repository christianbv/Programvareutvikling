package core.googleMapPackage;

import org.bson.types.ObjectId;

public class Position {
	private ObjectId id;
	private ObjectId userid;
	private double latitude;
	private double longitude;
	private boolean isActive;

	public Position(ObjectId id, ObjectId userid, double latitude,double longitude,boolean isActive) {
		this.id = id;
		this.userid = userid;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isActive = isActive;
	}
	
	
	// Getters and setters
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getUserid() {
		return userid;
	}

	public void setUserid(ObjectId userid) {
		this.userid = userid;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	
	

}
