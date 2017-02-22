package edu.cmu.cs.cs214.hw5.plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.TextPost;

import edu.cmu.cs.cs214.hw5.framework.Post;
import edu.cmu.cs.cs214.hw5.framework.UserData;

/**
 * Tumblr Data Plugin
 * @author ryanarcher
 *
 */

public class TumblrDataPlugin implements DataPlugin {
	
	private static final String NAME = "Tumblr Plugin";
	private static final String CONSUMER_KEY = 
			"0CBxdl7s0iKAfOW9twnkhdOapQmzCtfvJS9rR9T6JydhD0cNz8";
	private static final String SECRET_KEY = 
			"5XSSuK0RqDHspwLDktF94sATA0wIrTAlSnKCGhcKean1CjCnOk";

	/** {@inherit} */
	public String getName() {
		return NAME;
	}

	/** {@inherit} */
	public UserData getUserData(String userName) {
		UserData ud = new UserData(userName,getName());
		JumblrClient jumblr = new JumblrClient(CONSUMER_KEY,SECRET_KEY);
		jumblr.setToken(
				  "rD6CwySEMFIhj4LABh4QTsEFxQszdiUnGXQi4823GHEf8hFDoT",
				  "7Zh1yS8VZhHyTChurokS30mt8ipkO7k7gwh1f6aJaIfWzKt40S"
				);
		Blog blog = jumblr.blogInfo(userName);
		ud.setFollowers(blog.getFollowersCount());
		Map<String,String> options = new HashMap<String,String>();
		// only accept text posts
		options.put("notes_info","true");
		options.put("type", "text");
		List<com.tumblr.jumblr.types.Post> tumblrPosts = blog.posts(options);
		Date dt = null;
		// parse date as GMT
	    SimpleDateFormat sdf = 
	    	      new SimpleDateFormat("yyyy-mm-dd hh:mm:ss zzz");
		for(com.tumblr.jumblr.types.Post p : tumblrPosts) {
			try {
				dt = sdf.parse(p.getDateGMT());
			}
			catch(Exception e) {
				ud.setErroMsg("Parsing date error");
			}
			TextPost tp = (TextPost)p;
			ud.addPost(new Post(tp.getBody(),dt));
		}
		// tumblr does not supply an id but could use hashcode 
		// as id for blog object
		ud.setId(Integer.toString(blog.hashCode()));
		return ud;
	}

}
