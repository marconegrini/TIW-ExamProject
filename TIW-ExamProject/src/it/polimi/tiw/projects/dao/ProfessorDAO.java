package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.projects.beans.Course;
import it.polimi.tiw.projects.beans.Professor;
import it.polimi.tiw.projects.beans.Student;

public class ProfessorDAO {
	
	private Connection con;

	public ProfessorDAO(Connection connection) {
		this.con = connection;
	}

	public Professor checkProfessor(String i, String u, String p) throws SQLException {
		String query = """
				SELECT
					P.professorId, P.name, P.surname
				FROM
					professors AS P,
					users AS U
				WHERE
					P.professorId = U.userId AND
					U.userId = ? AND
					U.username = ? AND
					U.password = ?
				""";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, i);
			pstatement.setString(2, u);
			pstatement.setString(3, p);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Professor professor = new Professor();
					professor.setId(result.getInt("professorId"));
					professor.setName(result.getString("name"));
					professor.setSurname(result.getString("surname"));
					return professor;
				}
			}
		}
	}
	
	public List<Course> findCourses(Integer i) throws SQLException {
		List<Course> courses = new ArrayList<Course>();
		String query = """
				SELECT
					C.courseId, C.code, C.name AS c_name,
					P.professorId, P.name AS p_name, P.surname
				FROM
					professors AS P,
					courses AS C
				WHERE
					P.professorId = C.professor AND
					P.professorId = ?
				ORDER BY
					C.name ASC
				""";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, i);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Course course = new Course();
					Professor professor = new Professor();
					
					// Setting professor's info
					professor.setId(result.getInt("professorId"));
					professor.setName("p_name");
					professor.setSurname("surname");
					
					// Setting course's info
					course.setId(result.getInt("courseId"));
					course.setCode(result.getString("code"));
					course.setName(result.getString("c_name"));
					course.setProfessor(professor);
					
					// Adding course
					courses.add(course);
				}
			}
		}
		return courses;
	}
	
	public Integer findDefaultCourse(Integer i) throws SQLException {
		List<Course> courses = findCourses(i);
		return courses.size() > 0 ? courses.get(0).getId() : null;
	}
	
}
