package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projects.Project;
import application.DatabaseHelper;
import java.time.LocalDate;



public class EditProjectController {

    
    @FXML
    private TextField nameTextField; 
    @FXML
    private TextField descriptionTextField; 
   

   
    @FXML
    private Button saveButton; 
    @FXML
    private Button cancelButton; 
    
    @FXML
    private DatePicker projDatePicker;


    
    private Project project;

 
    public void setProject(Project project) {
        this.project = project;
        nameTextField.setText(project.getName());
        descriptionTextField.setText(project.getDescription());
        projDatePicker.setValue(project.getDate() != null ? project.getDate() : LocalDate.now());

    }
   

   
    @FXML
    private void handleSaveAction() {
       
        project.setName(nameTextField.getText());
        project.setDescription(descriptionTextField.getText());
//        project.updateLastUpdatedDate();

     
        project.setDate(projDatePicker.getValue());
        
         DatabaseHelper.updateProject(project); 

        
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    


   
    @FXML
    private void handleCancelAction() {
        
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

   
    


}
