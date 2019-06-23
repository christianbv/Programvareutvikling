package core.googleMapPackage;

import JavaFX.Item;
import JavaFX.User;

public class UserPosition {
	private Position position;
	private Item item;
	private User user;
	

	public UserPosition(Position position, Item item, User user) {
		this.position = position;
		this.item = item;
		this.user = user;
	}


	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
