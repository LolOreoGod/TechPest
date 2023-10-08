package controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
			//OPENS POP UP WINDOW
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
	
}
