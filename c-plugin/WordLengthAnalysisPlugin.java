package edu.cmu.cs.cs214.hw5.plugin;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import edu.cmu.cs.cs214.hw5.framework.AnalyzedData;
import edu.cmu.cs.cs214.hw5.framework.Post;

/**
 * Analysis plugin that displays a histogram of the lengths of words in posts
 * and mean, median, mode statistics of those lengths.
 * @author Team27
 *
 */
public class WordLengthAnalysisPlugin implements AnalysisPlugin{
    private static final int BINS = 20;
    
    /** {@inherit} */
    public JPanel analyzeData(List<AnalyzedData> analyzedDatas){
        int totalLength = 0;
        double mean, median;
        int mode;
        List<Integer> lengthList = new ArrayList<Integer>();
        List<Post> posts; 
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        /* for each word in a post, get the length of the word */
        for(AnalyzedData ad : analyzedDatas){
            posts = ad.getPosts();
            for(Post p : posts){
                String msg = p.getContent();
                /* possible delimiters of words. this may not be an exhaustive
                 * list but they're the most likely ones that will split words.
                 */
                String delims = "[ .;,?!+():\"]+";
                String[] words = msg.split(delims);
                for(String s : words){
                    lengthList.add(s.length());
                    totalLength += s.length();
                }
            }
        }
        double arr[] = new double[lengthList.size()];
        for(int i = 0; i < arr.length; i++){
            arr[i] = (double) lengthList.get(i);
        }
        dataset.addSeries("", arr, BINS);
        
        mean = (double) totalLength / (double) lengthList.size();
        List<Integer> sortedList = sort(lengthList);
        median = getMedian(sortedList);
        mode = getMode(sortedList);
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea("Statistics: \n Mean: " 
                + Double.toString(mean) + "\n Median: " + 
                Double.toString(median) + "\n Mode: " + mode);
       
        JFreeChart chart = ChartFactory.createHistogram(
                "Word Lengths", "Length (in characters)", "Frequency", 
                dataset, PlotOrientation.VERTICAL, true, false, false);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(area, BorderLayout.SOUTH);
        return panel;
    }
    /** {@inherit} */
    public String getName(){
        return "Word Length Analysis";
    }
    /**
     * Method that merges two sorted lists.
     * @param list1 first list
     * @param list2 second list
     * @return list that is list1 and list2 merged together
     */
    private List<Integer> merge(List<Integer> list1, List<Integer> list2){
        List<Integer> mergedList = new ArrayList<Integer>();
        int index1 = 0;
        int index2 = 0;
        int length1 = list1.size();
        int length2 = list2.size();
        /* loop until one of the lists has no more elems */
        while(index1 < length1 && index2 < length2){
            int value1 = list1.get(index1);
            int value2 = list2.get(index2);
            if(value1 < value2){
                mergedList.add(value1);
                index1++;
            }
            else if(value1 > value2){
                mergedList.add(value2);
                index2++;
            }
            else{
                mergedList.add(value1);
                mergedList.add(value2);
                index1++;
                index2++;
            }
        }
        /* append the remaining elements of whichever list still has elems */
        if(index1 == length1 && index2 < length2){
            mergedList.addAll(list2.subList(index2, length2));
        }
        else if(index2 == length2 && index1 < length1){
            mergedList.addAll(list1.subList(index1, length1));
        }
        return mergedList;
    }
    /**
     * Method that sorts an integer list.
     * @param list list to be sorted
     * @return sorted list
     */
    private List<Integer> sort(List<Integer> list){
        if(list.size() < 2) return list;
        int length = list.size();
        List<Integer> left = sort(list.subList(0,length/2));
        List<Integer> right = sort(list.subList(length/2, length));
        return merge(left, right);
    }
    /**
     * Method to get median value of list that is sorted.
     * @param list sorted list from which we want the median. 
     * @return median of the list
     */
    private double getMedian(List<Integer> list){
        int length = list.size();
        /* odd length means exact middle exists */
        if(length % 2 == 1) return list.get(length/2);
        /* take average of two middle values if even length */
        else{
            int val1 = list.get(length/2);
            int val2 = list.get(length/2 + 1);
            return (double) (val1 + val2) / 2.0;
        }
    }
    /**
     * Method to get the mode of the list that is sorted.
     * @param list sorted list from which we want mode.
     * @return mode of the list.
     */
    private int getMode(List<Integer> list){
        int maxCount = 0;
        int modeVal = -1;
        int counter = 1;
        for(int i = 0; i < list.size()-1; i++){
            if(list.get(i) == list.get(i+1)){
                counter++;
            }
            else{
                if(counter > maxCount){
                    maxCount = counter;
                    modeVal = list.get(i);
                }
                //reset counter once you hit two unequal numbers
                counter = 1;
            }
        }
        return modeVal;
    }
}
