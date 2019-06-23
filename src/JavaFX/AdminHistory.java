package JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminHistory {
    private SimpleStringProperty produktCol;
    private SimpleStringProperty datoCol;
    private SimpleStringProperty communityCol;

    public AdminHistory (String produkt, String dato, String community ){
        this.produktCol = new SimpleStringProperty(produkt);
        this.datoCol = new SimpleStringProperty(dato);
        this.communityCol = new SimpleStringProperty(community);
    }

    //Getters
    public String getProdukt() {
        return produktCol.get();
    }

    public String getDato() { return datoCol.get(); }

    public String getCommunity() {
        return communityCol.get();
    }

    //Setters
    public void setProdukt(String date) {
        this.produktCol.set(date);
    }

    public void setDato(String lastName) {this.datoCol.set(lastName);}

    public void setCommunity(String community) {
        this.communityCol.set(community);
    }

    //String properties
    public StringProperty produktProperty() {
        return produktCol;
    }

    public StringProperty datoProperty() { return datoCol; }

    public StringProperty communityProperty() {
        return communityCol;
    }

}
