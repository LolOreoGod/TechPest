package controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class NewCommentController {

    @FXML
    private DatePicker projDate;

    @FXML
    private TextArea projDesc;

    @FXML
    private Label showError;

    @FXML
    private Button back;

    @FXML
    private Button submit;

    @FXML
    void back(ActionEvent event) {
    	Main.setClosable(true);
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }

    @FXML
    void submit(ActionEvent event) {
    	try {
            // 1. Extract data from input fields
            String comment = projDesc.getText();

            // 2. Validate the data 
            if (comment.isEmpty()) {
                showError.setText("Comment cannot be empty!");
                return;
            }

            // Automatically capture the current date-time
            LocalDateTime currentDateTime = LocalDateTime.now();
            String formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Display the current date-time in the non-editable TextField
            projDesc.setText(formattedDateTime);

            // 3. Save the data
            System.out.println("Date-Time: " + formattedDateTime);
            System.out.println("Comment: " + comment);

            // Display success message
            showError.setText("Comment saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            showError.setText("An error occurred while saving the comment.");
        }
    }

    }  




