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
import projects.Project;  // Import your Project class

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:mydatabase.db";

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

    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS projects (\n"
            + " id integer PRIMARY KEY,\n"
            + " name text NOT NULL,\n"
            + " description text,\n"
            + " date text NOT NULL\n"   // Added a date column of type text
            + ");";

        try (Connection conn = connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertProject(Project project) {
        String sql = "INSERT INTO projects(name, description, date) VALUES(?,?,?)"; // Added date parameter

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getDescription());
            pstmt.setString(3, project.getDate().toString()); // Store date as string
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM projects";
        
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(selectQuery)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                LocalDate date = LocalDate.parse(rs.getString("date")); // Parse date string to LocalDate
                
                Project project = new Project(id, name, description, date);  // Use the constructor with ID
                projectList.add(project);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return projectList;
    }
    
    public static void clearProjectTable() {
    	String sql = "DELETE FROM projects";
    	try(Connection conn = connect();
    		Statement stmt = conn.createStatement()){
    		stmt.executeLargeUpdate(sql);
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    }

}
