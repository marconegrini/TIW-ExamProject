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
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;

import it.polimi.tiw.projects.dao.CourseDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;
import it.polimi.tiw.projects.beans.*;
/**
 * Servlet implementation class GotToRegisteredStudents
 */
@WebServlet("/GoToRegisteredStudents")
public class GoToRegisteredStudents extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	private OrderType orderType;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToRegisteredStudents() {
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
		orderType = new OrderType();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Integer appelloId = null;
		String appello = null;
		Date appelloDate = null;
		String courseName = null;
		String sortBy = null;
		
		try {
			appello = request.getParameter("date");
			appelloDate = Date.valueOf(appello);
		} catch (IllegalArgumentException | NullPointerException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect appello date value");
			return;
		}
		
		try {
			appelloId = Integer.parseInt(request.getParameter("appelloId"));
		} catch (IllegalArgumentException | NullPointerException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect appello date value");
			return;
		}
		
		
		
		try {
			courseName = request.getParameter("courseName");
		} catch (IllegalArgumentException | NullPointerException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect course name value");
			return;
		}
		
		Order order = null;
		System.out.println(sortBy);
		try {
			sortBy = request.getParameter("sortBy");
			if(sortBy == null) {
				sortBy = "surname";
				order = Order.ASC;
			} else {
				System.out.println(sortBy.toString());
				order = orderType.getOrder(sortBy.toString());
				orderType.updateOrder(sortBy.toString());
			}
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		CourseDAO courseDao = new CourseDAO(connection);
		List<Exam> registeredStudents = null;
		try {
			registeredStudents = courseDao.findRegisteredStudents(appelloId, sortBy, order);	
			if(registeredStudents == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
				return;
			}
		} catch (SQLException sqle) {
			//sqle.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in registered students database extraction");
		}
		
		System.out.println(registeredStudents);
		String path = "/WEB-INF/RegisteredStudents.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.clearVariables();
		ctx.setVariable("registeredStudents", registeredStudents);
		ctx.setVariable("date", appelloDate);
		ctx.setVariable("courseName", courseName);
		ctx.setVariable("appelloId", appelloId);
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
