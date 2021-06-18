package it.polimi.tiw.projects.beans;

public class Course {
	
	private int courseId;
	private String code;
	private String name;
	private Professor professor;
	
	public int getProfId() {
		return professor.getId();
	}
	
	public String getProfName() {
		return professor.getName();
	}
	
	public String getProfSurname() {
		return professor.getSurname();
	}

	public int getCourseId() {
		return courseId;
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

	public void setCourseId(Integer i) {
		courseId = i;
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
