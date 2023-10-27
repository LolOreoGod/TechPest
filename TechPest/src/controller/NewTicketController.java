package controller;

import application.DatabaseHelper;
import projects.Project;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;

public class NewTicketController {

    @FXML
    private ComboBox<Project> projectComboBox;  // ComboBox for project selection

    @FXML
    private TextField searchField;

    @FXML
    private TextField ticketTitle;
    @FXML
    private TextArea ticketDescription; 
    @FXML
    private Button btnCreateNewTicket;

    private ObservableList<Project> projectList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadProjects();

        projectComboBox.setItems(projectList);  // Set items for ComboBox
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null || newValue.isEmpty()) {
                projectComboBox.setItems(projectList); 
            } else {
                ObservableList<Project> filteredList = projectList.filtered(project -> 
                    project.getName().toLowerCase().contains(newValue.toLowerCase()));
                projectComboBox.setItems(filteredList);
            }
        });

        btnCreateNewTicket.setOnAction(e -> createNewTicket());
    }

    public void createNewTicket() {
        String title = ticketTitle.getText();
        String description = ticketDescription.getText();

        Project selectedProject = projectComboBox.getSelectionModel().getSelectedItem();  // Get the selected project from ComboBox

        if (selectedProject != null && validateTicketData(title, description)) {
            // Actual logic to save ticket to database
            DatabaseHelper.insertTicket(selectedProject.getId(), title, description); // Using the overloaded method to store title as well
            // Provide feedback to the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Ticket successfully created!");
            alert.showAndWait();
        } else {
            // Provide feedback about invalid data or if no project is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please provide valid data or select a project.");
            alert.showAndWait();
        }
    }

    private boolean validateTicketData(String title, String description) {
        return title != null && !title.trim().isEmpty() && description != null && !description.trim().isEmpty();
    }

    public void loadProjects() {
        List<Project> projectsFromDB = fetchProjectsFromDatabase();
        if (projectsFromDB != null) {
            projectList.addAll(projectsFromDB);
        }
    }

    private List<Project> fetchProjectsFromDatabase() {
        return DatabaseHelper.getAllProjects();
    }
}
