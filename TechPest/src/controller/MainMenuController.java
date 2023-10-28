package controller;

import application.Main;
import application.DatabaseHelper;  
import projects.Project;  
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.List;

/**
 * 
 * MainMenuController controls all popups that may occur
 * It also controls closability during popups
 *
 */
public class MainMenuController {

	@FXML Button newProject;
	@FXML Button ExistenceProject;
	private Stage stage;
	private Scene scene;

	// This method remains unchanged
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
	
	// This method remains unchanged
	public void showExistenceProject(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ExistenceProject.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			stage = new Stage();
			scene = new Scene(root, 1000, 600);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.setTitle("Existing Project");
			stage.show();

			Main.setClosable(false);
			stage.setOnCloseRequest(e -> Main.setClosable(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    // This method remains unchanged
    public void displayProjects() {
        List<Project> allProjects = DatabaseHelper.getAllProjects();
        for (Project project : allProjects) {
            System.out.println(project);
        }
    }

    // This method remains unchanged
    @FXML
    public void handleViewProjects(ActionEvent event) {
        displayProjects();
    }
}
