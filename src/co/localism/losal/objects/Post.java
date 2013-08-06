package co.localism.losal.objects;

public class Post {
	private String socialNetworkName= "";
	public String name = "";
	public int class_year = 0;
//	private timestamp;
//	private img user_icon;
	private String text = "";
	private String url ="";
	
	
	public Post(){
		
	}
	
	public Post(String name, int year){
		this.name = name;
		this.class_year = year;
	}
	
	
	
	
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	
	public void setText(String text){
		this.text = text;
	}
	public String getText(){
		return this.text;
	}
	
	public void setSocialNetworkName(String socialNetworkName){
		this.socialNetworkName = socialNetworkName;
	}
	public String getSocialNetworkName(){
		return this.socialNetworkName;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return this.url;
	}
	
	
}
