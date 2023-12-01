package projects;

import java.time.LocalDate;

public class Comment {
	private LocalDate date;
	private String comments;

	public Comment(LocalDate date, String comments) {
		this.date = date;
		this.comments = comments;
	}


	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
