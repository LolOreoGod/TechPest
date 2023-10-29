package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.xml.stream.events.Comment;

import application.DatabaseHelper;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import projects.Project;
import projects.Ticket;

public class CommentPageController {
    @FXML
    private TableView<Comment> commentTable;
    @FXML
    private TableColumn<Comment, LocalDate> dateColumn;
    @FXML
    private TableColumn<Comment, String> commentColumn;
    @FXML
    private Button back;

    @FXML
    private Button addComment;
    @FXML
    private MenuButton ticketDropDown;
    
    @FXML
    private Label ticketTitle;

    @FXML
    private Label ticketID;
    

    private Stage stage;
	private Scene scene;
	
	
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	
    @FXML
    void addComment(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NewComment.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("New Comment");
			stage.setScene(new Scene(root, 600, 600));
			stage.initStyle(StageStyle.UTILITY);
			stage.show();
	
			Main.setClosable(false);
			stage.setOnCloseRequest(e-> Main.setClosable(true));
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void back(ActionEvent event) {
		try {
			// does not open popup, just switches scene
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ViewTickets.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			scene = new Scene(root, 1000, 600);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("View Ticket");
			stage.show();

			Main.setClosable(false);
			stage.setOnCloseRequest(e -> Main.setClosable(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private Ticket currentTicket;

    public void setTicket(Ticket ticket) {
        this.currentTicket = ticket;
        updateTicketInfo();
    }

    private void updateTicketInfo() {
        if (currentTicket != null) {
            ticketTitle.setText("Ticket Title: " + currentTicket.getTitle());
            ticketID.setText("Ticket ID: " + currentTicket.getId());
        }
    }

}
