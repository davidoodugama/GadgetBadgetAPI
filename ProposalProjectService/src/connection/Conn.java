package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conn {

	public Connection connect() {
		Connection con = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/project?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"root", "");

			System.out.println("Successfully Connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	
}
