package Controllers;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NewProjectPageController {

	
	@FXML
    private Button back;

    @FXML
    private Button submit;

   
    @FXML
    public void back(ActionEvent event) {
    	Main.setClosable(true);
    	Stage stage = (Stage) back.getScene().getWindow();
    	stage.close();
    }

    @FXML
    public void submit(ActionEvent event) {
    	//TODO: Save information
    	Stage stage = (Stage) back.getScene().getWindow();
    	stage.close();
    	
    }
    
    

    
}
