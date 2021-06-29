package it.polimi.tiw.projects.beans;

public class Student {
	
	private Integer id;
	private String name;
	private String surname;
	private String email;
	private String bachelorCourse;
	

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getBachelorCourse() {
		return bachelorCourse;
	}
	
	public void setId(Integer i) {
		id = i;
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
		
	public void setBachelorCourse(String bc) {
		bachelorCourse = bc;
	}

}
