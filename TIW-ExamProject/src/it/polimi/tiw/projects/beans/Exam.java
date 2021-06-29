package it.polimi.tiw.projects.beans;

public class Exam {
	private Integer id;
	private Student student;
	private Appello appello;
	private Status status;
	private String grade;
	
	public Integer getId() {
		return id;
	}
	
	public Student getStudent() {
		return student;
	}
	
	public Appello getAppello() {
		return appello;
	}
	
	public String getStatus() {
		return status.toString();
	}
	
	public String getGrade() {
		return grade;
	}
	
	public void setId(Integer i) {
		id = i;
	}
	
	public void setStudent(Student s) {
		student = s;
	}
	
	public void setAppello(Appello a) {
		appello = a;
	}
	
	public void setStatus(Status s) {
		status = s;
	}
	
	public void setGrade(String g) {
		grade = g;
	}
	
}
