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

public class StudentDAO {
	
	private Connection con;

	public StudentDAO(Connection connection) {
		this.con = connection;
	}

	public Student checkStudent(String i, String u, String p) throws SQLException {
		String query = """
				SELECT
					S.studentId, S.name, S.surname, S.email, S.corsoDiLaurea
				FROM
					students AS S,
					users AS U
				WHERE
					S.studentId = U.userId AND
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
					Student student = new Student();
					student.setId(result.getInt("studentId"));
					student.setName(result.getString("name"));
					student.setSurname(result.getString("surname"));
					student.setEmail(result.getString("email"));
					student.setBachelorCourse(result.getString("corsoDiLaurea"));
					return student;
				}
			}
		}
	}
	
	public List<Course> findCourses(Integer i) throws SQLException {
		List<Course> courses = new ArrayList<Course>();
		String query = """
				SELECT DISTINCT
					C.courseId, C.code, C.name AS c_name,
					P.professorId, P.name AS p_name, P.surname
				FROM
					courses AS C,
					professors AS P,
					appelli AS A,
					exams AS E,
					students AS S
				WHERE
					C.courseId = A.courseId AND
					P.professorId = C.professor AND
					A.appelloId = E.appelloId AND
					S.studentId = E.student AND
					S.studentId = ?
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
