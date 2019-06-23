package JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class History {
    private SimpleStringProperty dateCol;
    private SimpleStringProperty nameCol;
    private SimpleStringProperty lastnameCol;
    private SimpleStringProperty ratingCol;
    private SimpleStringProperty shareCol;
    private SimpleStringProperty communityCol;

    public History (String date, String name, String lastname, String rating, String share, String community ){
        this.dateCol = new SimpleStringProperty(date);
        this.nameCol = new SimpleStringProperty(name);
        this.lastnameCol = new SimpleStringProperty(lastname);
        this.ratingCol = new SimpleStringProperty(rating);
        this.shareCol = new SimpleStringProperty(share);
        this.communityCol = new SimpleStringProperty(community);
    }

    //Getters
    public String getDate() {
        return dateCol.get();
    }

    public String getName() {
        return nameCol.get();
    }

    public String getLastname() { return lastnameCol.get(); }

    public String getRating() {
        return ratingCol.get();
    }

    public String getShare() {
        return shareCol.get();
    }

    public String getCommunity() {
        return communityCol.get();
    }

    //Setters
    public void setDate(String date) {
        this.dateCol.set(date);
    }

    public void setName(String name) {
        this.nameCol.set(name);
    }

    public void setLastName(String lastName) {this.lastnameCol.set(lastName);}

    public void setRating(String rating) {
        this.ratingCol.set(rating);
    }

    public void setShare(String share) {
        this.shareCol.set(share);
    }

    public void setCommunity(String community) {
        this.communityCol.set(community);
    }

    //String properties
    public StringProperty dateProperty() {
        return dateCol;
    }

    public StringProperty nameProperty() {
        return nameCol;
    }

    public StringProperty lastnameProperty() { return lastnameCol; }

    public StringProperty ratingProperty() {
        return ratingCol;
    }

    public StringProperty shareProperty() {
        return shareCol;
    }

    public StringProperty communityProperty() {
        return communityCol;
    }
}
