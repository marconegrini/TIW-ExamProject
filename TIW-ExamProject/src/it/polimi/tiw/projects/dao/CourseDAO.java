package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.projects.beans.Appello;
import it.polimi.tiw.projects.beans.Course;
import it.polimi.tiw.projects.beans.Professor;
import it.polimi.tiw.projects.beans.Student;

public class CourseDAO {
	
	private Connection con;

	public CourseDAO(Connection connection) {
		this.con = connection;
	}

	//finds appelli related to the specified course
	public List<Appello> findAppelli(String courseId) throws SQLException {
		List<Appello> appelli = new ArrayList<Appello>();
		String query = "SELECT course, date FROM appelli WHERE course = ? ORDER BY date DESC";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, courseId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Appello appello = new Appello();
					appello.setCourseId(result.getInt("course"));
					appello.setDate(result.getDate("date"));
					appelli.add(appello);
				}
			}
		}
		return appelli;
	}
	
	public List<Student> findStudentsByAppello(String courseId, String appello){
		return null;
	}
}
