package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import application.CommonObjs;
import application.DatabaseHelper;
import application.Main;
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
	private Scene scene;
	private Stage stage;

	
	private CommonObjs common = CommonObjs.getInstance();

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
		
		if(common.getSelectedProject() != null) {
			projectDropdown.getSelectionModel().select(common.getSelectedProject().getName());
		}
		
		projectDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			String selectedProjectName = newValue;
			Project selectedProject = allProjects.stream()
					.filter(project -> project.getName().equals(selectedProjectName)).findFirst().orElse(null);

			common.setSelectedProject(selectedProject);
			
			

		});
		
	}

	@FXML
	private void back(ActionEvent event) {
		try {
			// does not open popup, just switches scene
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ViewTickets.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			scene = new Scene(root);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("View Projects");
			stage.show();

			Main.setClosable(false);
			stage.setOnCloseRequest(e -> Main.setClosable(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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

		// Get the project ID by its name directly from the stored projects
		int projectId = allProjects.stream().filter(project -> project.getName().equals(projectName)).findFirst()
				.map(Project::getId).orElse(-1);

		if (projectId == -1) {
			showError.setText("Error fetching project ID!");
			return;
		}

		try {
			// Insert the new ticket into the database
			System.out.println("inserted");
			DatabaseHelper.insertTicket(projectId, ticketTitle, description);
			showError.setText("Ticket successfully added!");
		} catch (Exception e) {
			showError.setText("Error adding the ticket: " + e.getMessage());
			e.printStackTrace(); // This line prints the full stack trace for debugging.
		}

		back(event);
	}
}
