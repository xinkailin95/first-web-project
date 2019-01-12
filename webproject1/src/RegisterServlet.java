
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class RegisterServlet
 */

@WebServlet(name = "RegisterServlet", urlPatterns = "/api/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/movieDB")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Retrieve parameter username and password from url request.
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String re_password = request.getParameter("re-password");
		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		JsonObject jsonObject = new JsonObject();

		String reg_username = "^\\w{6,12}$"; // username must be 6-12 Eng or Num
		String reg_password = "^.{6,20}$"; // password must be length of 6-20
		if (!Pattern.matches(reg_username, username)) {
			jsonObject.addProperty("status", "fail");
			jsonObject.addProperty("message", "Invalid Username!");
			out.write(jsonObject.toString());
		} else if (!Pattern.matches(reg_password, password)) {
			jsonObject.addProperty("status", "fail");
			jsonObject.addProperty("message", "Invalid Password!!");
			out.write(jsonObject.toString());
		} else if (!password.equals(re_password)) {
			jsonObject.addProperty("status", "fail");
			jsonObject.addProperty("message", "Different Password!!!");
			out.write(jsonObject.toString());
		} else {
			try {
				conn = dataSource.getConnection();
				String query = "SELECT * FROM user where username = ?";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1, username);

				rs = stmt1.executeQuery();
				if (rs.next()) {
					// username exist
					jsonObject.addProperty("status", "faul");
					jsonObject.addProperty("message", "Username is taken!");
					out.write(jsonObject.toString());
				} else {
					String sql = "insert into user(username, password) values(?, ?)";
					stmt2 = conn.prepareStatement(sql);
					stmt2.setString(1, username);
					stmt2.setString(2, password);
					stmt2.executeUpdate();
					jsonObject.addProperty("state", "success");
					jsonObject.addProperty("message", "Success!!");
					out.write(jsonObject.toString());
				}

			} catch (Exception e) {
				// write error message JSON object to output
				jsonObject = new JsonObject();
				jsonObject.addProperty("status", "fail");
				jsonObject.addProperty("message", e.getMessage());

				out.write(jsonObject.toString());
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (stmt1 != null)
						stmt1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (stmt2 != null)
						stmt2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (conn != null)
						conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
