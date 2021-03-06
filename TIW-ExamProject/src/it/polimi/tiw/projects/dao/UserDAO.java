package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.polimi.tiw.projects.beans.User;

public class UserDAO {
	
	private Connection con;

	public UserDAO(Connection connection) {
		this.con = connection;
	}

	public User checkCredentials(String u, String p) throws SQLException {
		String query =	"SELECT  userId, username, password, role FROM users WHERE username = ? AND password = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, u);
			pstatement.setString(2, p);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					User user = new User();
					user.setId(result.getInt("userId"));
					user.setUsername(result.getString("username"));
					user.setPassword(result.getString("password"));
					user.setRole(result.getString("role"));
					return user;
				}
			}
		}
	}
}
