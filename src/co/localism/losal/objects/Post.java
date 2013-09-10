package co.localism.losal.objects;

import java.io.Serializable;
import java.util.Date;

import android.util.Log;
import co.localism.losal.activities.MainActivity;

import com.parse.ParseObject;
import com.parse.codec.binary.StringUtils;

/**
 * This is the Post object class.
 * 
 * @author Joe
 * 
 */
public class Post implements Serializable {
	private String socialNetworkName = "";
	private String name = "";
	private String class_year = "";
	private Date post_time;
	// private img user_icon;
	private Character user_icon = ' ';
	private String social_network_post_id = "";
	private String text = "";
	private String url = "";
	private String parse_object_id = "";
	private String fave_color = "";
	private boolean user_liked = false;
	private boolean is_system_post = false;
	private String user_type = "";
	
	
	public Post() {
		this.setUserIcon("e017");
	}
	
	
	public Post(ParseObject po, ParseObject user){
		this(po, user, false);
	}
	public Post(ParseObject po,ParseObject user, Boolean forFilter){
		this.setPostTime(po.getDate("postTime"));
		if(!forFilter)
			MainActivity.LAST_POST_DATE = this.getPostTime();
		// (po.getString("featured"));
		this.setSocialNetworkPostId(po.getString(
				"socialNetworkPostID"));
		this.setParseObjectId(po.getObjectId());

//		Log.i(tag, "objectID: " + po.getObjectId());
		// if(user != null);
		try {
//			Log.e(tag, "name"							+ user.getString("firstName"));
			this.setUserType(user.getString("userType"));
			
			if(this.getUserType().equalsIgnoreCase("student")){
//				student users show as first name and first initial of last
				this.setName(user
						.getString("firstName"));
				String lname = user
						.getString("lastName");
				this.setName(this.getName() + " " + lname.substring(0, 1) + ".");
				this.setClassYear(user
						.getString("year"));
			}else{
//				faculty and staff show as prefix and last name
				this.setName(user
						.getString("prefix") + " " + user
						.getString("lastName"));
				this.setClassYear(this.getUserType());
			}
			
		
			this.setClassYear(user
					.getString("year"));
			this.setUserIcon(user
					.getString("icon"));
			this.setFaveColor(user
					.getString("faveColor"));
		} catch (Exception e) {
//			Log.e(tag, e.toString());
			this.setName("Unknown");// placeholder data
			this.setUserIcon("e017");
			this.setFaveColor("#FFFFFF");
		}
		// Log.d(tag, ""+
		// user.get("firstName"));

		this.setText(po.getString("text"));
		// Log.d(tag, po.getString("text"));
		this.setSocialNetworkName(po.getString(
				"socialNetworkName"));

		try {
			this.setUrl(po.getString("url"));
		} catch (NullPointerException npe) {
			this.setUrl("");
		}
	}

	public Post(String name, String year) {
		this.name = name;
		this.class_year = year;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
	
	public void setSocialNetworkName(String socialNetworkName) {
		this.socialNetworkName = socialNetworkName;
	}
	/**
	 * 
	 * @return
	 */
	public String getSocialNetworkName() {
		return this.socialNetworkName;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setClassYear(String class_year) {
		this.class_year = class_year;
	}

	public String getClassYear() {
		return this.class_year;
	}

	public void setUserIcon(String user_icon) {
		String s = ("\\u"+user_icon);
		char c = (char) Integer.parseInt( s.substring(2), 16 );
//		this.user_icon =  ("\\u"+user_icon).toCharArray()[0];
		this.user_icon = c;
	}

	public Character getUserIcon() {
		return this.user_icon;
	}

	public void setPostTime(Date date) {
		this.post_time = date;

	}

	public Date getPostTime() {
		return this.post_time;
	}

	public void setSocialNetworkPostId(String social_network_post_id) {
		this.social_network_post_id = social_network_post_id;
	}

	public String getSocialNetworkPostId() {
		return this.social_network_post_id;
	}

	public void setParseObjectId(String parse_object_id) {
		this.parse_object_id = parse_object_id;
	}

	public String getParseObjectId() {
		return this.parse_object_id;
	}

	public void setFaveColor(String fave_color) {
		this.fave_color  = fave_color;
	}
	
	public String getFaveColor() {
		return this.fave_color;
	}
	
	
	public void setUserLiked(boolean liked){
		this.user_liked = liked;
	}
	
	public boolean getUserLiked(){
		return this.user_liked;
	}
	public void setIsSystemPost(boolean isSystemPost){
		this.is_system_post = isSystemPost;
	}
	
	public boolean isSystemPost(){
		return this.is_system_post;
	}
	
	public void setUserType(String userType){
		this.user_type = userType;
	}
	
	public String getUserType(){
		return this.user_type;
	}
	
	
}
