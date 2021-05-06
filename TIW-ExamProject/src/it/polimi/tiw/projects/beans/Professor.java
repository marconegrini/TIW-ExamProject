package it.polimi.tiw.projects.beans;

public class Professor {
	
	private int professorId;
	private String name;
	private String surname;
	private String username;

	public int getId() {
		return professorId;
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
	
	public void setId(int i) {
		professorId = i;
	}

	public void setName(String n) {
		name = n;
	}

	public void setSurname(String s) {
		surname = s;
	}
	
	public void setUsername(String u) {
		username = u;
	}

}
