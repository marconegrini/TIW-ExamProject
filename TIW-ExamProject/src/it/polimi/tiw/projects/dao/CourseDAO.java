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
import it.polimi.tiw.projects.beans.Exam;
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
	
	public List<Exam> findRegisteredStudents(String courseId, String appello) throws SQLException{
		List<Exam> registeredStudents = new ArrayList<Exam>();
		String query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, courseId);
			pstatement.setString(2, appello);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Student student = new Student();
					Exam exam = new Exam();
					student.setId(result.getInt("studentId"));
					student.setName(result.getString("name"));
					student.setSurname(result.getString("surname"));
					student.setEmail(result.getString("email"));
					student.setCorsoDiLaurea(result.getString("corsoDiLaurea"));
					exam.setCourseId(result.getInt("course"));
					exam.setDate(result.getDate("date"));
					exam.setStatus(Status.valueOf(result.getString("status")));
					exam.setGrade(result.getInt("grade"));
					exam.setStudent(student);
					registeredStudents.add(exam);
				}
			}
		} 
		
		for(Exam e : registeredStudents) {
			System.out.println(e.getCourseId());
			System.out.println(e.getDate());
			System.out.println(e.getStatus());
			System.out.println(e.getGrade());
			System.out.println(e.getStudent().getName());
			System.out.println(e.getStudent().getSurname());
			System.out.println(e.getStudent().getEmail());
			System.out.println(e.getStudent().getCorsoDiLaurea());
		}
		
		return registeredStudents;
	}
}
