package edu.cmu.cs.cs214.hw5.plugin;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.cs214.hw5.framework.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.DataUnit;
import edu.cmu.cs.cs214.hw5.framework.User;
import twitter4j.*;

/**
 * A Data Plugin for our social media framework that uses twitter
 * @author ryanarcher
 *
 */

public class TwitterDataPlugin implements DataPlugin {
	
	private List<String> userNames;
	private Twitter twitter;
	
	private static final String[] DEFAULT_USERS = {"AndrewWheating",
		"RA_Andrews","G_Rupp","CDerrickRun","runmarycain","_EricJenkins"};
	
	private static final int LAST = 5;
	private static final int MAX = 20;
	
	/**
	 * constructor
	 */
	public TwitterDataPlugin() {
		twitter = new TwitterFactory().getInstance();
		userNames =  new ArrayList<String>();
		twitter4j.User tUser = null;
		try {
			tUser = twitter.showUser(DEFAULT_USERS[LAST]);
		}
		catch(TwitterException e) {
			System.out.println(e.getMessage());
		}
		IDs ids = null;
		try {
			ids = twitter.getFollowersIDs(tUser.getId(), -1);
		}
		catch(TwitterException e) {
			System.out.println(e.getMessage());
		}
		long[] id = ids.getIDs();
		for(int i = 0; i < MAX; i++) {
			try {
				userNames.add(twitter.showUser(id[i]).getScreenName());
			}
			catch(TwitterException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	/** {@inherit} */
	public List<DataUnit> fetchData(int num, String name) {
		List<DataUnit> data = new ArrayList<DataUnit>();
		twitter4j.User tUser = null;
		try {
			tUser = twitter.showUser(name);
		}
		catch(Exception e) {
			System.out.print(e.getMessage());
		}
		Status status = tUser.getStatus();
		int id = (int)tUser.getId();
		User user = new User(id,name,status.getCreatedAt());
		DataUnit du = new DataUnit(user,null,status.getText());
		data.add(du);
		return data;
	}

	/** {@inherit} */
	@Override
	public List<DataUnit> fetchData(int num) {
		List<DataUnit> data = new ArrayList<DataUnit>();
		int id;
		User user;
		twitter4j.User tUser = null;
		Status status = null;
		String name;
		for(int i = 0; i < num; i++) {
			name = userNames.get(i);
			try {
				tUser = twitter.showUser(name);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
			try {
			    id = (int)tUser.getId();
	            status = tUser.getStatus();
	            if(status == null) continue;
	            user = new User(id,name,status.getCreatedAt());
	            data.add(new DataUnit(user,null,status.getText()));
			}
			catch(Exception e){
			    System.out.println("API rate limit exceeded");
			}
			
		}
		return data;
	}	
	/** {@inherit} */
    public String getName(){
        return "Twitter Data Plugin";
    }

}
