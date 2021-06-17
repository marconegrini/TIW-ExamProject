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

	public Exam findExamById(Integer examId) throws SQLException {
		Exam exam = new Exam();
		String query =	"SELECT S.studentId, S.name, S.surname, S.email, S.corsoDiLaurea, A.courseId, A.appelloId, A.date, E.examId, E.status, E.grade FROM exams AS E, students AS S, appelli AS A  WHERE E.student = S.studentId AND E.appelloId = A.appelloId AND E.examId = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, examId.toString());
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
}
