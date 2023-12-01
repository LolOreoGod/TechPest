package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import projects.Comment;
import projects.Project;
import projects.Ticket;

public class DatabaseHelper {

	private static final String DB_URL = "jdbc:sqlite:projects.db";
	private static final String DB_URLTickets = "jdbc:sqlite:tickets.db";
	private static final String DB_URLComments = "jdbc:sqlite:comments.db";

	// Establish a connection to the SQLite database
	public static Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL);
			System.out.println("Connection to Projects SQLite has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static Connection connectTickets() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URLTickets);
			System.out.println("Connection to Tickets SQLite has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static Connection connectComments() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URLComments);
			System.out.println("Connection to Comments SQLite has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	// Create a new 'projects' table if it doesn't exist
	public static void createNewTable() {
		String sql = "CREATE TABLE IF NOT EXISTS projects (" + " id integer PRIMARY KEY," + " name text NOT NULL,"
				+ " description text," + " date text NOT NULL" + ");";

		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Create a new 'tickets' table if it doesn't exist
	public static void createNewTicketsTable() {
		String sql = "CREATE TABLE IF NOT EXISTS tickets (" + " id integer PRIMARY KEY,"
				+ " projectId integer NOT NULL," + " title text NOT NULL," + " description text,"
				+ " FOREIGN KEY (projectId) REFERENCES projects(id)" + ");";

		try (Connection conn = connectTickets(); Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void createNewCommentTable() {
		String sql = "CREATE TABLE IF NOT EXISTS comments (" + "date TEXT NOT NULL," + "comment TEXT,"
				+ "ticketId INTEGER," + "FOREIGN KEY (ticketId) REFERENCES ticket(id)" + ");";

		try (Connection conn = connectComments(); Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Insert a new project into the 'projects' table
	public static void insertProject(Project project) {
		String sql = "INSERT INTO projects(name, description, date) VALUES(?,?,?)";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, project.getName());
			pstmt.setString(2, project.getDescription());
			pstmt.setString(3, project.getDate().toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void deleteProject(Project project) {
		String sql = "DELETE FROM projects WHERE id = ?";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, project.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Retrieve all projects from the 'projects' table
	public static List<Project> getAllProjects() {
		List<Project> projectList = new ArrayList<>();
		String selectQuery = "SELECT * FROM projects";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectQuery)) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String description = rs.getString("description");
				LocalDate date = LocalDate.parse(rs.getString("date"));
				Project project = new Project(id, name, description, date);
				projectList.add(project);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return projectList;
	}

	// Retrieve all projects from the 'Tickets' table
	public static List<Ticket> getAllTickets() {
		List<Ticket> ticketList = new ArrayList<>();
		String selectQuery = "SELECT * FROM Tickets";

		try (Connection conn = connectTickets();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectQuery)) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("title");
				String description = rs.getString("description");
				Ticket ticket = new Ticket(id, name, description);
				ticketList.add(ticket);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return ticketList;
	}

	public static List<Comment> getAllComments() {
		List<Comment> commentList = new ArrayList<>();
		String selectQuery = "SELECT * FROM Comments";

		try (Connection conn = connectComments();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(selectQuery)) {
			while (rs.next()) {
				String dateString = rs.getString("date");
				LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String comments = rs.getString("comment"); // Adjusted the variable name to avoid conflict
				Comment comment = new Comment(date, comments); // Assuming there is a proper constructor in the Comment
																// class
				commentList.add(comment);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return commentList;
	}

	// Retrieve all tickets related to a specific project
	public static List<Ticket> getTicketsForProject(int selectedProjectId) {
		List<Ticket> ticketList = new ArrayList<>();
		String selectQuery = "SELECT * FROM tickets WHERE projectId = ?";

		try (Connection conn = connectTickets(); PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
			pstmt.setInt(1, selectedProjectId); // Use setInt for the project ID
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String description = rs.getString("description");
				Ticket ticket = new Ticket(id, title, description);
				ticketList.add(ticket);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return ticketList;
	}

	public static List<Comment> getCommentsForTicket(int ticketId) {
		List<Comment> commentList = new ArrayList<>();
		String selectQuery = "SELECT * FROM comments WHERE ticketID = ?";

		try (Connection conn = connectComments(); PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
			pstmt.setInt(1, ticketId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				LocalDate date = LocalDate.parse(rs.getString("date"));
				String comment = rs.getString("comment"); // Adjusted to match the column name in the database
				Comment commentObject = new Comment(date, comment);
				commentList.add(commentObject);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return commentList;
	}

	public static void clearProjectTableCascade(int projectId) {
		// This method will delete the project along with its tickets and comments

		// Step 1: Delete comments associated with tickets of the project
		String deleteCommentsSql = "DELETE FROM comments WHERE ticketID = ?";
		try (Connection conn = connectComments(); PreparedStatement pstmt = conn.prepareStatement(deleteCommentsSql)) {
			pstmt.setInt(1, projectId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// Step 2: Delete tickets of the project
		String deleteTicketsSql = "DELETE FROM tickets WHERE projectId = ?";
		try (Connection conn = connectTickets(); PreparedStatement pstmt = conn.prepareStatement(deleteTicketsSql)) {
			pstmt.setInt(1, projectId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		// Step 3: Delete the project itself
		String deleteProjectSql = "DELETE FROM projects WHERE id = ?";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(deleteProjectSql)) {
			pstmt.setInt(1, projectId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// Add a new method to delete a project along with its associated tickets and
	// comments
	public static void deleteProjectCascade(Project project) {
		clearProjectTableCascade(project.getId());
	}

	// Insert a new ticket into the 'tickets' table for a specific project
	public static void insertTicket(int projectId, String title, String description) {
		String sql = "INSERT INTO tickets(projectId, title, description) VALUES(?,?,?)";

		try (Connection conn = connectTickets(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, projectId);
			pstmt.setString(2, title);
			pstmt.setString(3, description);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void insertComment(int ticketId, LocalDate date, String comment) {
		String sql = "INSERT INTO comments (ticketID, date, comment) VALUES (?, ?, ?)";

		try (Connection conn = connectComments(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, ticketId);
			pstmt.setString(2, date.toString()); // Modified to convert LocalDate to String directly
			pstmt.setString(3, comment);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void clearTicketsTable() {
		String sql = "DELETE FROM tickets";
		try (Connection conn = connectTickets(); Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void clearCommentsTable() {
		String sql = "DELETE FROM comments";
		try (Connection conn = connectComments(); Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void deleteComment(LocalDate date, String comment) {
		String sql = "DELETE FROM comments WHERE date = ? AND comment = ?";
		try (Connection conn = connectComments(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, date.toString());
			pstmt.setString(2, comment);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void updateProject(Project project) {
		String sql = "UPDATE projects SET name = ?, description = ?, date = ? WHERE id = ?";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, project.getName());
			pstmt.setString(2, project.getDescription());
			pstmt.setString(3, project.getDate().toString());
			pstmt.setInt(4, project.getId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Project updated successfully!");
			} else {
				System.out.println("No project was updated. Make sure the ID is correct!");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void updateTicket(Ticket ticket) {
		String sql = "UPDATE tickets SET title = ?, description = ? WHERE id = ?";

		try (Connection conn = connectTickets(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, ticket.getTitle());
			pstmt.setString(2, ticket.getDescription());
			pstmt.setInt(3, ticket.getId());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Ticket updated successfully!");
			} else {
				System.out.println("No ticket was updated. Make sure the ID is correct!");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void updateComment(Comment oldComment, Comment comment) {
		String sql = "UPDATE comments SET date = ?, comment = ? WHERE comment = ?";

		try (Connection conn = connectComments(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, comment.getDate().toString());
			pstmt.setString(2, comment.getComments());
			//pstmt.setString(3, oldComment.getDate().toString());
			pstmt.setString(3, oldComment.getComments());

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Comment updated successfully!");
			} else {
				System.out.println("No comment was updated.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void deleteTicketCascade(Ticket ticket) {
	    if (ticket == null) {
	        System.out.println("Ticket is null");
	        return;
	    }

	    int ticketID = ticket.getId();

	    String deleteCommentsSql = "DELETE FROM comments WHERE ticketID = ?";
	    try (Connection conn = connectComments(); PreparedStatement pstmt = conn.prepareStatement(deleteCommentsSql)) {
	        pstmt.setInt(1, ticketID);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }

	    String deleteTicketsSql = "DELETE FROM tickets WHERE id = ?";
	    try (Connection conn = connectTickets(); PreparedStatement pstmt = conn.prepareStatement(deleteTicketsSql)) {
	        pstmt.setInt(1, ticketID);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}



	/*
	 * public static void updateProject(Project project) { String sql =
	 * "UPDATE projects SET name = ?, description = ?, date = ?, lastUpdated = ? WHERE id = ?"
	 * ;
	 * 
	 * try (Connection conn = connect(); PreparedStatement pstmt =
	 * conn.prepareStatement(sql)) { pstmt.setString(1, project.getName());
	 * pstmt.setString(2, project.getDescription()); pstmt.setString(3,
	 * project.getDate().toString()); pstmt.setString(4,
	 * project.getLastUpdated().toString()); pstmt.setInt(5, project.getId());
	 * 
	 * int affectedRows = pstmt.executeUpdate(); if (affectedRows > 0) {
	 * System.out.println("Project updated successfully!"); } else {
	 * System.out.println("No project was updated. Make sure the ID is correct!"); }
	 * } catch (SQLException e) { System.out.println(e.getMessage()); } }
	 */

}
