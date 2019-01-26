
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
 * Servlet implementation class singleMovie
 */
@WebServlet(name = "/singleMovie", urlPatterns = "/api/movie")
public class SingleMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SingleMovie() {
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
		String title = request.getParameter("title");
		PrintWriter out = response.getWriter();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		JsonObject jsonObject = new JsonObject();
		

		try {
			conn = dataSource.getConnection();
			String query = "SELECT m.title, m.year, m.director, s.name star from movies m, stars_in_movies sm, stars s where m.title = '"
					+ title + "' and sm.movieId = m.id and sm.starId = s.id";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			String stars = "";
			while (rs.next()) {
//				stars += rs.getString("star") + ", ";
				stars += "<a name=\"stars\" href=\"#\" style=\"text-decoration: none;\">" + rs.getString("star") + "</a>, ";
				if (rs.isLast()) {
					stars = stars.substring(0, stars.length()-2);
					String movieTitle = rs.getString("title");
					int movieYear = rs.getInt("year");
					String movieDirector = rs.getString("director");

					jsonObject.addProperty("title", movieTitle);
					jsonObject.addProperty("year", movieYear);
					jsonObject.addProperty("director", movieDirector);
					jsonObject.addProperty("star", stars);
				}
			}
			out.write(jsonObject.toString());
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
