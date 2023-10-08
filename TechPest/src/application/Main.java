package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	static Boolean canExit = true;
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
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
	
	public static void setClosable(boolean closable) {
		if(closable) {
			canExit = true;
		}
		else {
			canExit = false;
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
