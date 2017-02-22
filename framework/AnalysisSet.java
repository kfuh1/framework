package edu.cmu.cs.cs214.hw5.framework;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * class that associates a dataplugin and analysisplugin, 
 * and facilitates plugin communication based on user
 * interaction
 * @author kfuh
 *
 */
public class AnalysisSet {
    private static final int DEFAULT_DUS_NUM = 10;
    
    private SocialMediaFramework framework;
    private DataPlugin dataPlugin;
    private AnalysisPlugin analysisPlugin;
    private JPanel analysisSetPanel;
    private List<DataUnit> cachedData;
    private int numDus;
    private String username;
    private List<DataUnit> data;
    /**
     * Constructor for the AnalysisSet
     * @param dt data plugin 
     * @param at analysis plugin
     * @param p panel that analysis set will modify to display user input
     *          choices
     * @param cdata cached data
     * @param f framework associated with the analysis set
     */
    public AnalysisSet(DataPlugin dt, AnalysisPlugin at, JPanel p,
            List<DataUnit> cdata, SocialMediaFramework f){
        data = new ArrayList<DataUnit>();
        
        dataPlugin = dt;
        analysisPlugin = at;
        analysisSetPanel = p;
        cachedData = cdata;
        framework = f;
        displayNumberScreen();
    }
    /**
     * Method to get some specified number of data units from data plugin.
     */
    private void fetchPluginData(){
        int index = 0;
        if(username == null || username.equals("")){
            data = dataPlugin.fetchData(numDus);
        }
        else{
            data = dataPlugin.fetchData(numDus, username);
            System.out.println("fetching data");
        }
        /* if API rate limit was hit or something else caused data plugin
         * not to get enough data units, take from the cached data. */
        while(index < cachedData.size() && data.size() < numDus){
            data.add(cachedData.get(index));
            index++;
        }
        displaySelectionScreen();
    }
    /**
     * Method to display selection of number of data units and
     * possibly enter a user name that the data plugin can use
     * to start the fetch data query on.
     */
    private void displayNumberScreen(){
        JPanel numPanel = new JPanel(new BorderLayout());
        JPanel namePanel = new JPanel(new BorderLayout());
        analysisSetPanel.removeAll();
        JLabel numLabel = new JLabel("Enter desired number of data units:");
        JLabel nameLabel = new JLabel("Enter username to analyze (optional):");
        final JFormattedTextField numField = new JFormattedTextField();
        final JTextField nameField = new JTextField();
        numField.setValue(new Integer(DEFAULT_DUS_NUM));
        JButton submitNumButton = new JButton("Submit");
        submitNumButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                numDus = (int) numField.getValue();
                username = nameField.getText();
                fetchPluginData();
            }
        });
        numPanel.add(numLabel, BorderLayout.NORTH);
        numPanel.add(numField, BorderLayout.CENTER);
        namePanel.add(nameLabel, BorderLayout.NORTH);
        namePanel.add(nameField, BorderLayout.CENTER);
        
        analysisSetPanel.add(numPanel,BorderLayout.NORTH);
        analysisSetPanel.add(namePanel, BorderLayout.CENTER);
        analysisSetPanel.add(submitNumButton, BorderLayout.SOUTH);
        
    }
    /**
     * Method to get data units associated with certain user names.
     * @param selectedNames user names selected to be analyzed
     * @return List of data units associated with the specified user names.
     */
    private List<DataUnit> getSelectedData(List<String> selectedNames){
        List<DataUnit> selectedData = new ArrayList<DataUnit>();
        if(selectedNames.size() == 0){
            return data;
        }
        else{
            for(DataUnit du : data){
                User u = du.getAssoUser();
                if(selectedNames.contains(u.getName())){
                    selectedData.add(du);
                }
            }
            return selectedData;
        }
    }
    /**
     * Method called to display panel that gives external user the
     * choice of which users they want to be analyzed.
     */
    private void displaySelectionScreen(){
        List<String> userIDList = new ArrayList<String>();
        Set<String> userIDSet = new HashSet<String>();
        for(DataUnit du : data){
            User u = du.getAssoUser();
            String name = u.getName();
            /* the external user will get a list of unique names of users
             * to choose from when selecting the subset. */
            if(!userIDSet.contains(name)){
                userIDSet.add(name);
                userIDList.add(name);
            }
        }
        String[] userIDArr = new String[userIDList.size()];
        userIDArr = userIDList.toArray(userIDArr);
        analysisSetPanel.removeAll();
        final JList<String> userChoiceList = new JList<String>(userIDArr);
        userChoiceList.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollList = new JScrollPane(userChoiceList);
        analysisSetPanel.add(scrollList, BorderLayout.CENTER);
        JLabel userSelectLabel = new JLabel("Select users to analyze:");
        analysisSetPanel.add(userSelectLabel, BorderLayout.WEST);
        
        JButton submitSelectionBtn = new JButton("Submit");
        submitSelectionBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                List<String> selectedNames = 
                        userChoiceList.getSelectedValuesList();
                List<DataUnit> selectedData = getSelectedData(selectedNames);
                analysisPlugin.receiveData(
                        selectedData, framework.invertedIndex(selectedData));
                displayAnalysis();
            }
        });
        analysisSetPanel.add(submitSelectionBtn, BorderLayout.SOUTH);
        analysisSetPanel.validate();
    }
    /**
     * Method called to display analysis plugin's customization of the panel.
     */
    private void displayAnalysis(){
        analysisSetPanel.removeAll();

        JPanel pluginPanel = new JPanel();
        analysisPlugin.customize(pluginPanel);
        analysisSetPanel.add(pluginPanel, BorderLayout.CENTER);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                displayNumberScreen();
            }
        });
        analysisSetPanel.add(refreshBtn, BorderLayout.SOUTH);
        analysisSetPanel.validate();
    }
}
