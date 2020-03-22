package patientFile;

import static java.lang.System.out;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a data access object for the user login
 * 
 * @author Michael Kaleve, Eric Walter
 * @version 1.1, 2019-06-17
 */
public class LoginDAO {

	/**
	 * This method validates the user input strings and returns a boolean
	 * @param user User name string
	 * @param password User password string
	 * @return Returns a string
	 */
	public static String validate(String user, String password) {

		String sOutcome = null;
		Connection con = null;
		PreparedStatement ps = null;
		Util util = new Util();

		// out.println( "validate()..." );
		if (util != null)
			util.log("validate()...");

		try {
			String sQl = "SELECT userName, password, role FROM user_login WHERE userName = ? and password = ?";

			con = util.getCon();
			ps = con.prepareStatement(sQl);
			ps.setString(1, user);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				out.println("User " + user + " logged in.");
				sOutcome = rs.getString("role");
				return sOutcome;
			}

		} catch (SQLException ex) {
			System.out.println("Login error --> " + ex.getMessage());
			return "";
		} finally {
			util.closeConnection(con);
		}
		return "";
	}
}
