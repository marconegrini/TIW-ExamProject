package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.projects.beans.Appello;
import it.polimi.tiw.projects.beans.Exam;
import it.polimi.tiw.projects.beans.Status;
import it.polimi.tiw.projects.beans.Student;

public class ExamDAO {

	private Connection con;

	public ExamDAO(Connection connection) {
		this.con = connection;
	}

	public Exam findExamById(String examId) throws SQLException {
		Exam exam = new Exam();
		String query =	"SELECT S.studentId, S.name, S.surname, S.email, S.corsoDiLaurea, A.courseId, A.appelloId, A.date, E.examId, E.status, E.grade FROM exams AS E, students AS S, appelli AS A  WHERE E.student = S.studentId AND E.appelloId = A.appelloId AND E.examId = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, examId);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Student student = new Student();
					Appello appello = new Appello();
					
					student.setId(result.getInt("studentId"));
					student.setName(result.getString("name"));
					student.setSurname(result.getString("surname"));
					student.setEmail(result.getString("email"));
					student.setCorsoDiLaurea(result.getString("corsoDiLaurea"));
					
					appello.setAppelloId(result.getInt("appelloId"));
					appello.setCourseId(result.getInt("courseId"));
					appello.setDate(result.getDate("date"));
					
					exam.setExamId(result.getInt("examId"));
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
	 * @throws SQLException	If query fails.
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
	
	public void verbalizza(Integer appelloId) throws SQLException {
		String query = "UPDATE exams SET status = 'VERBALIZZATO' WHERE appelloId = ? AND (status = 'PUBBLICATO' OR STATUS = 'RIFIUTATO')";
		try (PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setInt(1, appelloId);
			pstatement.executeUpdate();
		}
	}
}
