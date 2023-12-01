package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projects.Project;
import application.CommonObjs;
import application.DatabaseHelper;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;



public class EditProjectController implements Initializable{

    
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


    private CommonObjs common = CommonObjs.getInstance();
    private Project project;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//this.project = common.getSelectedProject();
    	nameTextField.setText(common.getSelectedProject().getName());
    	descriptionTextField.setText(common.getSelectedProject().getDescription());
    	projDatePicker.setValue(common.getSelectedProject().getDate());
    	
    	
    }
    
    /**
    public void setProject(Project project) {
        this.project = project;
        nameTextField.setText(project.getName());
        descriptionTextField.setText(project.getDescription());
        projDatePicker.setValue(project.getDate() != null ? project.getDate() : LocalDate.now());

    }
   
    **/
   
    @FXML
    private void handleSaveAction() {
    	this.project = common.getSelectedProject();
        project.setName(nameTextField.getText());
        project.setDescription(descriptionTextField.getText());
        projDatePicker.setValue(project.getDate() != null ? project.getDate() : LocalDate.now());
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
