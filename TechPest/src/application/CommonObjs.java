package application;

import javafx.scene.control.TableView;
import projects.Comment;
import projects.Project;
import projects.Ticket;

/*
 * The point of this singleton class is so we can transfer data between controllers
 * Only way this is possible is if we only had one instance of the class
 */
public class CommonObjs {
	
	
	private static CommonObjs commonObjs = new CommonObjs();
	private Ticket selectedTicket;
	private Project selectedProject;
	private Comment selectedComment;

	
	
	private TableView<Ticket> ticketTable;
	private TableView<Project> projectTable;
	private TableView<Comment> commentTable;
	
	//private Scene lastScene;
	
	//this determines where the back button leads to, in view comments
	private boolean editView;

	public boolean getEditView() {
		return editView;
	}
	
	public void setEditView(boolean editView) {
		this.editView = editView;
	}
	
	public TableView<Project> getProjectTable() {
		return projectTable;
	}

	public void setProjectTable(TableView<Project> projectTable) {
		this.projectTable = projectTable;
	}

	public TableView<Comment> getCommentTable() {
		return commentTable;
	}

	public void setCommentTable(TableView<Comment> commentTable) {
		this.commentTable = commentTable;
	}

	public TableView<Ticket> getTicketTable() {
		return ticketTable;
	}

	public void setTicketTable(TableView<Ticket> selectedTable) {
		this.ticketTable = selectedTable;
	}

	private CommonObjs() {
		
	}
	
	public static CommonObjs getInstance() {
		return commonObjs;
	}

	public static void setCommonObjs(CommonObjs commonObjs) {
		CommonObjs.commonObjs = commonObjs;
	}

	public Ticket getSelectedTicket() {
		return selectedTicket;
	}

	public void setSelectedTicket(Ticket selectedTicket) {
		this.selectedTicket = selectedTicket;
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public Comment getSelectedComment() {
		return selectedComment;
	}

	public void setSelectedComment(Comment selectedComment) {
		this.selectedComment = selectedComment;
	}

	
	
}
