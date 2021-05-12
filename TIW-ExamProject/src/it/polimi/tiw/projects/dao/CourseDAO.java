package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.polimi.tiw.projects.beans.Professor;
import it.polimi.tiw.projects.beans.Student;

public class CourseDAO {
	
	private Connection con;

	public CourseDAO(Connection connection) {
		this.con = connection;
	}

	public Professor checkProfessor(String id, String usrn, String pwd) throws SQLException {
		return null;
	}
}
