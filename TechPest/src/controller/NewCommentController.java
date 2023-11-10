package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import projects.Comment;
import projects.Project;
import projects.Ticket;


public class NewCommentController implements Initializable{
	
    @FXML
    private ChoiceBox<String> ticketDropBox;
    
    @FXML
    private TextField commentDate;

    @FXML
    private TextArea commentDesc;

    @FXML
    private Label showError;

    @FXML
    private Button back;

    @FXML
    private Button submit;
    
    
    private List<Ticket> allTickets;
    //common singleton
    private Ticket selectedTicket;
    private CommonObjs common = CommonObjs.getInstance();
    
    //date instance vars
    private LocalDateTime currentDateTime = LocalDateTime.now();
    //private DateTimeFormatterBuilder df = new DateTimeFormatterBuilder();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String formattedDateTime;

    public void initialize(URL location, ResourceBundle resources) {
    	//commentDate.setValue(LocalDate.now());
    	allTickets = DatabaseHelper.getAllTickets();
    	
    	if (allTickets == null) {
			showError.setText("Error fetching projects from the database!");
			return;
    	}
		// Extract project names and sort them alphabetically
		ObservableList<String> ticketNames = FXCollections
				.observableArrayList(allTickets.stream().map(Ticket::getTitle).sorted().collect(Collectors.toList()));

		ticketDropBox.setItems(ticketNames);
		
		//set selected ticket in dropdown
		selectedTicket = common.getSelectedTicket();
		ticketDropBox.getSelectionModel().select(selectedTicket.getTitle());
		
		//make text field uneditaable, then set date
		commentDate.setEditable(false);
        formattedDateTime = currentDateTime.format(formatter);
        // Display the current date-time in the non-editable TextField
        commentDate.setText(formattedDateTime);
		
        
        
    }


    @FXML
    void back(ActionEvent event) {
    	Main.setClosable(true);
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void submit() throws IOException {
    	//NOTE THAT SINCE DATE IS SET TO A STRING,
        //WE MUST CONVERT IT BACK TO A LOCALDATE TO STORE IN DATABASE
        LocalDate date = LocalDate.parse(commentDate.getText(), formatter);
        String comment = commentDesc.getText();

        if (ticketDropBox.getSelectionModel().getSelectedItem() == null || comment.isEmpty()) {
            showError.setText("All fields must be filled!");
            return;
        }

        // Create the comments table if it doesn't exist
        DatabaseHelper.createNewCommentTable();
        // Use the DatabaseHelper class to insert the new comment into the database
        DatabaseHelper.insertComment(date, comment);

        ExistenceProjectController.setClosable(true);
        
        //take last record from database and add it to table
        
        

        Stage stage = (Stage) submit.getScene().getWindow();
        stage.close();
        
        
    }
    



}
