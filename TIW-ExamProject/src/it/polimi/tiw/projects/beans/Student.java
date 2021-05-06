package it.polimi.tiw.projects.beans;

public class Student {
	
	private int studentId;
	private String name;
	private String surname;
	private String email;
	private String username;
	

	public int getId() {
		return studentId;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setId(int i) {
		studentId = i;
	}

	public void setName(String n) {
		name = n;
	}

	public void setSurname(String s) {
		surname = s;
	}
	
	public void setEmail(String e) {
		email = e;
	}
	
	public void setUsername(String u) {
		username = u;
	}

}
