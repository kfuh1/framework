package edu.cmu.cs.cs214.hw5.framework;

/**
 * class to represent DataUnits which are units of data
 * that constraint data plugin on the type of input passed
 * in.
 * @author zhichunl
 *
 */

public class DataUnit {
	
	private User assoUser;
	private Location loc;
	private String message;
	
	/**
	 * constructor for DataUnit
	 * @param user the user
	 * @param loc1 the location 
	 * @param mes the message
	 */
	public DataUnit(User user, Location loc1, String mes){
		assoUser = user;
		loc = loc1;
		message = mes;
	}

	/**
	 * @return the assoUser
	 */
	public User getAssoUser() {
		return assoUser;
	}

	/**
	 * @return the loc
	 */
	public Location getLoc() {
		return loc;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
}
