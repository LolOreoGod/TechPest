package controller;

import application.Main;
import application.DatabaseHelper;  // Assuming this is where your DatabaseHelper class is located.
import projects.Project;  // Import your Project class.
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

/**
 * 
 * MainMenuController controls all popups that may occur
 * It also controls closability during popups
 *
 */
public class MainMenuController {

	@FXML Button newProject;
	public void handleButtonAction(ActionEvent event) {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NewProjectPageSB.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("New Project");
			stage.setScene(new Scene(root, 600, 600));
			stage.initStyle(StageStyle.UTILITY);
			stage.show();
	
			Main.setClosable(false);
			stage.setOnCloseRequest(e-> Main.setClosable(true));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML Button ExistenceProject;
	public void showExistenceProject(ActionEvent event) throws IOException{
		root = FXMLLoader.load(getClass().getResource("/view/ExistenceProject.fxml"));
		stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.setTitle("Existence Project");
		stage.setScene(new Scene(root, 1000, 600));
		stage.show(); 
	}
    // New method to display the projects
    public void displayProjects() {
        List<Project> allProjects = DatabaseHelper.getAllProjects();
        for (Project project : allProjects) {
            System.out.println(project);
        }    
    }

    // Handler for the View Projects button
    @FXML
    public void handleViewProjects(ActionEvent event) {
        displayProjects();
    }
    
    private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void Back(ActionEvent event) throws IOException{
		root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}

