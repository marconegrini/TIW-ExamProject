package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.projects.beans.Appello;
import it.polimi.tiw.projects.beans.Course;
import it.polimi.tiw.projects.beans.Student;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.CourseDAO;
import it.polimi.tiw.projects.dao.ProfessorDAO;
import it.polimi.tiw.projects.dao.StudentDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

@WebServlet("/GoToHomeStudent")
public class GoToHomeStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public GoToHomeStudent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Student student = (Student) request.getSession().getAttribute("student");
		
		StudentDAO sDao = new StudentDAO(connection);
		List<Course> courses = null;
		List<Appello> appelli = null;
		Integer chosenCourseId;
		CourseDAO cDao = new CourseDAO(connection);
		Course course = null;
		
		try {
			courses = sDao.findCourses(student.getId());
			
			chosenCourseId = request.getParameter("courseId") == null ?
					sDao.findDefaultCourse(student.getId()) : Integer.parseInt(request.getParameter("courseId"));
			course = cDao.findCourseById(chosenCourseId);
			
			appelli = cDao.findAppelli(chosenCourseId);
		} catch (SQLException e) {
			// throw new ServletException(e);
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in student's courses database extraction");
		}
		
		String path = "/WEB-INF/HomeStudent.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("courses", courses);
		ctx.setVariable("chosenCourse", course);
		ctx.setVariable("appelli", appelli);

		templateEngine.process(path, ctx, response.getWriter());
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
