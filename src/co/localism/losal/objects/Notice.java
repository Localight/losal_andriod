package co.localism.losal.objects;

public class Notice {
//	private String headline;
	private String title = "";
	private String details = "";
//	image
	public Notice(){
	}
	
	public Notice(String headline){
		this.title = title;
	}
	
	
	
	
	public String getTitle(){
		return this.title;
	}
	
	public String getDetails(){
		return this.details;
	}

	public void setTitle(String title) {
		this.title = title;
		
	}

	public void setDetails(String details) {
		this.details = details;
		
	}
	
}
