package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.DatabaseHelper;
import projects.Comment;
import projects.Project;
import projects.Ticket;

public class NewTicketControllerSB {

	@FXML
	private ComboBox<String> projectDropdown;

	@FXML
	private TextField ticketTitleInput;

	@FXML
	private TextArea ticketDesc;

	@FXML
	private Label showError;

	@FXML
	private Button back;

	@FXML
	private Button submitButton;

	@FXML
	private TextArea newCommentText;

	@FXML
	private Button submitComment;

	@FXML
	private ListView<String> commentListView;

	// Class variable to store the projects
	private List<Project> allProjects;


	@FXML
	public void initialize() {

		// Fetch all projects from the database
		allProjects = DatabaseHelper.getAllProjects();

		// Check if allProjects is null
		if (allProjects == null) {
			showError.setText("Error fetching projects from the database!");
			return;
		}

		// Extract project names and sort them alphabetically
		ObservableList<String> projectNames = FXCollections
				.observableArrayList(allProjects.stream().map(Project::getName).sorted().collect(Collectors.toList()));

		projectDropdown.setItems(projectNames);
	}

	@FXML
	private void back(ActionEvent event) {
		// Close the current window
		Stage stage = (Stage) back.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void submit(ActionEvent event) {
	    if (projectDropdown.getSelectionModel().getSelectedItem() == null || ticketTitleInput.getText().isEmpty()
	            || ticketDesc.getText().isEmpty()) {
	        showError.setText("All fields must be filled!");
	        return;
	    }

	    String projectName = projectDropdown.getSelectionModel().getSelectedItem();
	    String ticketTitle = ticketTitleInput.getText();
	    String description = ticketDesc.getText();

	    // Get the project by its name directly from the stored projects
	    Project selectedProject = allProjects.stream()
	            .filter(project -> project.getName().equals(projectName))
	            .findFirst()
	            .orElse(null);

	    if (selectedProject == null) {
	        showError.setText("Error fetching project!");
	        return;
	    }
	    int id = 0;
		// Create a new Ticket object
	    Ticket newTicket = new Ticket(id, ticketTitle, description);

	    newTicket.setId(id);
	    newTicket.setTitle(ticketTitle);
	    newTicket.setDescription(description);

	    try {
	        // Insert the new ticket into the database
	        DatabaseHelper.insertTicket(newTicket, selectedProject);
	        showError.setText("Ticket successfully added!");
	    } catch (Exception e) {
	        showError.setText("Error adding the ticket: " + e.getMessage());
	        e.printStackTrace(); // This line prints the full stack trace for debugging.
	    }

	    back(event);
	}
}
