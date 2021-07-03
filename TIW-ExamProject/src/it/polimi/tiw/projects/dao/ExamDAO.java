package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import it.polimi.tiw.projects.beans.Appello;
import it.polimi.tiw.projects.beans.Course;
import it.polimi.tiw.projects.beans.Exam;
import it.polimi.tiw.projects.beans.Professor;
import it.polimi.tiw.projects.beans.Status;
import it.polimi.tiw.projects.beans.Student;

public class ExamDAO {

	private Connection con;

	public ExamDAO(Connection connection) {
		this.con = connection;
	}

	public Exam findExamById(String examId) throws SQLException {
		Exam exam = new Exam();
		String query = """
				SELECT
					S.studentId, S.name AS s_name, S.surname AS s_surname, S.email, S.corsoDiLaurea,
					A.appelloId, A.date,
					C.courseId, C.code, C.name AS c_name,
					P.professorId, P.name AS p_name, P.surname AS p_surname,
					E.examId, E.status, E.grade
				FROM
					exams AS E, students AS S, appelli AS A, courses AS C, professors AS P
				WHERE
					E.examId = ? AND S.studentId = E.student AND A.appelloId = E.appelloId AND C.courseId = A.courseId AND P.professorId = C.professor
				""";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, examId);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Student student = new Student();
					Professor professor = new Professor();
					Course course = new Course();
					Appello appello = new Appello();
					
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
				}
			}
		}
		
		return exam;
	}
	
	/**
	 * Returns an {@link Exam Exam} given its student and its appello.
	 * 
	 * @param studentId	The student who took the exam.
	 * @param appelloId	The appello in which the student took the exam.
	 * @return	The exam id if everything's ok, null otherwise.
	 * @throws SQLException	If the query fails.
	 */
	public Integer getExamIdByStudentAndSession(Integer studentId, Integer appelloId) throws SQLException {
		Integer id = null;
		String query = "SELECT E.examId from exams AS E WHERE E.student = ? AND E.appelloId = ?";
		
		try (PreparedStatement pstatement = con.prepareStatement(query)) {
			pstatement.setInt(1, studentId);
			pstatement.setInt(2, appelloId);
			try (ResultSet result = pstatement.executeQuery()) {
				if (!result.isBeforeFirst()) return null;
				result.next();
				id = result.getInt("examId");
				result.last();
				if (result.getRow() > 1) return null; // more than one record found, invalid query
			}
		}
		return id;
	}
	
	public boolean isRefusable(String grade, String status) {
		return status.toUpperCase().equals("PUBBLICATO") && !(
				grade.toUpperCase().equals("ASSENTE") ||
				grade.toUpperCase().equals("RIMANDATO") ||
				grade.toUpperCase().equals("RIPROVATO"));
	}
	
	public void insertGrade(String grade, Integer examId) throws SQLException{
		String query = "UPDATE exams SET grade = ?, status = 'INSERITO' WHERE examId = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setString(1, grade);
			pstatement.setInt(2, examId);
			pstatement.executeUpdate();
		}
	}
	
	public void pubblica(Integer appelloId) throws SQLException {
		String query = "UPDATE exams SET status = 'PUBBLICATO' WHERE appelloId = ? AND status = 'INSERITO'";
		try (PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setInt(1, appelloId);
			pstatement.executeUpdate();
		}
	}
	
	public void verbalizza(Integer i) throws SQLException {
		String createReport = "INSERT INTO reports (created_at) VALUES (?)";
		PreparedStatement s1 = null;
		String queryReportPK = "SELECT LAST_INSERT_ID() FROM reports";
		Statement s2 = null;
		String updateRefused = "UPDATE exams SET grade = 'RIMANDATO' WHERE appelloId = ? AND status = 'RIFIUTATO'";
		PreparedStatement s3 = null;
		String updateStatus = "UPDATE exams SET status = 'VERBALIZZATO', reportId = ? WHERE appelloId = ? AND (status = 'PUBBLICATO' OR STATUS = 'RIFIUTATO')";
		PreparedStatement s4 = null;
		
		// disabilito l'applicazione automatica delle modifiche
		con.setAutoCommit(false);
		
		try {
			// (prepared) statement 1
			s1 = con.prepareStatement(createReport);
			Timestamp created_at = Timestamp.valueOf(LocalDateTime.now());
			s1.setTimestamp(1, created_at);
			s1.executeUpdate();
			
			// statement 2 deve essere il primo eseguito
			s2 = con.createStatement();
			ResultSet rs = s2.executeQuery(queryReportPK);
			rs.next();
			Integer reportId = rs.getInt(1);

			// (prepared) statement 3
			s3 = con.prepareStatement(updateRefused);
			s3.setInt(1, i);
			s3.executeUpdate();

			// (prepared) statement 1
			s4 = con.prepareStatement(updateStatus);
			s4.setInt(1, reportId);
			s4.setInt(2, i);
			s4.executeUpdate();
			
			// applico le modifiche al database
			con.commit();
		} catch (SQLException e) {
			// se qualcosa è andato storto ripristino lo stato precedente
			con.rollback();
			//throw e;
		} finally {
			// riabilito le modifiche automatiche e chiudo tutti gli statement (non avendo fatto try with resource)
			con.setAutoCommit(true);
			if (s1 != null) {
				try {
					s1.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if (s2 != null) {
				try {
					s2.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if (s3 != null) {
				try {
					s3.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if (s4 != null) {
				try {
					s4.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
	}
	
	public void rifiuta(Integer examId) throws SQLException {
		String query = "UPDATE exams SET status = 'RIFIUTATO' WHERE examId = ? AND status = 'PUBBLICATO'";
		try (PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setInt(1, examId);
			pstatement.executeUpdate();
		}
	}
}
