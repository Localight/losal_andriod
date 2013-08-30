package co.localism.losal.objects;

public class Notice {
//	private String headline;
	private String title = "";
	private String details = "";
	private String image_url="";
	private String link_url="";
	private String button_link="";
	private String button_text="";
	private String teaser="";

	
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
	
	public void setButtonLink(String buttonLink){
		this.button_link = buttonLink;
	}
	public String getButtonLink() {
		return this.button_link;
		
	}
	
	public void setButtonText(String buttonText){
		this.button_text = buttonText;
	}
	public String getButtonText() {
		return this.button_text;
		
	}
	
	
	public void setTeaser(String teaser){
		this.teaser = teaser;
	}
	public String getTeaser() {
		return this.teaser;
		
	}
	
	
}
