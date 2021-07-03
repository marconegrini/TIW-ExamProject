package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.projects.beans.Exam;
import it.polimi.tiw.projects.dao.ExamDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class Pubblica
 */
@WebServlet("/Pubblica")
public class Pubblica extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Pubblica() {
        super();
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
		doPost(request, response);
		// response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Only POST requests allowed here");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ExamDAO examDao = new ExamDAO(connection);
		Integer appelloId = null;
		Date appelloDate = null;
		String courseName = null;
		
		try {
			appelloId = Integer.parseInt(request.getParameter("appelloId"));
			appelloDate = Date.valueOf(request.getParameter("date"));
			courseName = request.getParameter("courseName");
		} catch (IllegalArgumentException | NullPointerException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter value");
			return;
		}
		
		try {
			examDao.pubblica(appelloId);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Database failure while updating grade");
			return;
		}
	
		// POST-REDIRECT-GET
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToRegisteredStudents?date=" + appelloDate + "&appelloId=" + appelloId.toString() + "&courseName=" + courseName;
		response.sendRedirect(path);
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
