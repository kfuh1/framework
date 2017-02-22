package edu.cmu.cs.cs214.hw5.plugin;

import java.util.ArrayList;

import edu.cmu.cs.cs214.hw5.framework.Post;
import edu.cmu.cs.cs214.hw5.framework.UserData;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.IdNameEntity;
import facebook4j.Page;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.api.FriendMethods;
import facebook4j.api.PostMethods;
import facebook4j.api.UserMethods;
import facebook4j.auth.AccessToken;

/**
 * facebook data plugin for team 30's framework since only pages like CNN have
 * public posts that we can access. This method first finds the page that is
 * closest with the user name and then finds the user based on the id of the
 * page. Then, access the posts. According to Team 30, since we can't access the
 * posts of private users, we can set an error when request for private users.
 * 
 * @author Team27
 *
 */
public class FacebookDataPlugin implements DataPlugin {

	private Facebook fb;
	private static final String appID = "853030851428670";
	private static final String appSecret = "993a50d6a3be26e017c10853db85e62d";
	private static final String accessToken = "CAACEdEose0cBAPafVZBsWLCrp8cRHxA6Qz0qtPYZCi5iDdeokrOsEzKXGHd9YhUfo5erp4I2LgqlDf4h9d8DwDiZC892krzG56WyTxHAEuZCwJaOfGP2inCGB0knaGriKhyZBuKex4lK3GqQ3RQ28TsBU3j0EZBecR0OvFG3gFNe9GR4JCsz5obPSXXSrPgz1hhiKVXBzyY0IJOR1AU8ZCyIq9vUTAQD2u8soMh7h4r6AZDZD";

	/**
	 * constructor for facebook data plugin
	 */
	public FacebookDataPlugin() {
		fb = new FacebookFactory().getInstance();
		fb.setOAuthAppId(appID, appSecret);
		fb.setOAuthPermissions("email,publish_stream,...");
		fb.setOAuthAccessToken(new AccessToken(accessToken));
	}

	/** {@inheritDoc} */
	public String getName() {
		return "FacebookData";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FacebookException
	 */
	public UserData getUserData(String userName) {
		ResponseList<Page> pages = null;
		UserMethods um = fb.users();
		UserData ud = new UserData(userName, getName());
		try {
			pages = fb.searchPages(userName);
		} catch (FacebookException e) {
			ud.setErroMsg("no associated users found");
		}
		if (pages == null || pages.isEmpty()) {
			ud.setErroMsg("cannot find the user, might be a private account");
			return ud;
		}
		User user = null;
		try {
			user = um.getUser(pages.get(0).getId());
		} catch (FacebookException e1) {
			ud.setErroMsg("no user exists.");
		}
		PostMethods ps = fb.posts();
		IdNameEntity loc = user.getLocation();
		if (loc != null) {
			ud.setLocation(loc.getName());
		}
		ResponseList<facebook4j.Post> uPosts = null;
		ArrayList<Post> posts = new ArrayList<Post>();
		try {
			uPosts = ps.getPosts(user.getId());
		} catch (FacebookException e) {
			ud.setErroMsg("error in finding posts");
		}
		if (uPosts != null && !uPosts.isEmpty()) {
			for (int i = 0; i < uPosts.size(); i++) {
				Post newP = new Post(uPosts.get(i).getMessage(), uPosts.get(i)
						.getCreatedTime());
				posts.add(newP);
			}
		}
		ud.setPostList(posts);
		FriendMethods fm = fb.friends();
		int followers = 0;
		try {
			followers = fm.getFriendlists(user.getId()).size();
		} catch (FacebookException e) {
			// do nothing
		}
		ud.setFollowees(followers);
		ud.setFollowers(followers);
		ud.setId(ud.getId());
		return ud;
	}
}
