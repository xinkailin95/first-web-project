
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SingleStar
 */
@WebServlet(name = "/SingleStar", urlPatterns = "/api/star")
public class SingleStar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SingleStar() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Resource(name = "jdbc/movieDB")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String star = request.getParameter("star");
		PrintWriter out = response.getWriter();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		JsonObject jsonObject = null;
		JsonArray jsonArray = null;
		try {
			conn = dataSource.getConnection();
			String query = "SELECT m.title,m.year,m.director,s.name star,s.birthYear FROM movies m,stars s,stars_in_movies sm WHERE s.name='"
					+ star + "' AND s.id=sm.starId AND sm.movieId=m.id LIMIT 20;";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			// if a star play multiply movies
			while (rs.next()) {
				String movieTitle = rs.getString("title");
				int movieYear = rs.getInt("year");
				String movieDirector = rs.getString("director");
				int birthYear = rs.getInt("birthYear");
				
				jsonArray = new JsonArray();
				jsonObject = new JsonObject();
				jsonObject.addProperty("title", movieTitle);
				jsonObject.addProperty("year", movieYear);
				jsonObject.addProperty("director", movieDirector);
				jsonObject.addProperty("star", star);
				jsonObject.addProperty("birthYear", birthYear);
				jsonArray.add(jsonObject);
			}
			out.write(jsonArray.toString());
		} catch (Exception e) {
			// TODO: handle exception
			jsonObject = new JsonObject();
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
				if (stmt != null)
					stmt.close();
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
