package it.polimi.tiw.projects.beans;

import java.sql.Date;

public class Appello {
	private int appelloId;
	private int courseId;
	private Date date;
	
	public int getAppelloId() {
		return appelloId;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public String getDate() {
		return date.toString();
	}
	
	public void setAppelloId(int appelloId) {
		this.appelloId = appelloId;
	}
	
	public void setCourseId(Integer id) {
		courseId = id;
	}
	
	public void setDate(Date d) {
		date = d;
	}
	
}
