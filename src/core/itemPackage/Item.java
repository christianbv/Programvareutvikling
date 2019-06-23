package core.itemPackage;

public class Item {
	
	private String name; 
	
	public Item(String name) {
		
		if(validItemName(name)) {
			this.name = name;
		}
	}

	//	ta høyde for at navn ikke kan være uendelig langt? setter maks 20 karakterer
	public boolean validItemName(String navn) {
		
		if(navn.length() == 0) {
			throw new IllegalArgumentException("Navn kan ikke være et tomt felt"); 
		}
		if(navn.length() > 20){
			throw new IllegalArgumentException("Item Navn kan ikke være lengre enn 20 chars"); 
		}
		
		return true; 
		
	}
	
	public String getName() {
		return name; 
	}
	
	@Override
	public String toString() {
		return "Item [name=" + name + "]";
	}

}