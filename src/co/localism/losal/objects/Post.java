package co.localism.losal.objects;

public class Post {
	private String social_source = "";
	public String Name = "";
	public int class_year = 0;
//	private timestamp;
//	private img user_icon;
	private String comment = "";
	private String img_URL ="";
	
	
	public Post(){
		
	}
	
	public Post(String name, int year){
		this.Name = name;
		this.class_year = year;
	}
	
	
}
