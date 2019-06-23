package JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminCommunity {
    private SimpleStringProperty nameCol;
    private SimpleStringProperty descriptionCol;

    public AdminCommunity (String name, String description){
        this.nameCol = new SimpleStringProperty(name);
        this.descriptionCol = new SimpleStringProperty(description);
    }

    public String getName() {
        return nameCol.get();
    }

    public String getDescription() {
        return descriptionCol.get();
    }

    public void setName(String date) {
        this.nameCol.set(date);
    }

    public void setDescription(String lastName) {this.descriptionCol.set(lastName); }

    public StringProperty nameProperty() {
        return nameCol;
    }

    public StringProperty descriptionProperty() { return descriptionCol; }
}
