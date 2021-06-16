package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

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
	
	public List<Exam> findRegisteredStudents(String courseId, String appello, String sortBy, String order) throws SQLException{
		List<Exam> registeredStudents = new ArrayList<Exam>();
		String query = null;
		if(order.equals("ASC")) {
			switch(sortBy) {
			case "studentId":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY studentId ASC";
				break;
			case "surname":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY surname ASC";
				break;
			case "name":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY name ASC";
				break;
			case "email":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY email ASC";
				break;
			case "corsoDiLaurea":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY corsoDiLaurea ASC";
				break;
			case "grade":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY grade ASC";
				break;
			case "status":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY status ASC";
				break;
			}
		} else {
			switch(sortBy) {
			case "studentId":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY studentId DESC";
				break;
			case "surname":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY surname DESC";
				break;
			case "name":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY name DESC";
				break;
			case "email":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY email DESC";
				break;
			case "corsoDiLaurea":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY corsoDiLaurea DESC";
				break;
			case "grade":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY grade DESC";
				break;
			case "status":
				query = "SELECT studentId, name, surname, email, corsoDiLaurea, course, date, status, grade FROM exams, students  WHERE student = studentId AND course = ? AND date = ? ORDER BY status DESC";
				break;
			}
		}
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
		
		System.out.println(sortBy + " " + order + " order performed");
		for(Exam e : registeredStudents) {
			System.out.println(e.getName());
			System.out.println(e.getSurname());
			System.out.println(e.getCorsoDiLaurea());
		}
		return registeredStudents;
	}
	
	
}
