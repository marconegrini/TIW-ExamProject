package it.polimi.tiw.projects.beans;

import java.sql.Date;

public class Appello {
	private int courseId;
	private Date date;
	
	public int getCourseId() {
		return courseId;
	}
	
	public String getDate() {
		return date.toString();
	}
	
	public void setCourseId(Integer id) {
		courseId = id;
	}
	
	public void setDate(Date d) {
		date = d;
	}
	
}
