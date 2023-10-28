package controller;

import application.DatabaseHelper;
import application.Main;
import projects.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ExistenceProjectController implements Initializable{

	@FXML
    private TextField searchField;

    @FXML
    private TableView<Project> TableView;

    @FXML
    private TableColumn<Project, String> IDColumn;

    @FXML
    private TableColumn<Project, String> ProjectNameColumn;

    @FXML
    private TableColumn<Project, String> DescriptionColumn;

    @FXML
    private TableColumn<Project, LocalDate> InvoiceDateColumn;

    @FXML
    private TableColumn<Project, LocalDate> LastUpdatedColumn;

    @FXML
    private TableColumn<Project, String> StatusColumn;

    @FXML
    private TableColumn<Project, String> ActionsColumn;
    
    @FXML
    private Button newTicket;
    

   // private ObservableList<Project> projectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        InvoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        List<Project> projectList = DatabaseHelper.getAllProjects();
        ObservableList<Project> observableList = FXCollections.observableArrayList(projectList);
        TableView.setItems(observableList);
    	
    
    }
    @FXML
    public void clearAll() {
        DatabaseHelper.clearProjectTable();
        refreshTable();
    }
    private void refreshTable() {
        List<Project> projectList = DatabaseHelper.getAllProjects();
        ObservableList<Project> observableList = FXCollections.observableArrayList(projectList);
        TableView.setItems(observableList);
    }
     
    
    public void newTickets(ActionEvent event) {
        showNewTickets();
    }
    
    @FXML
    public void showNewTickets() {
    	try {
	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NewTicket.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
	        stage.setTitle("New Ticket");
	        stage.setScene(new Scene(root, 600, 600));
	        stage.initStyle(StageStyle.UTILITY);
	        stage.show();

	        Main.setClosable(false);
	        stage.setOnCloseRequest(e-> Main.setClosable(true));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
    }

/**
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
    **/
/**
    private boolean validateTicketData(String title, String description) {
        return title != null && !title.trim().isEmpty() && description != null && !description.trim().isEmpty();
    }
    **/
/**
    public void loadProjects() {
        List<Project> projectsFromDB = fetchProjectsFromDatabase();
        if (projectsFromDB != null) {
            projectList.addAll(projectsFromDB);
        }
    }
    **/
/**
    private List<Project> fetchProjectsFromDatabase() {
        return DatabaseHelper.getAllProjects();
    }
**/
}
