package edu.cmu.cs.cs214.hw5.framework;

/**
 * class that represents location
 * 
 * @author zhichunl
 *
 */
public class Location {

	private String city;
	private String state;

	/**
	 * constructor for Location
	 * 
	 * @param cityI
	 *            city input
	 * @param stateI
	 *            state input
	 */
	public Location(String cityI, String stateI) {
		city = cityI;
		state = stateI;
	}
	
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
}
