package JavaFX;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
	
	private  SimpleStringProperty IdCol;
	private  SimpleStringProperty NameCol;
	private  SimpleStringProperty CategoryCol;
	
	
	public Item(String id, String category, String name) {
		
		this.IdCol = new SimpleStringProperty(id);
		this.NameCol = new SimpleStringProperty(name);
		this.CategoryCol = new SimpleStringProperty(category);
	}

	public String getId() {
		return IdCol.get();
	}
	public String getKategori() {
		return CategoryCol.get();
	}
	public String getNavn() {
		return NameCol.get();
	}
	
	public void setId(String id) {
		this.IdCol.set(id);
	}
	public void setKategori(String kat) {
		this.CategoryCol.set(kat);
	}
	public void setNavn(String navn) {
		this.NameCol.set(navn);
	}
	
	public StringProperty idProperty() {
		return IdCol;
	}
	
	public StringProperty nameProperty() {
		return NameCol;
	}
	
	public StringProperty categoryProperty() {
		return CategoryCol;
	}
	
	

}
