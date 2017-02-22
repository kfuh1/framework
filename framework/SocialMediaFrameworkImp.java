package edu.cmu.cs.cs214.hw5.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

/**
 * the framework class that handles
 * mostly registering and storing the 
 * initial data and facilitates interactions
 * between gui and anlysis set.
 * @author zhichunl
 *
 */
public class SocialMediaFrameworkImp implements SocialMediaFramework {
	
	private ArrayList<DataPlugin> dps;
	private ArrayList<AnalysisPlugin> aps;
	private HashMap<JPanel, AnalysisSet> setRel;
	private HashMap<DataPlugin, List<DataUnit>> cached;
	private static final int NUM = 10;
	
	public SocialMediaFrameworkImp(){
		dps = new ArrayList<DataPlugin>();
		aps = new ArrayList<AnalysisPlugin>();
		setRel = new HashMap<JPanel, AnalysisSet>();
		cached = new HashMap<DataPlugin, List<DataUnit>>();
	}

	/** {@inheritDoc} */
	public void registerDataPlugin(DataPlugin dp) {
		dps.add(dp);
		cached.put(dp, dp.fetchData(NUM));
	}

	/** {@inheritDoc} */
	public void registerAnalysisPlugin(AnalysisPlugin ap) {
		aps.add(ap);
	}

	/** {@inheritDoc} */
	public void selectedDataAnalysis(JPanel tab, DataPlugin dp,
			AnalysisPlugin ap) {
		AnalysisSet as = new AnalysisSet(dp, ap, tab, cached.get(dp), this);
		setRel.put(tab, as);
	}

	/** {@inheritDoc} */
	public List<DataPlugin> getAllDataPlugin() {
		return dps;
	}

	/** {@inheritDoc} */
	public List<AnalysisPlugin> getAllAnalysisPlugin() {
		return aps;
	}

	/** {@inheritDoc} */
	public void delTab(JPanel tab) {
		setRel.remove(tab);
	}

	/** {@inheritDoc} */
	public HashMap<String, ArrayList<DataUnit>> invertedIndex(
			List<DataUnit> dus) {
		HashMap<String, ArrayList<DataUnit>> iI = new HashMap<String, ArrayList<DataUnit>>();
		for (DataUnit du : dus){
			String[] wordList = du.getMessage().split("\\s*(\\?|\\!|\\.|,|\\s)\\s*");
			for (int i = 0; i < wordList.length; i++){
				if (iI.containsKey(wordList[i])){
					iI.get(wordList[i]).add(du);
				}
				else{
					ArrayList<DataUnit> units = new ArrayList<DataUnit>();
					units.add(du);
					iI.put(wordList[i], units);
				}
			}
		}
		return iI;
	}

}
