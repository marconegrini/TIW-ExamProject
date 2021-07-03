package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.projects.beans.Status;

import it.polimi.tiw.projects.beans.Appello;
import it.polimi.tiw.projects.beans.Course;
import it.polimi.tiw.projects.beans.Exam;
import it.polimi.tiw.projects.beans.Professor;
import it.polimi.tiw.projects.beans.Student;
import it.polimi.tiw.projects.beans.Order;

public class CourseDAO {
	
	private Connection con;

	public CourseDAO(Connection connection) {
		this.con = connection;
	}
	
	public Course findCourseById(Integer id) throws SQLException {
		Course course = new Course();
		String query = """
				SELECT
					C.courseId, C.code, C.name AS c_name,
					P.professorId, P.name AS p_name, P.surname
				FROM
					courses AS C, professors AS P
				WHERE
					C.professor = P.professorId AND C.courseId = ?
				""";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, id);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) return null;
				result.next();
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

				result.last();
				if (result.getRow() > 1) return null; // more than one record found, invalid query
			}
		}
		return course;
		
	}
	
	/**
	 * Returns an {@link Appello Appello} given its course and its date.
	 * 
	 * @param courseId	The id of the course to search for.
	 * @param date		The date of the exam to search for.
	 * @return	An instance of {@link Appello Appello} if everything is ok; null otherwise.
	 * @throws SQLException	If query fails.
	 */
	public Appello findAppello(Integer courseId, Date date) throws SQLException {
		Appello appello = new Appello();
		String query = """
				SELECT
					A.appelloId, A.date,
					C.courseId, C.code, C.name AS c_name,
					P.professorId, P.name AS p_name, P.surname
				FROM
					appelli AS A, courses AS C, professors AS P
				WHERE
					P.professorId = C.professor AND
					C.courseId = A.courseId AND
					C.courseId = ? AND
					A.date = ?
				""";
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			pstatement.setInt(1, courseId);
			pstatement.setString(2, date.toString());
			try (ResultSet result = pstatement.executeQuery()) {
				if (!result.isBeforeFirst()) return null;
				result.next();
				
				Professor professor = new Professor();
				Course course = new Course();
				
				// Setting professor's info
				professor.setId(result.getInt("professorId"));
				professor.setName(result.getString("p_name"));
				professor.setSurname(result.getString("surname"));
				
				// Setting course's info
				course.setId(result.getInt("courseId"));
				course.setCode(result.getString("code"));
				course.setName(result.getString("c_name"));
				course.setProfessor(professor);
				
				// Setting appello's info
				appello.setId(result.getInt("appelloId"));
				appello.setDate(result.getDate("date"));
				appello.setCourse(course);
				
				result.last();
				if (result.getRow() > 1) return null; // more than one record found, invalid query
			}
		}
		return appello;
	}

	//finds appelli related to the specified course
	public List<Appello> findAppelli(Integer i) throws SQLException {
		List<Appello> appelli = new ArrayList<Appello>();
		String query = """
				SELECT
					A.appelloId, A.date,
					C.courseId, C.code, C.name AS c_name,
					P.professorId, P.name AS p_name, P.surname
				FROM
					appelli AS A, courses AS C, professors AS P
				WHERE
					P.professorId = C.professor AND
					C.courseId = A.courseId AND
					C.courseId = ?
				ORDER BY
					A.date DESC
				""";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, i);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Professor professor = new Professor();
					Course course = new Course();
					Appello appello = new Appello();
					
					// Setting professor's info
					professor.setId(result.getInt("professorId"));
					professor.setName(result.getString("p_name"));
					professor.setSurname(result.getString("surname"));
					
					// Setting course's info
					course.setId(result.getInt("courseId"));
					course.setCode(result.getString("code"));
					course.setName(result.getString("c_name"));
					course.setProfessor(professor);
					
					// Setting appello's info
					appello.setId(result.getInt("appelloId"));
					appello.setDate(result.getDate("date"));
					appello.setCourse(course);
					
					// Adding the appello to the list
					appelli.add(appello);
				}
			}
		}
		return appelli;
	}
	
	public List<Exam> findRegisteredStudents(Integer appelloId, String sortBy, Order order) throws SQLException{
		List<Exam> registeredStudents = new ArrayList<Exam>();
		String query = """
				SELECT
					E.examId, E.status, E.grade,
					A.appelloId, A.date,
					C.courseId, C.code, C.name AS c_name,
					P.professorId, P.name AS p_name, P.surname AS p_surname,
					S.studentId, S.name AS s_name, S.surname AS s_surname, S.email, S.corsoDiLaurea
				FROM
					exams AS E,
					students AS S,
					appelli AS A,
					courses AS C,
					professors AS P
				WHERE
					P.professorId = C.Professor AND
					C.courseId = A.courseId AND
					A.appelloId = E.appelloId AND
					S.studentId = E.student AND
					A.appelloId = ?
				""";
		
		query += switch(sortBy) {
			case "studentId" -> " ORDER BY S.studentId " + order.name();
			case "surname" -> " ORDER BY S.surname " + order.name();
			case "name" -> " ORDER BY S.name " + order.name();
			case "email" -> " ORDER BY S.email " + order.name();
			case "corsoDiLaurea" -> " ORDER BY S.corsoDiLaurea " + order.name();
			case "grade" -> " ORDER BY E.grade " + order.name();
			case "status" -> " ORDER BY E.status " + order.name();
			default -> "";
		};
			
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, appelloId.toString());
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Professor professor = new Professor();
					Course course = new Course();
					Student student = new Student();
					Appello appello = new Appello();
					Exam exam = new Exam();
					
					// Setting student's info
					student.setId(result.getInt("studentId"));
					student.setName(result.getString("s_name"));
					student.setSurname(result.getString("s_surname"));
					student.setEmail(result.getString("email"));
					student.setBachelorCourse(result.getString("corsoDiLaurea"));
					
					// Setting professor's info
					professor.setId(result.getInt("professorId"));
					professor.setName(result.getString("p_name"));
					professor.setSurname(result.getString("p_surname"));
					
					// Setting course's info
					course.setId(result.getInt("courseId"));
					course.setCode(result.getString("code"));
					course.setName(result.getString("c_name"));
					course.setProfessor(professor);
					
					// Setting appello's info
					appello.setId(result.getInt("appelloId"));
					appello.setDate(result.getDate("date"));
					appello.setCourse(course);
					
					// Setting exam's info
					exam.setId(result.getInt("examId"));
					exam.setAppello(appello);
					exam.setStatus(Status.valueOf(result.getString("status")));
					exam.setGrade(result.getString("grade"));
					exam.setStudent(student);

					// Adding exam
					registeredStudents.add(exam);
				}
			}
		} 
		
		System.out.println(sortBy + " " + order + " order performed");
		for(Exam e : registeredStudents) {
			Student s = e.getStudent();
			System.out.println(s.getName());
			System.out.println(s.getSurname());
			System.out.println(s.getBachelorCourse());
		}
		return registeredStudents;
	}
	
	
}
