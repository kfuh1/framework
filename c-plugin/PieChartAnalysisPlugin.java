package edu.cmu.cs.cs214.hw5.plugin;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import edu.cmu.cs.cs214.hw5.framework.AnalyzedData;

public class PieChartAnalysisPlugin implements AnalysisPlugin {

	/** {@inheritDoc} */
	public JPanel analyzeData(List<AnalyzedData> analyzedDatas) {
		DefaultPieDataset ds = new DefaultPieDataset();
		String top1 = "";
		int num1 = 0;
		String top2 = "";
		int num2 = 0;
		String top3 = "";
		int num3 = 0;
		String top4 = "";
		int num4 = 0;
		for (AnalyzedData ad : analyzedDatas) {
			for (Map.Entry<String, Integer> k : ad.getWordsCount().entrySet()) {
				if (top1 == "") {
					top1 = k.getKey();
					num1 = k.getValue();
				} else if (k.getValue() > ad.getWordsCount().get(top1)) {
					String temp = top1;
					int tempNum = num1;
					top1 = k.getKey();
					num1 = k.getValue();
					top2 = temp;
					num2 = tempNum;
				} else if (top2 == "") {
					top2 = k.getKey();
					num2 = k.getValue();
				} else if (k.getValue() > ad.getWordsCount().get(top2)) {
					String temp = top2;
					int tempNum = num2;
					top2 = k.getKey();
					num2 = k.getValue();
					top3 = temp;
					num3 = tempNum;
				} else if (top3 == "") {
					top3 = k.getKey();
					num3 = k.getValue();
				} else if (k.getValue() > ad.getWordsCount().get(top3)) {
					String temp = top3;
					int tempNum = num3;
					top3 = k.getKey();
					num3 = k.getValue();
					top4 = temp;
					num4 = tempNum;
				} else if (top4 == ""
						|| k.getValue() > ad.getWordsCount().get(top4)) {
					top4 = k.getKey();
					num4 = k.getValue();
				}
			}

		}
		int total = num1 + num2 + num3 + num4;
		if (total == 0) {
			total = 1;
		}
		ds.setValue(top1 + ": " + (100 * (double) num1) / total + "%",
				((double) num1) / total);
		ds.setValue(top2 + ": " + (100 * (double) num2) / total + "%",
				((double) num2) / total);
		ds.setValue(top3 + ": " + (100 * (double) num3) / total + "%",
				((double) num3) / total);
		ds.setValue(top4 + ": " + (100 * (double) num4) / total + "%",
				((double) num4) / total);
		JFreeChart chart = ChartFactory
				.createPieChart(
						"Top 4 most used words overall: max of number of times each word is used over each analyzed data",
						ds);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setSize(new Dimension(75, 75));
		return chartPanel;
	}

	/** {@inheritDoc} */
	public String getName() {
		return "PieChart";
	}

}
