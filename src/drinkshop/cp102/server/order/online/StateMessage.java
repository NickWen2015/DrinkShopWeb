package drinkshop.cp102.server.order.online;

import java.util.Set;

public class StateMessage {
	private String type;//進來,離開,聊天狀態
	// the user changing the state
	private String user;
	// total users
	private Set<String> users;

	public StateMessage(String type, String user, Set<String> users) {
		super();
		this.type = type;
		this.user = user;
		this.users = users;//目前的所有人
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Set<String> getUsers() {
		return users;
	}

	public void setUsers(Set<String> users) {
		this.users = users;
	}

}
