package edu.cmu.cs.cs214.hw5.framework;

import java.util.Date;

/**
 * the user class that contains data about a user
 * @author zhichunl
 *
 */
public class User {
	
	private int id;
	private String name;
	private Date time;
	
	/**
	 * constructor for User
	 * @param idI the input id
	 * @param nameI the input name
	 * @param timeI the input time
	 */
	public User(int idI, String nameI, Date timeI){
		id = idI;
		name = nameI;
		time = timeI;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}
}
