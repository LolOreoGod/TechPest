package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.CommonObjs;
import application.DatabaseHelper;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import projects.Comment;
import projects.Project;
import projects.Ticket;

public class ViewTicketsController implements Initializable {

	@FXML
	private ComboBox<String> projectDropdown;

	@FXML
	private TableView<Ticket> ticketsTableView;

	@FXML
	private Label showError;

	@FXML
	private Button back;

	@FXML
	private TableColumn<Ticket, String> TicketIDColumn;

	@FXML
	private TableColumn<Ticket, String> TicketName;

	@FXML
	private TableColumn<Ticket, String> TicketDescription;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private TextField searchField;
	
	@FXML
	private Button clearProjSelectionButton;

	private List<Project> allProjects;
	private List<Ticket> ticketList;
	private Stage stage;
	private Scene scene;
	private CommonObjs common = CommonObjs.getInstance();

	@FXML
	void back(ActionEvent event) {
		try {
			// does not open popup, just switches scene
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ExistenceProject.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			scene = new Scene(root, 1000, 600);
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

	// Assuming the 'Project' and 'Ticket' classes are appropriately defined.

	public void initialize(URL location, ResourceBundle resources) {
		allProjects = DatabaseHelper.getAllProjects();
		
		TicketIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		TicketName.setCellValueFactory(new PropertyValueFactory<>("title"));
		TicketDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		List<Ticket> list = DatabaseHelper.getAllTickets();
		ObservableList<Ticket> ticketList = FXCollections.observableArrayList(list);
		ticketsTableView.setItems(ticketList);
		ticketsTableView.refresh();

		if (allProjects == null || allProjects.isEmpty()) {
			showError.setText("Error fetching projects from the database!");
			return;
		}

		ObservableList<String> projectNames = FXCollections
				.observableArrayList(allProjects.stream().map(Project::getName).sorted().collect(Collectors.toList()));

		projectDropdown.setItems(projectNames);

		// Add a listener to the dropdown to fetch tickets related to the selected
		// project
		projectDropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			String selectedProjectName = newValue;
			Project selectedProject = allProjects.stream()
					.filter(project -> project.getName().equals(selectedProjectName)).findFirst().orElse(null);

			if (selectedProject == null) {
				return; // Handle the case when the selected project is not found
			}

			int projectId = selectedProject.getId(); // Assuming a method to get the project ID

			// Fetch all tickets related to the selected project ID
			this.ticketList = DatabaseHelper.getTicketsForProject(projectId);

			if (this.ticketList == null) {
				// Handle the case when there's an error fetching tickets
				return;
			}
		

		TicketIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		TicketName.setCellValueFactory(new PropertyValueFactory<>("title"));
		TicketDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

		ObservableList<Ticket> projectTicketsObservableList = FXCollections.observableArrayList(this.ticketList);
		ticketsTableView.setItems(projectTicketsObservableList);
		ticketsTableView.refresh();
			
			

		});
		//select default project name in drop down, based off of previous selection
		projectDropdown.getSelectionModel().select(common.getSelectedProject().getName());
		common.setTicketTable(ticketsTableView);;
		
		/*
		 * DOUBLE CLICK EVENT
		 */
		ticketsTableView.setOnMouseClicked(event -> {
			//If double click
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				// on double click, open view ticket
				if (ticketsTableView.getSelectionModel().getSelectedItem() != null) {
					String ticketName = ticketsTableView.getSelectionModel().getSelectedItem().getTitle();
					viewComments(ticketName, event);
				}
			}
		});
	}

	private void viewComments(String ticketName, MouseEvent event) {

		try {
			Ticket selectedTicket = ticketsTableView.getSelectionModel().getSelectedItem();
			common.setSelectedTicket(selectedTicket);
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ViewComments.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			scene = new Scene(root, 1000, 600);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("View Comment");
			stage.show();

			// Get the selected ticket from the table
			
			// Main.setClosable(false);
			// stage.setOnCloseRequest(e-> Main.setClosable(true));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	@FXML
    void handleSearchAction(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();
 
        if (searchText.isEmpty() && projectDropdown.getSelectionModel().isEmpty()) {
            fullRefreshTable();
            return;
        }
        //Within the list of filtered tickets (by project)
        //We filter for search by ticket name
        List<Ticket> filteredTickets = ticketList.stream()
                .filter(ticket -> ticket.getTitle().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        ObservableList<Ticket> observableList = FXCollections.observableArrayList(filteredTickets);
        //TODO: Filter for search by project name as well

        if (observableList.isEmpty()) {
            ticketsTableView.setPlaceholder(new Label("No projects found with the search term."));
        } else {
            ticketsTableView.setPlaceholder(new Label("")); 
        }

        ticketsTableView.setItems(observableList);
    }
	
	private void refreshTable() {
		//refresh, with respect to project selection
		ticketList = DatabaseHelper.getTicketsForProject(common.getSelectedProject().getId());
		ObservableList<Ticket> observableList = FXCollections.observableArrayList(ticketList);
		ticketsTableView.setItems(observableList);
	}
	
	//Fully refresh, disregarding project selection
	private void fullRefreshTable() {
		ticketList = DatabaseHelper.getAllTickets();
		ObservableList<Ticket> observableList = FXCollections.observableArrayList(ticketList);
		ticketsTableView.setItems(observableList);
	}

	
	@FXML
	void handleClearProjSelection(ActionEvent Event) {
		projectDropdown.getSelectionModel().clearSelection();
		fullRefreshTable();
	}
}
