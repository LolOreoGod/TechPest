package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

import application.DatabaseHelper;
import projects.Project;

public class NewTicketControllerSB {

    @FXML
    private ComboBox<String> projectDropdown;

    
    @FXML
    private TextField ticketTitleInput;
    
    @FXML
    private TextArea projDesc;  
    
    @FXML
    private Label showError;   

    @FXML
    private Button back;

    @FXML
    private Button submit;

    // Class variable to store the projects
    private List<Project> allProjects;

    @FXML
    public void initialize() {
        // Fetch all projects from the database
        allProjects = DatabaseHelper.getAllProjects();

        // Check if allProjects is null
        if(allProjects == null) {
            showError.setText("Error fetching projects from the database!");
            return;
        }

        // Extract project names and sort them alphabetically
        ObservableList<String> projectNames = FXCollections.observableArrayList(
        	    allProjects.stream()
        	               .map(Project::getName)
        	               .sorted()
        	               .collect(Collectors.toList())
        	);


        projectDropdown.setItems(projectNames);
    }


    @FXML
    private void back() {
        // Close the current window
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void submit() {
        String projectName = projectDropdown.getSelectionModel().getSelectedItem();
        String ticketTitle = ticketTitleInput.getText();
        String description = projDesc.getText();

        if (projectName == null || ticketTitle.isEmpty() || description.isEmpty()) {
            showError.setText("All fields must be filled!");
            return;
        }

        // Get the project ID by its name directly from the stored projects
        int projectId = allProjects.stream()
            .filter(project -> project.getName().equals(projectName))
            .findFirst()
            .map(Project::getId)
            .orElse(-1);

        if (projectId == -1) {
            showError.setText("Error fetching project ID!");
            return;
        }

        try {
            // Insert the new ticket into the database
            DatabaseHelper.insertTicket(projectId, ticketTitle, description);
            showError.setText("Ticket successfully added!");
        } catch (Exception e) {
            showError.setText("Error adding the ticket: " + e.getMessage());
        }

        // Optionally, close the window after submission
        // back();
    }
    
    
}
