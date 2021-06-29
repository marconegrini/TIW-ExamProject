package it.polimi.tiw.projects.beans;

import java.sql.Date;

public class Appello {
	private Integer id;
	private Date date;
	private Course course;

	public Integer getId() {
		return id;
	}

	public String getDate() {
		return date.toString();
	}

	public Course getCourse() {
		return course;
	}

	public void setId(Integer i) {
		id = i;
	}

	public void setDate(Date d) {
		date = d;
	}

	public void setCourse(Course c) {
		course = c;
	}

}
