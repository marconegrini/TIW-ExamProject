package it.polimi.tiw.projects.beans;

import java.sql.Date;

public class Exam {
	private Student student;
	private Integer courseId;
	private Date date;
	private Status status;
	private Integer grade;
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	
	public Student getStudent() {
		return this.student;
	}
	
	public Integer getCourseId() {
		return this.courseId;
	}
	
	public String getDate() {
		return this.date.toString();
	}
	
	public String getStatus() {
		return this.status.toString();
	}
	
	public Integer getGrade() {
		return this.grade;
	}
	
	
}
