package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
	
	@FXML
	private TableColumn<Ticket, Void> ActionsColumn;

	private List<Project> allProjects;
	private List<Ticket> ticketList;
	private Stage stage;
	private Scene scene;
	private CommonObjs common = CommonObjs.getInstance();

	@FXML
	void back(ActionEvent event) {
		try {
			// does not open popup, just switches scene
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ViewProjects.fxml"));
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
			common.setSelectedProject(selectedProject);

			if (this.ticketList == null) {
				// Handle the case when there's an error fetching tickets
				return;
			}
		

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
		
		setupActionsColumn();
	}

	private void viewComments(String ticketName, MouseEvent event) {

		try {
			common.setEditView(false);
			Ticket selectedTicket = ticketsTableView.getSelectionModel().getSelectedItem();
			common.setSelectedTicket(selectedTicket);
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ViewComments.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			scene = new Scene(root);
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
	
	private void setupActionsColumn() {
        ActionsColumn.setCellFactory(param -> new TableCell<Ticket, Void>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Ticket ticket = getTableView().getItems().get(getIndex());
                    common.setSelectedTicket(ticket);
                    handleEditAction(ticket);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }
	
	private void handleEditAction(Ticket ticket) {
	    try {
	        
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditTicket.fxml"));
	        Parent root = loader.load();
	        
	        
	        
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Edit Ticket");
	        stage.initModality(Modality.APPLICATION_MODAL);
	        
	        
	        stage.setOnHidden(event -> refreshTable());
	        
	       
	        stage.showAndWait();
	    } catch (IOException e) {
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
        
        //We need to first get all the projects and filter them by search, like we did in Project Search
        List<Project> filteredProj= DatabaseHelper.getAllProjects();
        //does project name contain search case?
        filteredProj= filteredProj.stream()
        		.filter(project -> project.getName().toLowerCase().contains(searchText))
        		.collect(Collectors.toList());
        
        
        //Within the list of filtered tickets (by project)
        //We filter for search by ticket name
        List<Ticket> filteredTickets = ticketList.stream()
                .filter(ticket -> ticket.getTitle().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        
        //For all the projects that we filtered, we must add each of their tickets into filtered tickets
        //This way, we can find tickets attributed to the project we search for
        
        
        /*
         * TODO: FIX DOUBLE COUNTING
         */
        for(Project p : filteredProj) {
        	filteredTickets.addAll(DatabaseHelper.getTicketsForProject(p.getId()));
        
        }
        
        ObservableList<Ticket> observableList = FXCollections.observableArrayList(filteredTickets);

        if (observableList.isEmpty()) {
            ticketsTableView.setPlaceholder(new Label("No tickets found with the search term."));
        } else {
            ticketsTableView.setPlaceholder(new Label("")); 
        }

        ticketsTableView.setItems(observableList);
    }
	
	private void refreshTable() {
		//refresh, with respect to project selection
		if(common.getSelectedProject() != null) {
			ticketList = DatabaseHelper.getTicketsForProject(common.getSelectedProject().getId());
			ObservableList<Ticket> observableList = FXCollections.observableArrayList(ticketList);
			ticketsTableView.setItems(observableList);
		}
		else {
			fullRefreshTable();
		}
		
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
		common.setSelectedProject(null);
		fullRefreshTable();
	}
	
    @FXML
    void clearAll(ActionEvent event) {
    	DatabaseHelper.clearTicketsTable();
		refreshTable();
    }
    
    @FXML
    void deleteTicket(ActionEvent event) {
    	Ticket selectedTicket = ticketsTableView.getSelectionModel().getSelectedItem();
    	if (selectedTicket != null) {
    	    DatabaseHelper.deleteTicketCascade(selectedTicket);
    	    refreshTable();
    	} else {
    	    System.out.print("Please select a ticket");
    	}

    }
    
    @FXML
    void handleNewTicket(ActionEvent event) {
    	System.out.println("View ticket");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NewTicket.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			scene = new Scene(root);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("New Ticket");
			stage.show();
			
			refreshTable();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
