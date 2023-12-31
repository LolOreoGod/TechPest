package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import application.CommonObjs;
import application.DatabaseHelper;
import application.Main;
import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import projects.Comment;
import projects.Project;
import projects.Ticket;

public class ViewCommentsController implements Initializable {
	@FXML
	private TableView<Comment> CommentTable;
	@FXML
	private TableColumn<Comment, LocalDate> DateColumn;
	@FXML
	private TableColumn<Comment, String> commentColumn;
	
	@FXML
	private TableColumn<Comment, Void> ActionsColumn;
	@FXML
	private Button back;

	@FXML
	private Button addComment;
	@FXML
	private MenuButton ticketDropDown;

	@FXML
	private Label ticketTitle;

	@FXML
	private Label ticketID;
	
	@FXML
	private Button refreshButton;
	 
	@FXML
	private Button deleteCommentButton;
	
	@FXML
	private Button clearButton;
	
	@FXML
	private ListView<String> commentsListView; 

	private Stage stage;
	private Scene scene;
	

    //@FXML
    //private ListView<String> ticketList;
    
    private List<Comment> commentList;
    private CommonObjs common = CommonObjs.getInstance();
    private Ticket selectedTicket; // newly added field

    public void initialize(URL location, ResourceBundle resources) {
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

        // Retrieving ticket info from common object
        selectedTicket = common.getSelectedTicket();
        ticketTitle.setText(ticketTitle.getText() + " " + selectedTicket.getTitle());
        ticketID.setText(ticketID.getText() + " " +  Integer.toString(selectedTicket.getId()));
        common.setCommentTable(CommentTable);

        // Fetch comments for the selected ticket
        this.commentList = DatabaseHelper.getCommentsForTicket(selectedTicket.getId());

        if (this.commentList == null) {
            // Handle the case when there's an error fetching comments
            return;
        }

        Platform.runLater(() -> {
            ObservableList<Comment> observableCommentList = FXCollections.observableArrayList(this.commentList);
            CommentTable.setItems(observableCommentList);
            CommentTable.refresh();
        });
        
        setupActionsColumn();

    }
    private void setupActionsColumn() {
        ActionsColumn.setCellFactory(param -> new TableCell<Comment, Void>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Comment comment = getTableView().getItems().get(getIndex());
                    common.setSelectedComment(comment);
                    handleEditAction(comment);
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
	
	private void handleEditAction(Comment comment) {
	    try {
	        
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditComment.fxml"));
	        Parent root = loader.load();
	        
	        
	        
	        Stage stage = new Stage();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Edit Comment");
	        stage.initModality(Modality.APPLICATION_MODAL);
	        
	        
	        stage.setOnHidden(event -> refreshCommentTable());
	        
	       
	        stage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
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
			//TODO: add window close guard here
			stage.setOnCloseRequest(e -> Main.setClosable(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML
	void back(ActionEvent event) {
		try {
			// does not open popup, just switches scene
			
			if(common.getEditView()) {
		
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/EditTicket.fxml"));
				Parent root = (Parent) fxmlLoader.load();
				scene = new Scene(root);
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.setTitle("Edit Ticket");
				stage.show();

				Main.setClosable(false);
				stage.setOnCloseRequest(e -> Main.setClosable(true));
				
			}
			
			else {
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ViewTickets.fxml"));
				Parent root = (Parent) fxmlLoader.load();
				scene = new Scene(root);
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.setTitle("View Ticket");
				stage.show();

				Main.setClosable(false);
				stage.setOnCloseRequest(e -> Main.setClosable(true));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	@FXML
	void handleRefresh() {
		System.out.println("table refreshed");
		
        List<Comment> allComments = DatabaseHelper.getCommentsForTicket(selectedTicket.getId());
        ObservableList<Comment> observableCommentList = FXCollections.observableArrayList(allComments);
        CommentTable.setItems(observableCommentList);
	}
	
	@FXML
	void handleDeleteComment(ActionEvent event) {
	    Comment selectedComment = CommentTable.getSelectionModel().getSelectedItem();

	    if (selectedComment != null) {
	        DatabaseHelper.deleteComment(selectedComment.getDate(), selectedComment.getComments());
	        refreshCommentTable();
	    } else {
	        // No comment selected, show a message or handle accordingly
	        System.out.println("No comment selected");
	    }
	}

	// Add this method to refresh the CommentTable after deleting a comment
	private void refreshCommentTable() {
	    List<Comment> allComments = DatabaseHelper.getCommentsForTicket(selectedTicket.getId());
	    ObservableList<Comment> observableCommentList = FXCollections.observableArrayList(allComments);
	    CommentTable.setItems(observableCommentList);
	}

	
	@FXML
	void handleClear() {
		DatabaseHelper.clearCommentsTable();
		handleRefresh();
	}
}
