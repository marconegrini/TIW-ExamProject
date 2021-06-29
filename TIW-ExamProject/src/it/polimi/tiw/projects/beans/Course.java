package it.polimi.tiw.projects.beans;

public class Course {	
	
	private Integer id;
	private String code;
	private String name;
	private Professor professor;

	public Integer getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public Professor getProfessor() {
		return professor;
	}
	
	public void setId(Integer i) {
		id = i;
	}
	
	public void setCode(String c) {
		code = c;
	}

	public void setName(String n) {
		name = n;
	}
	
	public void setProfessor(Professor p) {
		professor = p;
	}
	
}
