
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
import com.mysql.fabric.xmlrpc.base.Array;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet(name = "/SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServlet() {
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
		String like = request.getParameter("like");
		String genres_type = request.getParameter("genre_type"); // * for regular search
		String search_type = request.getParameter("search_type"); // default title
		
		String[] genreString = genres_type.split(" ");
		String g_type = "";
		for (int i = 0; i < genreString.length; i++) {
			g_type += "or g.name = '" + genreString[i] + "' ";
		}
		g_type = g_type.substring(3);
		
		PrintWriter out = response.getWriter();
		if (search_type.equals("title")) {
			if (genres_type.equals("*")) {
				// for general search on nav
				out.write(defaultTitle(like).toString());
			} else {
				// for advance search
				out.write(genresTitle(like, g_type).toString());
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

	private JsonArray defaultTitle(String like) {
		Connection conn = null;
		Statement stmt = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		Statement stmt3 = null;
//		Statement stmt4 = null;
		Statement stmt5 = null;
		ResultSet rs = null;
		JsonArray jsonArray = new JsonArray();

		try {
			conn = dataSource.getConnection();
			// limit the search data to 60
			String query = "SELECT * FROM movies WHERE title LIKE '" + like + "%' ORDER BY title limit 60";
//			String query = "SELECT * FROM movies WHERE title LIKE 'a%' ORDER BY title limit ?, 20";

			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
			stmt2 = conn.createStatement();
			stmt3 = conn.createStatement();
			stmt5 = conn.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String movieId = rs.getString("id");
				String movieTitle = rs.getString("title");
				int movieYear = rs.getInt("year");
				String movieDirector = rs.getString("director");

				ResultSet genres_in_movies = stmt1
						.executeQuery("SELECT * from genres_in_movies where movieId='" + movieId + "'");
				int genreId = 0;
				if (genres_in_movies.next()) {
					genreId = genres_in_movies.getInt("genreId");
				}
				ResultSet genres = stmt2.executeQuery("SELECT * from genres where id='" + genreId + "'");
				String genreName = "";
				if (genres.next()) {
					genreName = genres.getString("name");
				}
				ResultSet ratings = stmt5.executeQuery("SELECT * from ratings where movieId='" + movieId + "'");
				float rating = 0;
				if (ratings.next()) {
					rating = ratings.getFloat("rating");
				}

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", movieId);
				jsonObject.addProperty("title", movieTitle);
				jsonObject.addProperty("year", movieYear);
				jsonObject.addProperty("director", movieDirector);
				jsonObject.addProperty("genres", genreName);
				jsonObject.addProperty("ratings", rating);
				jsonArray.add(jsonObject);

				ratings.close();
				genres.close();
				genres_in_movies.close();
			}

		} catch (Exception e) {
			// TODO: handle exception
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("message", e.getMessage());
			jsonArray.add(jsonObject);
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
		return jsonArray;
	}

	private JsonArray genresTitle(String like, String g_type) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		JsonArray jsonArray = new JsonArray();

		try {
			conn = dataSource.getConnection();
			// limit the search data to 60
			String query = "SELECT m.id,m.title,m.year,m.director,g.name genres,r.rating FROM movies m,genres_in_movies gm,genres g,ratings r WHERE m.title LIKE '"
					+ like + "%' AND m.id=gm.movieId AND gm.genreId=g.id AND (" + g_type
					+ ") AND r.movieId=m.id LIMIT 60";
//			String query = "SELECT * FROM movies WHERE title LIKE 'a%' ORDER BY title limit ?, 20";

			stmt = conn.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				String movieId = rs.getString("id");
				String movieTitle = rs.getString("title");
				int movieYear = rs.getInt("year");
				String movieDirector = rs.getString("director");
				String genreName = rs.getString("genres");
				float rating = rs.getFloat("rating");

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", movieId);
				jsonObject.addProperty("title", movieTitle);
				jsonObject.addProperty("year", movieYear);
				jsonObject.addProperty("director", movieDirector);
				jsonObject.addProperty("genres", genreName);
				jsonObject.addProperty("ratings", rating);
				jsonArray.add(jsonObject);
			}

		} catch (Exception e) {
			// TODO: handle exception
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("message", e.getMessage());
			jsonArray.add(jsonObject);
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
		return jsonArray;
	}
}
