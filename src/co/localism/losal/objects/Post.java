package co.localism.losal.objects;

import java.io.Serializable;
/**
 * This is the Post object class. 
 * @author Joe
 *
 */
public class Post implements Serializable{
	private String socialNetworkName= "";
	private String name = "";
	private int class_year = 0;
//	private timestamp;
//	private img user_icon;
	private String user_icon;

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
	
	public void setClassYear(int class_year){
		this.class_year = class_year;
	}
	public int getClassYear(){
		return this.class_year;
	}
	
	public void setUserIcon(String user_icon){
		this.user_icon = user_icon;
	}
	public String getUserIcon(){
		return this.user_icon;
	}
	
}
