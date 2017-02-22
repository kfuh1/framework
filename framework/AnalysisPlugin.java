package edu.cmu.cs.cs214.hw5.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

/**
 * interface for analysis plugins
 * 
 * @author zhichunl
 *
 */
public interface AnalysisPlugin {

	/**
	 * function to pass in the panel for the analysis plugin to customize
	 * 
	 * @param panel
	 *            the panel passed in
	 */
	void customize(JPanel panel);

	/**
	 * passes in the data received from the data plugin and also preforms
	 * inverted index to provide for more interesting analysis.
	 * 
	 * @param dus
	 * @param invertedIndex
	 */
	void receiveData(List<DataUnit> dus,
			HashMap<String, ArrayList<DataUnit>> invertedIndex);
	
	/**
	 * gets the name of the analysis plugin
	 * @return the name
	 */
	String getName();
}
