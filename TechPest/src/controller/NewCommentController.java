package controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

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

    }

}
