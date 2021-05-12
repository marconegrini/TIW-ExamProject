package it.polimi.tiw.projects.beans;

public class User {
	
	private String username;
	private String password;
	private Role role;


	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role.toString();
	}
	
	public String getPassword() {
		return password;
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
