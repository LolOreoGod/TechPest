package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.CommonObjs;
import application.DatabaseHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projects.Ticket;

public class EditTicketController implements Initializable{

    @FXML
    private AnchorPane editTicketAnchorPane;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;
    
    @FXML
    private Label showError;

    private Stage stage;
	private Scene scene;
    
    
    private CommonObjs common = CommonObjs.getInstance();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	nameTextField.setText(common.getSelectedTicket().getTitle());
    	descriptionTextField.setText(common.getSelectedTicket().getDescription());
    	
    	
    	
    }
    
    
    @FXML
    void handleCancelAction(ActionEvent event) {
    	Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    	

    }

    @FXML
    void handleSaveAction(ActionEvent event) {
    	if (nameTextField.getText().isEmpty() || descriptionTextField.getText().isEmpty()){
			showError.setText("All fields must be filled!");
			return;
		}
    	
    	Ticket ticket = common.getSelectedTicket();
    	ticket.setTitle(nameTextField.getText());
        ticket.setDescription(descriptionTextField.getText());
//        project.updateLastUpdatedDate();
        
        
         DatabaseHelper.updateTicket(ticket); 

        
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void handleEditComments(ActionEvent event) {
    	try {
			common.setEditView(true);
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

}