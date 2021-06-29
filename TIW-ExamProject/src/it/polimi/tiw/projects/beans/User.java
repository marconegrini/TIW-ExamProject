package it.polimi.tiw.projects.beans;

public class User {
	
	private int id;
	private String username;
	private String password;
	private Role role;

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role.toString();
	}
	
	public void setId(Integer i) {
		id = i;
	}

	public void setUsername(String u) {
		username = u;
	}
	
	public void setPassword(String p) {
		password = p;
	}

	public void setRole(String r) {
		role = Enum.valueOf(Role.class, r.toUpperCase());
	}

}
