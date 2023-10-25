package projects;

import java.time.LocalDate;

public class Project {
    private int id;                // ID of the project
    private String name;            // Name of the project
    private String description;     // Description of the project
    private LocalDate date;         // Date the project was created

    // Constructor to initialize a new Project with ID (for retrieving from DB)
    public Project(int id, String name, String description, LocalDate date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    // Constructor without ID (for new projects)
    public Project(String name, String description, LocalDate date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    // Getters and Setters for each attribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
