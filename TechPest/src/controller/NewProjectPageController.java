package controller;

import application.Main;
import application.CommonObjs;
import application.DatabaseHelper; // Importing DatabaseHelper class
import projects.Project; // Importing the Project class
import projects.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label; // Importing the Label class
import javafx.scene.paint.Color; // Importing the Color class
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class NewProjectPageController {

    @FXML
    private Button back;

    @FXML
    private Button submit;

    @FXML
    private TextField projname;

    @FXML
    private DatePicker projDate;

    @FXML
    private TextArea projDesc;

    @FXML
    private Label showError; // Label to display error message
    
    private CommonObjs common = CommonObjs.getInstance();

    @FXML
    private void initialize() {
        projDate.setValue(LocalDate.now());
    }

    @FXML
    public void back(ActionEvent event) {
        Main.setClosable(true);
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void submit() {
        String name = projname.getText();
        LocalDate date = projDate.getValue();
        String description = projDesc.getText();

        if (name.isEmpty() || date == null || description.isEmpty()) {
            showError.setText("Please fill in all fields.");
            showError.setTextFill(Color.RED);
            return; // Exit the method if any field is empty
        }

        // Create a new Project instance
        Project newProject = new Project(name, description, date);

        // Use the DatabaseHelper class to insert the new project into the database
        DatabaseHelper.insertProject(newProject);

        //Refresh table
        List<Project> projectList = DatabaseHelper.getAllProjects();
        ObservableList<Project> observableList = FXCollections.observableArrayList(projectList);
        if(common.getProjectTable() != null) {
        	common.getProjectTable().setItems(observableList);
        }
		
        
        
        Main.setClosable(true);
        Stage stage = (Stage) submit.getScene().getWindow();
        stage.close();
    }
}
