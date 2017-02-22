package edu.cmu.cs.cs214.hw5.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

/**
 * interface of the frame work 
 * @author zhichunl
 *
 */
public interface SocialMediaFramework {
	
	/**
	 * registers a data plugin
	 * @param dp the data plugin
	 */
	void registerDataPlugin(DataPlugin dp);
	
	/**
	 * registers the analysis plugin
	 * @param ap the analysis plugin
	 */
	void registerAnalysisPlugin(AnalysisPlugin ap);
	
	/**
	 * invoked when a data plugin to analysis plugin pair
	 * is selected
	 * @param tab the jpanel
	 * @param dp the data plugin
	 * @param ap the analysis plugin
	 */
	void selectedDataAnalysis(JPanel tab, DataPlugin dp, AnalysisPlugin ap);
	
	/**
	 * getter for all data plugins
	 * @return list of all data plugins
	 */
	List<DataPlugin> getAllDataPlugin();
	
	/**
	 * getter for all analysis plugins
	 * @return list of all analysis plugins
	 */
	List<AnalysisPlugin> getAllAnalysisPlugin();
	
	/**
	 * call to delete an analysis set
	 * @param tab jpanel
	 */
	void delTab(JPanel tab);
	
	/**
	 * performs inverted index on the data
	 * @param dus the list of data
	 * @return the inverted index hash map
	 */
	HashMap<String, ArrayList<DataUnit>> invertedIndex(List<DataUnit> dus);
}
