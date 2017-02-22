package edu.cmu.cs.cs214.hw5.plugin;

import java.awt.Font;
import java.util.ArrayList;
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

import edu.cmu.cs.cs214.hw5.framework.AnalysisPlugin;
import edu.cmu.cs.cs214.hw5.framework.DataUnit;
import edu.cmu.cs.cs214.hw5.framework.User;

/**
 * sample analysis plugin that displays a pie chart representing
 * times when users post most often
 * @author ryanarcher
 *
 */

public class NumberPostsAnalysisPlugin implements AnalysisPlugin {

    private List<DataUnit> data;
    private HashMap<User,Integer> numPosts;

    @Override
    public void customize(JPanel panel) {
        update(panel);
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
        for (Map.Entry<User, Integer> entry : numPosts.entrySet()) {
            User user = entry.getKey();
            int posts = entry.getValue();
            data.setValue(user.getName(), (double)posts / total);
        }
        return data;
    }

    /**
     * receive the data from the data plugin associated with this analysis
     * @param dus the list of data units from data plugin
     * @param invertedIndex the hashmap of inverted indices
     */
    @Override
    public void receiveData(List<DataUnit> dus,
            HashMap<String, ArrayList<DataUnit>> invertedIndex) {
        data = dus;
    }
    
    /**
     * turn data units into hashmap to be used in pie chart
     * @return the hashmap
     */
    private HashMap<User,Integer> parseDataUnits() {
        HashMap<User,Integer> map = new HashMap<User,Integer>();
        int numberPosts;
        for(DataUnit d : data) {
            User user = d.getAssoUser();
            if(map.containsKey(user)) {
                numberPosts = map.get(user);
                map.put(user, numberPosts + 1);
            }
            else {
                map.put(user, 1);
            }
        }
        return map;
    }
    
    /**
     * update the analysis
     * @param panel panel to be customized by the analysis plugin
     */
    private void update(JPanel panel) {
        numPosts = parseDataUnits();
        int total = numPosts.size();
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
        panel.add(chartPanel);
        panel.validate();
    }
    /** {@inherit} */
    public String getName(){
        return "Number Posts Analysis Plugin";
    }
}
