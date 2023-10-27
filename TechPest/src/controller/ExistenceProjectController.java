package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import application.DatabaseHelper;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import projects.Project;

public class ExistenceProjectController implements Initializable{

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
    private Button clearAll;
    
    @FXML
    private Button NewTicket;
    
    @FXML
    private Button AddComments;
    
    @FXML
    private Button Back;
    
    @FXML
    public void clearAll() {
        DatabaseHelper.clearProjectTable();
        refreshTable();
    }
   
    public void initialize(URL location, ResourceBundle resources) {
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        InvoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
   
        List<Project> projectList = DatabaseHelper.getAllProjects();
        ObservableList<Project> observableList = FXCollections.observableArrayList(projectList);
        TableView.setItems(observableList);
    }
    
    private void refreshTable() {
        List<Project> projectList = DatabaseHelper.getAllProjects();
        ObservableList<Project> observableList = FXCollections.observableArrayList(projectList);
        TableView.setItems(observableList);
    }
    
    private Stage stage;
	private Scene scene;
	private Parent root;
    @FXML
    public void Back(ActionEvent event) throws IOException {
    	root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
    }
    
    @FXML
    public void NewTicket(ActionEvent event) {
        
    }
    
    @FXML
    public void AddComments(ActionEvent event) {
        
    }

}
