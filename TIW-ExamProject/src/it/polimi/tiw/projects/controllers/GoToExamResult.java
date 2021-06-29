package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.projects.beans.Appello;
import it.polimi.tiw.projects.beans.Course;
import it.polimi.tiw.projects.beans.Exam;
import it.polimi.tiw.projects.beans.Student;
import it.polimi.tiw.projects.dao.CourseDAO;
import it.polimi.tiw.projects.dao.ExamDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class GoToExamResult
 */
@WebServlet("/GoToExamResult")
public class GoToExamResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToExamResult() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException{
    	connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Student student = (Student) request.getSession().getAttribute("student");
		Integer courseId = null;
		try {
			courseId = Integer.parseInt(request.getParameter("course"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		Date appelloDate = null;
		try {
			appelloDate = Date.valueOf(request.getParameter("appello"));
		} catch (IllegalArgumentException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		Exam exam = null;
		Course course = null;
		boolean refusable = false;
		
		try {
			ExamDAO examDao = new ExamDAO(connection);
			CourseDAO courseDao = new CourseDAO(connection);
			Appello appello = courseDao.findAppello(courseId, appelloDate);
			if (appello == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Appello not found");
				return;
			}
			
			Integer examId = examDao.getExamIdByStudentAndSession(student.getId(), appello.getId());
			if(examId == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exam not found");
				return;
			} 
			
			exam = examDao.findExamById(examId.toString());
			if(exam == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exam not found");
				return;
			}
			refusable = examDao.isRefusable(exam.getGrade(), exam.getStatus());
			
			course = courseDao.findCourseById(courseId);
			if (course == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
				return;
			} 
		} catch (SQLException sqle) {
				response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in exam database extraction");
		}
		
		System.out.println("student: " + student);
		System.out.println("selected course id: " + courseId);
		System.out.println("selected appello date: " + appelloDate);
		
		String path = "/WEB-INF/ExamResult.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		ctx.clearVariables();
		ctx.setVariable("examInfo", exam);
		ctx.setVariable("courseInfo", course);
		ctx.setVariable("refusable", refusable);
		
		this.templateEngine.process(path, ctx, response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
