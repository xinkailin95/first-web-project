
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * Servlet implementation class MainServlet
 */
@WebServlet(name = "/MainServlet", urlPatterns = "/api/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MainServlet() {
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
//		int paging = Integer.parseInt(request.getParameter("paging"));

		PrintWriter out = response.getWriter();

		Connection conn = null;
		PreparedStatement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
//		Statement stmt4 = null;
		Statement stmt5 = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			String query = "SELECT * FROM ratings ORDER BY rating DESC LIMIT 0, 20";
			stmt = conn.prepareStatement(query);
//			stmt.setInt(1, 20); // for later use if apply for different page
//			stmt.setInt(2, 0);
			
			stmt1 = conn.createStatement();
    		stmt2 = conn.createStatement();
    		stmt3 = conn.createStatement();
//    		stmt4 = conn.createStatement();
    		stmt5 = conn.createStatement();
    		
			rs = stmt.executeQuery();

			JsonArray jsonArray = new JsonArray();
			
			while (rs.next()) {
				String movieId = rs.getString("movieId");
				ResultSet movies = stmt1.executeQuery("SELECT * from movies where id='"+movieId+"'");
    			ResultSet genres_in_movies = stmt3.executeQuery("SELECT * from genres_in_movies where movieId='"+movieId+"'");
    			ResultSet stars_in_movies = stmt5.executeQuery("SELECT * from stars_in_movies where movieId='"+movieId+"'");
    			String starId = "";
    			if(stars_in_movies.next()) {
    				starId = stars_in_movies.getString("starId");
    			}
//    			ResultSet stars = stmt4.executeQuery("SELECT * from stars where id='"+starId+"'");
//    			String starName = "";
//    			if(stars.next()) {
//    				starName = stars.getString("name");
//    			}
    			int genreId = 0;
    			if(genres_in_movies.next()) {
    				genreId = genres_in_movies.getInt("genreId");
    			}
    			ResultSet genres = stmt2.executeQuery("SELECT * from genres where id='"+genreId+"'");
    			String genreName = "";
    			if(genres.next()) {
    				genreName = genres.getString("name");
    			}
    			String movieTitle = "";
    			int movieYear = 0;
    			String movieDirector = "";
    			if(movies.next()) {
    				movieTitle = movies.getString("title");
    				movieYear = movies.getInt("year");
    				movieDirector = movies.getString("director");
    			}
    			float rating = rs.getFloat("rating");

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", movieId);
				jsonObject.addProperty("title", movieTitle);
				jsonObject.addProperty("year", movieYear);
				jsonObject.addProperty("director", movieDirector);
				jsonObject.addProperty("genres", genreName);
				jsonObject.addProperty("ratings", rating);
				jsonArray.add(jsonObject);
				
				movies.close();
    			genres.close();
    			genres_in_movies.close();
//    			stars.close();
    			stars_in_movies.close();
			}
			out.write(jsonArray.toString());
		} catch (Exception e) {
			// TODO: handle exception
			JsonObject jsonObject = new JsonObject();
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
				if (stmt3 != null)
					stmt3.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
//			try {
//				if (stmt4 != null)
//					stmt4.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			try {
				if (stmt5 != null)
					stmt5.close();
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
