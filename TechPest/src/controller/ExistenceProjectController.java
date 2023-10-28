package controller;

import application.DatabaseHelper;
import application.Main;
import projects.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ExistenceProjectController implements Initializable {

	@FXML
	private TextField searchField;

	@FXML
	private TableView<Project> TableView;

	@FXML
	private TableColumn<Project, String> IDColumn;

	@FXML
	private TableColumn<Project, String> ProjectNameColumn;

	@FXML
	private TableColumn<Project, String> DescriptionColumn;

	@FXML
	private TableColumn<Project, LocalDate> InvoiceDateColumn;

	@FXML
	private TableColumn<Project, LocalDate> LastUpdatedColumn;

	@FXML
	private TableColumn<Project, String> StatusColumn;

	@FXML
	private TableColumn<Project, String> ActionsColumn;

	@FXML
	private Button newTicketButton;

	private Stage stage;
	private Scene scene;

	// private ObservableList<Project> projectList =
	// FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		ProjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		InvoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

		List<Project> projectList = DatabaseHelper.getAllProjects();
		ObservableList<Project> observableList = FXCollections.observableArrayList(projectList);
		TableView.setItems(observableList);

		TableView.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				// on double click, open view ticket
				if (TableView.getSelectionModel().getSelectedItem() != null) {
					String projName = TableView.getSelectionModel().getSelectedItem().getName();
					viewTickets(projName, event);
				}
			}
		});
	}

    @FXML
    void clearAll(ActionEvent event) {
    	DatabaseHelper.clearProjectTable();
    	DatabaseHelper.clearTicketsTable();
		refreshTable();
    }
    
	private void refreshTable() {
		List<Project> projectList = DatabaseHelper.getAllProjects();
		ObservableList<Project> observableList = FXCollections.observableArrayList(projectList);
		TableView.setItems(observableList);
	}

	@FXML
	public void newTicket() {
		System.out.println("View ticket");
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NewTicket.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("New Ticket");
			stage.setScene(new Scene(root, 476, 546));
			stage.initStyle(StageStyle.UTILITY);
			stage.show();

			Main.setClosable(false);
			stage.setOnCloseRequest(e -> Main.setClosable(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewTickets(String projName, Event event) {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ViewTickets.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			scene = new Scene(root, 1000, 600);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("View Ticket");
			stage.show();

			// Main.setClosable(false);
			// stage.setOnCloseRequest(e-> Main.setClosable(true));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private Stage primaryStage;
	public void backToMain(ActionEvent event) {
		try {
			// does not open popup, just switches scene
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
			Scene scene = new Scene(root, 800, 800);
			primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
