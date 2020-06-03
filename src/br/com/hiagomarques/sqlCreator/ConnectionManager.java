package br.com.hiagomarques.sqlCreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
	
	public static Connection get() throws SQLException {
		String url = "jdbc:postgresql://localhost/teste";
		Properties props = new Properties();
		props.setProperty("user","postgres");
		props.setProperty("password","root");
		return DriverManager.getConnection(url, props);

	}
}
