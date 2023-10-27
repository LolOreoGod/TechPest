package projects;

public class Ticket {
    private int id;
    private int projectId;
    private String title;
    private String description;

    // Constructor to initialize a new Ticket
    public Ticket(int id, int projectId, String title, String description) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters for each attribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
