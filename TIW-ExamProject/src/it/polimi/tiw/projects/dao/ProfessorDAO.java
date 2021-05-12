package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.polimi.tiw.projects.beans.Professor;
import it.polimi.tiw.projects.beans.Student;

public class ProfessorDAO {
	
	private Connection con;

	public ProfessorDAO(Connection connection) {
		this.con = connection;
	}

	public Professor checkProfessor(String usrn, String pwd) throws SQLException {
		String query =	"SELECT  professorId, name, surname FROM professors WHERE profUser = ? AND profPass = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, usrn);
			pstatement.setString(2, pwd);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Professor professor = new Professor();
					professor.setId(result.getInt("professorId"));
					professor.setName(result.getString("name"));
					professor.setSurname(result.getString("surname"));
					return professor;
				}
			}
		}
	}
}
