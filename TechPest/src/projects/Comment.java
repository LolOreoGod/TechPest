package projects;

import java.sql.Timestamp;

public class Comment {
	
	private Timestamp timestamp;
	private String description;
	
	
	public Comment(String description) {
		if(description.equals("")) {
			return;
		}
		else {
			timestamp = new Timestamp(System.currentTimeMillis());
			this.description = description;
		
		}
	}
	
	
	public String getDescription() {
		return description;
	}
	
	public Timestamp getTime() {
		return timestamp;
	}

}
