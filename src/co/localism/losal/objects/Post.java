package co.localism.losal.objects;

import java.io.Serializable;
import java.util.Date;

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
	private String user_icon;
	private String social_network_post_id = "";
	private String text = "";
	private String url = "";
	private String parse_object_id = "";

	public Post() {

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
		this.user_icon = user_icon;
	}

	public String getUserIcon() {
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

	public String getSocialNetworkPostId(String social_network_post_id) {
		return this.social_network_post_id = social_network_post_id;
	}

	public void setParseObjectId(String parse_object_id) {
		this.parse_object_id = parse_object_id;
	}

	public String getParseObjectId(String parse_object_id) {
		return this.parse_object_id = parse_object_id;
	}
}
