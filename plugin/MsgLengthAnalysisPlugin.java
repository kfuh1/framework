package edu.cmu.cs.cs214.hw5.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.IntervalXYDataset;

import edu.cmu.cs.cs214.hw5.framework.AnalysisPlugin;
import edu.cmu.cs.cs214.hw5.framework.DataUnit;
/**
 * Sample analysis plugin that produces histogram of message lengths.
 * @author kfuh
 *
 */
public class MsgLengthAnalysisPlugin implements AnalysisPlugin{
    private static final int BINS = 20;
    private List<DataUnit> dataUnits;
    
    /**
     * constructor for the plugin 
     */
    public MsgLengthAnalysisPlugin(){
        dataUnits = new ArrayList<DataUnit>(); 
    }

    /** {@inheritDoc} */
    public void customize(JPanel panelI) {
        update(panelI);
    }

    /** {@inheritDoc} */
    public void receiveData(List<DataUnit> dus,
            HashMap<String, ArrayList<DataUnit>> invertedIndexI) {
        dataUnits = dus;
        
    }
    /**
     * Method to construct dataset based on given data
     * @return data set
     */
    private IntervalXYDataset createDataset(){
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset.addSeries("", getDataArr(), BINS);
        return dataset;
    }
    /**
     * Method to create an array of all message lengths.
     * @return array of doubles representing lengths of messages.
     */
    private double[] getDataArr(){
        List<Integer> lengthList = new ArrayList<Integer>();
        for(DataUnit du : dataUnits){
            String msg = du.getMessage();
            lengthList.add(msg.length());
        }
        double[] arr = new double[lengthList.size()];
        for(int i = 0; i < arr.length; i++){
            arr[i] = (double) lengthList.get(i);
        }
        return arr;
    }
    /**
     * updates the panel with new data
     * @param panel panel to be customized by the analysis plugin
     */
    private void update(JPanel panel){
        if(dataUnits.size() == 0) return;
        panel.removeAll();
        /* creating the historgram was based off of javadoc examples online. */
        IntervalXYDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createHistogram(
                "Message Lengths", "Length (in characters)", "Frequency", 
                dataset, PlotOrientation.VERTICAL, true, false, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel);
        panel.validate();
    }
    /** {@inherit} */
    public String getName(){
        return "Message Length Analysis Plugin";
    }
}
