package co.localism.losal.objects;

public class Notice {
//	private String headline;
	private String title = "";
	private String details = "";
	private String image_url="";
	private String link_url="";

	
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
	

	public void setImageUrl(String imageUrl) {
		this.image_url = imageUrl;
		
	}
	
	public String getImageUrl() {
		return this.image_url;
		
	}
	
	public void setLinkUrl(String linkUrl){
		this.link_url = linkUrl;
	}
	public String getLinkUrl() {
		return this.link_url;
		
	}
	
}
