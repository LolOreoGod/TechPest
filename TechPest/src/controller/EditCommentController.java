package controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.CommonObjs;
import application.DatabaseHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import projects.Comment;
import projects.Ticket;

public class EditCommentController implements Initializable{

    @FXML
    private AnchorPane editCommentAnchorPane;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;
    
    @FXML
    private TextField commentDate;
    
    @FXML
    private Label showError;
    
    
    private CommonObjs common = CommonObjs.getInstance();
    private Comment oldComment;
    
    
    //date instance vars
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String formattedDateTime;
    
    
    @FXML
    void handleCancelAction(ActionEvent event) {
    	Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleSaveAction(ActionEvent event) {
    	if (descriptionTextField.getText().isEmpty()){
			showError.setText("All fields must be filled!");
			return;
		}
    	
    	//Creating the new comment
    	LocalDate date = LocalDate.parse(commentDate.getText(), formatter);
        String commentStr = descriptionTextField.getText();
    	common.setSelectedComment(oldComment);
    	Comment comment = new Comment(date, commentStr);

        
       // System.out.println("Old comment: " + oldComment.getComments() + " New: " + comment.getComments());
         DatabaseHelper.updateComment(oldComment, comment); 

        
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//we need to save the old comment so we can figure out where to edit
		//since comments are not stored on an id basis
    	descriptionTextField.setText(common.getSelectedComment().getComments());
    	oldComment = common.getSelectedComment();
    	
    	formattedDateTime = currentDateTime.format(formatter);
    	commentDate.setText(formattedDateTime);
	}

}

