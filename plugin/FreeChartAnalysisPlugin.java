package edu.cmu.cs.cs214.hw5.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw5.framework.AnalysisPlugin;
import edu.cmu.cs.cs214.hw5.framework.DataUnit;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * a simple analysis plugin that uses free charts
 * @author zhichunl
 *
 */

public class FreeChartAnalysisPlugin implements AnalysisPlugin {
	
	private HashMap<String, ArrayList<DataUnit>> invertedDus;
	
	/**
	 * constructor for the plugin 
	 */
	public FreeChartAnalysisPlugin(){
		invertedDus = new HashMap<String, ArrayList<DataUnit>>();
	}

	/** {@inheritDoc} */
	public void customize(JPanel panelI) {
		update(panelI);
	}

	/** {@inheritDoc} */
	public void receiveData(List<DataUnit> dusI,
			HashMap<String, ArrayList<DataUnit>> invertedIndexI) {
		invertedDus = invertedIndexI;
	}
	
	/**
	 * updates the panel with new data
	 * @param panel panel to be updated by the analysis plugin
	 */
	private void update(JPanel panel){
		DefaultPieDataset ds = new DefaultPieDataset();
		String top1 = "";
		int num1 = 0;
		String top2 = "";
		int num2 = 0;
		String top3 = "";
		int num3 = 0;
		String top4 = "";
		int num4 = 0;
		for (Map.Entry<String, ArrayList<DataUnit>> k : invertedDus.entrySet()) {
			if (top1 == "") {
				top1 = k.getKey();
				num1 = k.getValue().size();
			} else if (k.getValue().size() > invertedDus.get(top1).size()) {
				String temp = top1;
				int tempNum = num1;
				top1 = k.getKey();
				num1 = k.getValue().size();
				top2 = temp;
				num2 = tempNum;
			} else if (top2 == "") {
				top2 = k.getKey();
				num2 = k.getValue().size();
			} else if (k.getValue().size() > invertedDus.get(top2).size()) {
				String temp = top2;
				int tempNum = num2;
				top2 = k.getKey();
				num2 = k.getValue().size();
				top3 = temp;
				num3 = tempNum;
			} else if (top3 == "") {
				top3 = k.getKey();
				num3 = k.getValue().size();
			} else if (k.getValue().size() > invertedDus.get(top3).size()) {
				String temp = top3;
				int tempNum = num3;
				top3 = k.getKey();
				num3 = k.getValue().size();
				top4 = temp;
				num4 = tempNum;
			} else if (top4 == ""
					|| k.getValue().size() > invertedDus.get(top4).size()) {
				top4 = k.getKey();
				num4 = k.getValue().size();
			}
		}
		int total = num1 + num2 + num3 + num4;
		if (total == 0){
			total = 1;
		}
		ds.setValue(top1, ((double)num1)/total);
		ds.setValue(top2, ((double)num2)/total);
		ds.setValue(top3, ((double)num3)/total);
		ds.setValue(top4, ((double)num4)/total);
		JFreeChart chart = ChartFactory.createPieChart("Top 4 most used words", ds);
		ChartPanel chartPanel = new ChartPanel(chart);
		panel.add(chartPanel);
	}
	/** {@inherit} */
	public String getName(){
	    return "Free Chart Analysis Plugin";
	}
}
