package Controllers;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NewProjectPageController {
	/**
	 * 
	 * NewProjectPageController controls all actions on New Project window
	 * This includes control over creation of new projects
	 *
	 */

	
	@FXML
    private Button back;

    @FXML
    private Button submit;

   /**
    * 
    * @param event When back button is pressed, window exits, 
    * 			   Main Menu set to closable
    */
    @FXML
    public void back(ActionEvent event) {
    	Main.setClosable(true);
    	Stage stage = (Stage) back.getScene().getWindow();
    	stage.close();
    }
    /**
     * 
     * @param event When submit button is pressed, fields are saved, window exits, 
     * 				Main Menu set to closable
     */
    @FXML
    public void submit(ActionEvent event) {
    	//TODO: Save information
    	Main.setClosable(true);
    	Stage stage = (Stage) submit.getScene().getWindow();
    	stage.close();
    	
    }
    
    
    
    

    
}
