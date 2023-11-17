package controller;

import application.CommonObjs;
import application.DatabaseHelper;
import application.Main;
import projects.Comment;
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
import java.util.stream.Collectors;


//new added
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import java.io.IOException;
import javafx.stage.Modality;

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
	private TableColumn<Project, Void> ActionsColumn;  //Changed here String to Void

	@FXML
	private Button newTicketButton;
	@FXML
	private Button addComment;
	
	@FXML
	private Button searchButton;

	private Stage stage;
	private Scene scene;
	private Project selectedProject;
	private CommonObjs common = CommonObjs.getInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		ProjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		InvoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

		List<Project> projectList = DatabaseHelper.getAllProjects();
		ObservableList<Project> observableList = FXCollections.observableArrayList(projectList);
		TableView.setItems(observableList);
		
		
		common.setProjectTable(TableView);
		/**
		 * DOUBLE CLICK EVENT
		 */
		TableView.setOnMouseClicked(event -> {
			//IF DOUBLE CLICK
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				selectedProject = TableView.getSelectionModel().getSelectedItem();
				if (selectedProject != null) {
					String projName = TableView.getSelectionModel().getSelectedItem().getName();
					viewTickets(projName, event);
				}
			}
		});
		
		setupActionsColumn(); //new line added
	}
	
	
	
	
	//new code
	private void setupActionsColumn() {
        ActionsColumn.setCellFactory(param -> new TableCell<Project, Void>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Project project = getTableView().getItems().get(getIndex());
                    handleEditAction(project);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
    }



	private void handleEditAction(Project project) {
	    try {
	        
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditProject.fxml"));
	        Parent root = loader.load();

	      
	        EditProjectController editController = loader.getController();
	        editController.setProject(project);

	        
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Edit Project");
	        stage.initModality(Modality.APPLICATION_MODAL);
	        
	        
	        stage.setOnHidden(event -> refreshTable());
	        
	       
	        stage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}






	
	@FXML
    void handleSearchAction(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();
 
        if (searchText.isEmpty()) {
            refreshTable();
            return;
        }

        List<Project> filteredProjects = DatabaseHelper.getAllProjects().stream()
                .filter(project -> project.getName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        ObservableList<Project> observableList = FXCollections.observableArrayList(filteredProjects);


        if (observableList.isEmpty()) {
            TableView.setPlaceholder(new Label("No projects found with the search term."));
        } else {
            TableView.setPlaceholder(new Label("")); 
        }

        TableView.setItems(observableList);
    }

	@FXML
	void deleteProject(ActionEvent event) {
	    Project selectedProject = TableView.getSelectionModel().getSelectedItem();
	    if(selectedProject != null) {
		    DatabaseHelper.deleteProjectCascade(selectedProject);
			refreshTable();
	    }else {
	    	System.out.print("Please select a project");
	    }
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
			stage.setOnCloseRequest(e -> Main.setClosable(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			common.setSelectedProject(selectedProject);
			
			
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

	public static void setClosable(boolean closable) {
		boolean canExit;
		if (closable) {
			canExit = true;
		} else {
			canExit = false;
		}
	}
}
