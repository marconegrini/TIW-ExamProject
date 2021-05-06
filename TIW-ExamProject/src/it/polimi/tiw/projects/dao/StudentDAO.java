package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.polimi.tiw.projects.beans.Student;

public class StudentDAO {
	
	private Connection con;

	public StudentDAO(Connection connection) {
		this.con = connection;
	}

	public Student checkStudent(Integer studentId, String usrn, String pwd) throws SQLException {
		String query =	"SELECT  studentId, name, surname, email, username FROM students WHERE studentId = ? AND username = ? AND password = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, studentId);
			pstatement.setString(2, usrn);
			pstatement.setString(3, pwd);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Student student = new Student();
					student.setId(result.getInt("studentId"));
					student.setName(result.getString("name"));
					student.setSurname(result.getString("surname"));
					student.setEmail(result.getString("email"));
					student.setUsername(result.getString("username"));
					return student;
				}
			}
		}
	}
}
