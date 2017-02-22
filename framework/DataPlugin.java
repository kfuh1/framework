package edu.cmu.cs.cs214.hw5.framework;

import java.util.List;

/**
 * interface for data plugins 
 * @author zhichunl, kfuh
 *
 */
public interface DataPlugin {

	/**
	 * asks the data plugin to fetch a certain amount of data
	 * @param num the number of dataunits
	 * @return the list of random data units
	 */
	List<DataUnit> fetchData(int num);
	/**
	 * asks the data plugin to fetch data for a particular user.
	 * @param num number of dataunits
	 * @param username name of user of interest
	 * @return list of data units associated with user and the
	 * user's followers.
	 */
	List<DataUnit> fetchData(int num, String username);
	
	/**
	 * gets the name of the data plugin
	 * @return the name of the data plugin
	 */
	String getName();
}
