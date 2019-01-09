
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class SingleStarServlet
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    // Create a dataSource which registered in web.xml
    @Resource(name = "jdbc/movieDB")
    private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// Retrieve parameter username and password from url request.
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		JsonObject jsonObject = null;

		 try {
		 	// Get a connection from dataSource
		 	conn = dataSource.getConnection();
		 	// Construct a query with parameter represented by "?"
		 	String query = "SELECT * from accounts as s where s.username = ? and s.password = ?";
		 	// Declare our statement
		 	stmt = conn.prepareStatement(query);
		 	// Set the parameter represented by "?" in the query to the id we get from url,
		 	// num 1 indicates the first "?" in the query
		 	stmt.setString(1, username);
		 	stmt.setString(2, password);
		 	// Perform the query
		 	rs = stmt.executeQuery();

		 	// Create a JsonObject based on the data we retrieve from rs
		 	jsonObject = new JsonObject();
		 	// Iterate through each row of rs
		 	if (rs.next()) {
		 		// set this user into the session
	            request.getSession().setAttribute("user", new User(username));
//	            String userName = (String) session.getAttribute("user"); // to read the value
	            
//		 		String un = rs.getString("username");
//		 		String pw = rs.getString("password");
		 		jsonObject.addProperty("status", "success");
		 		jsonObject.addProperty("message", "success");
//		 		jsonObject.addProperty("username", un);
//		 		jsonObject.addProperty("password", pw);
		 		out.write(jsonObject.toString());
		 	}else{
		 		jsonObject.addProperty("status", "fail");
		 		jsonObject.addProperty("message", "Account does not exit!!");
		 		out.write(jsonObject.toString());
		 	}
		 } catch (Exception e) {
		 	// write error message JSON object to output
		 	jsonObject = new JsonObject();
		 	jsonObject.addProperty("status", "fail");
		 	jsonObject.addProperty("message", e.getMessage());

		 	out.write(jsonObject.toString());
		 }finally{
			 try{
				if(rs!=null) rs.close();
			}catch (Exception e){
				e.printStackTrace();
			}
			try{
				if(stmt!=null) stmt.close();
			}catch (Exception e){
				e.printStackTrace();
			}
			try{
				if(conn!=null) conn.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		 }

		out.close();
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
