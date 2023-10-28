package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import projects.Project;
import projects.Ticket;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:mydatabase.db";

    // Establish a connection to the SQLite database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Create a new 'projects' table if it doesn't exist
    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS projects ("
                   + " id integer PRIMARY KEY,"
                   + " name text NOT NULL,"
                   + " description text,"
                   + " date text NOT NULL"
                   + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
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

    // Retrieve all projects from the 'projects' table
    public static List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM projects";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectQuery)) {
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
    
    public static List<Project> getAllTickets() {
        List<Project> projectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM projects";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectQuery)) {
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

    // Delete all entries from the 'projects' table
    public static void clearProjectTable() {
        String sql = "DELETE FROM projects";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Create a new 'tickets' table if it doesn't exist
    public static void createNewTicketsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tickets ("
                   + " id integer PRIMARY KEY,"
                   + " projectId integer NOT NULL,"
                   + " title text NOT NULL,"
                   + " description text,"
                   + " FOREIGN KEY (projectId) REFERENCES projects(id)"
                   + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert a new ticket into the 'tickets' table
    public static void insertTicket(int projectId, String title, String description){
        String sql = "INSERT INTO tickets(projectId, title, description) VALUES(?,?,?)"; 

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Retrieve all tickets related to a specific project
    public static List<Ticket> getTicketsForProject(int projectId) {
        List<Ticket> ticketList = new ArrayList<>();
        String selectQuery = "SELECT * FROM tickets WHERE projectId = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Ticket ticket = new Ticket(id, projectId, title, description);
                ticketList.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ticketList;
    }

    // Delete all entries from the 'tickets' table
    public static void clearTicketsTable() {
        String sql = "DELETE FROM tickets";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
