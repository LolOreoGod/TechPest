package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * 
 * @author Mengting Chang
 * @author Dong Tang
 * @author Sadiya Rahman
 * @version 0.30, 10/14/23
 * 
 *
 */

public class Main extends Application {
	static Boolean canExit = true;
	@Override
	/**
	 * Loads GFX from MainMenu.fxml
	 * On any close window request, check if window is allowed to exit
	 * WE MIGHT NOT ALLOW WINDOW TO EXIT WHEN WE HAVE POP-UP OPEN
	 */
	public void start(Stage primaryStage) {
		try {
			// Initialize the database table.
			DatabaseHelper.createNewTable();
			DatabaseHelper.createNewTicketsTable();
			
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
			Scene scene = new Scene(root,800,800);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					if(!canExit) {
						event.consume();
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param closable Boolean to check if Main Menu can be closed
	 * 
	 */
	public static void setClosable(boolean closable) {
		if(closable) {
			canExit = true;
		}
		else {
			canExit = false;
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
