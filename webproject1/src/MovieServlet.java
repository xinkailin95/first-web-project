

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class MovieServlet
 */
@WebServlet("/MovieServlet")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "jdbc/movieDB")
    private DataSource dataSource;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		// change this to your own mysql username and password
		String loginUser = "test";
		String loginPasswd = "";
		String loginUrl = "jdbc:mysql://13.58.57.202:3306/moviedb";
			
		// set response mime type
        response.setContentType("text/html");
		
        // get the printwriter for writing response
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head><title>Fabflix</title></head>");
        
        try {
	    		Class.forName("com.mysql.jdbc.Driver").newInstance();
	    		// create database connection
//	    		Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
	    		Connection connection = dataSource.getConnection();
	    		// declare statement
	    		Statement statement = connection.createStatement();
	    		Statement statement1 = connection.createStatement();
	    		Statement statement2 = connection.createStatement();
	    		Statement statement3 = connection.createStatement();
	    		Statement statement4 = connection.createStatement();
	    		Statement statement5 = connection.createStatement();
	    		// prepare query
	    		String query = "SELECT * from ratings order by rating desc limit 20";
	    		// execute query
	    		ResultSet resultSet = statement.executeQuery(query);
	
	    		out.println("<body>");
	    		out.println("<h1>Movies List</h1>");
	    		
	    		out.println("<table border>");
	    		
	    		// add table header row
	    		out.println("<tr>");
	    		out.println("<td>id</td>");
	    		out.println("<td>title</td>");
	    		out.println("<td>year</td>");
	    		out.println("<td>director</td>");
	    		out.println("<td>genres</td>");
	    		out.println("<td>stars</td>");
	    		out.println("<td>rating</td>");
	    		out.println("</tr>");
	    		
	    		// add a row for every movie result
	    		while (resultSet.next()) {
	    			// get a movie from result set
	    			String movieId = resultSet.getString("movieId");
	    			ResultSet movies = statement1.executeQuery("SELECT * from movies where id='"+movieId+"'");
	    			ResultSet genres_in_movies = statement3.executeQuery("SELECT * from genres_in_movies where movieId='"+movieId+"'");
	    			ResultSet stars_in_movies = statement5.executeQuery("SELECT * from stars_in_movies where movieId='"+movieId+"'");
	    			String starId = "";
	    			if(stars_in_movies.next()) {
	    				starId = stars_in_movies.getString("starId");
	    			}
	    			ResultSet stars = statement4.executeQuery("SELECT * from stars where id='"+starId+"'");
	    			String starName = "";
	    			if(stars.next()) {
	    				starName = stars.getString("name");
	    			}
	    			int genreId = 0;
	    			if(genres_in_movies.next()) {
	    				genreId = genres_in_movies.getInt("genreId");
	    			}
	    			ResultSet genres = statement2.executeQuery("SELECT * from genres where id='"+genreId+"'");
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
	    			float rating = resultSet.getFloat("rating");
	    			
	    			out.println("<tr>");
	    			out.println("<td>" + movieId + "</td>");
	    			out.println("<td>" + movieTitle + "</td>");
	    			out.println("<td>" + movieYear + "</td>");
	    			out.println("<td>" + movieDirector + "</td>");
	    			out.println("<td>" + genreName + "</td>");
	    			out.println("<td>" + starName + "</td>");
	    			out.println("<td>" + rating + "</td>");
	    			out.println("</tr>");
	    			
	    			movies.close();
	    			genres.close();
	    			genres_in_movies.close();
	    			stars.close();
	    			stars_in_movies.close();
	    		}
	    		
	    		out.println("</table>");
	    		
	    		out.println("</body>");
	    		
	    		resultSet.close();
	    		statement.close();
	    		statement1.close();
	    		statement2.close();
	    		statement3.close();
	    		statement4.close();
	    		statement5.close();
	    		connection.close();
	    		
	    } catch (Exception e) {
	    		/*
	    		 * After you deploy the WAR file through tomcat manager webpage,
	    		 *   there's no console to see the print messages.
	    		 * Tomcat append all the print messages to the file: tomcat_directory/logs/catalina.out
	    		 * 
	    		 * To view the last n lines (for example, 100 lines) of messages you can use:
	    		 *   tail -100 catalina.out
	    		 * This can help you debug your program after deploying it on AWS.
	    		 */
	    		e.printStackTrace();
	    		
	    		out.println("<body>");
	    		out.println("<p>");
	    		out.println("Exception in doGet: " + e.getMessage());
	    		out.println("</p>");
	    		out.print("</body>");
	    }
	    
	    out.println("</html>");
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
