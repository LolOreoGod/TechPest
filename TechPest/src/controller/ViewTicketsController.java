package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import projects.Project;
import projects.Ticket;

public class ViewTicketsController implements Initializable{
	
	private String projName;

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
    
    private List<Project> allProjects;
    
    
    @FXML
    void back(ActionEvent event) {

    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TicketIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		TicketName.setCellValueFactory(new PropertyValueFactory<>("title"));
		TicketDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        List<Ticket> ticketList = DatabaseHelper.getAllTickets();
        ObservableList<Ticket> observableList = FXCollections.observableArrayList(ticketList);
        ticketsTableView.setItems(observableList);
        
		System.out.println(projName);
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
        
        projectDropdown.setValue(projName);
       // System.out.println(projectDropdown.getValue());
	}
	
	private void receiveData() {
		
	}
	
	/*
	public String setProjName(String name) {
		projName = name;
		return name;
	}
	*/

}
