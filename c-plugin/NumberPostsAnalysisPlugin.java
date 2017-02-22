package edu.cmu.cs.cs214.hw5.plugin;

import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import edu.cmu.cs.cs214.hw5.framework.AnalyzedData;
import edu.cmu.cs.cs214.hw5.framework.Post;

/**
 * analysis plugin that analyzes number of posts from users
 * @author ryanarcher
 *
 */

public class NumberPostsAnalysisPlugin implements AnalysisPlugin {
	
	private HashMap<String,Integer> numPosts;

	/** {@inherit} */
	public JPanel analyzeData(List<AnalyzedData> analyzedDatas) {
		numPosts = new HashMap<String,Integer>();
		int size = 0;
		int total = 0;
		List<Post> posts = null;
		for(AnalyzedData d : analyzedDatas) {
			posts = d.getPosts();
			size = posts.size();
			numPosts.put(d.getName(),size);
			total += size;
		}
        PieDataset dataSet = createDataSet(total);
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Number of Posts",  // chart title
                dataSet,             // data
                true,               // include legend
                true,
                false
            );
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        JPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}
    
    /**
     * create the data set for the pie chart
     * @param map hashmap mapping index in time range to number of posts
     *  in that time
     * @param total the total number of posts in the data
     * @return a pie data set that reflects the data in the map
     */
    private PieDataset createDataSet(int total) {
        DefaultPieDataset data = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : numPosts.entrySet()) {
            String user = entry.getKey();
            int posts = entry.getValue();
            data.setValue(user, (double)posts / total);
        }
        return data;
    }
	

    /** {@inherit} */
	public String getName() {
		return "Number of Posts";
	}

}
