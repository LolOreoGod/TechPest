package controller;

import java.awt.event.ActionEvent;
import java.time.LocalDate;

import javax.xml.stream.events.Comment;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class CommentPageController {
    @FXML
    private TableView<Comment> commentTable;
    @FXML
    private TableColumn<Comment, LocalDate> dateColumn;
    @FXML
    private TableColumn<Comment, String> commentColumn;
    @FXML
    private Button back;

    private Stage stage;

    @FXML
    void initialize() {
        // You can initialize the table and other components here
    }

    @FXML
    void back() {

    }
}
