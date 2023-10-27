package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import application.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import projects.Project;

public class OldExistenceProjectController implements Initializable{

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
    

}
