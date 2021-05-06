package it.polimi.tiw.projects.beans;

public class User {
	
	private Integer userId;
	private String username;
	private String password;
	private Role role;

	public Integer getId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role.toString();
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setId(int i) {
		userId = i;
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
