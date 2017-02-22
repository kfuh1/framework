package edu.cmu.cs.cs214.hw5.framework;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
/**
 * GUI for the social media framework
 * @author ryanarcher
 */
public class SocialMediaFrameworkGUI {

	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JButton addTabButton;
	private int tabCounter = 0;
	private SocialMediaFramework framework;
	private List<DataPlugin> dataPlugins;
	private List<AnalysisPlugin> analysisPlugins;
	private JComboBox<String> aPluginDrop;
	private JComboBox<String> dPluginDrop;
	private JPanel bottom;
	private List<JPanel> panels;
	/**
	 * constructor for the class
	 * @param f the framework for this GUI
	 */
    public SocialMediaFrameworkGUI(SocialMediaFramework f) {
    	panels = new ArrayList<JPanel>();
    	framework = f;
    	bottom = new JPanel();
    	bottom.setLayout(new BorderLayout());
    	
    	// call framework to get all the plugins
    	dataPlugins = framework.getAllDataPlugin();
    	analysisPlugins = framework.getAllAnalysisPlugin();
    	
    	frame = new JFrame("Social Media Framework");
    	tabbedPane = new JTabbedPane();
    	addTabButton = new JButton("Add Tab");
    	addTabButton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e){
    			add();
    		} 
    	});
    	
    	// get the names of the plugins
    	String[] aplugins = new String[analysisPlugins.size()];
    	String[] dplugins = new String[analysisPlugins.size()];
    	
    	
    	for(int i = 0; i < analysisPlugins.size(); i++) {
    		aplugins[i] = analysisPlugins.get(i).getName();
    	}
    	
    	for(int j = 0; j < dataPlugins.size(); j++) {
    		dplugins[j] = dataPlugins.get(j).getName();
    	}
    	
    	aPluginDrop = new JComboBox<String>(aplugins);
    	dPluginDrop = new JComboBox<String>(dplugins);
 

    	frame.add(tabbedPane, BorderLayout.CENTER);
    	
    	bottom.add(addTabButton,BorderLayout.SOUTH);
    	bottom.add(aPluginDrop,BorderLayout.WEST);
    	bottom.add(dPluginDrop,BorderLayout.EAST);
    	frame.add(bottom,BorderLayout.SOUTH);

    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	frame.pack();
    	frame.setMinimumSize(new Dimension(500, 500));
    	frame.setVisible(true);
    }

    /**
     * add a new tab to the GUI for the analysis
     */
    public void add() {
    	final JPanel content = new JPanel();
    	content.setLayout(new BorderLayout());
    	
    	int aIndex = aPluginDrop.getSelectedIndex();
    	int dIndex = dPluginDrop.getSelectedIndex();
    	
     	JButton refresh = new JButton("Refresh");
    	refresh.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			AnalysisPlugin plugin = analysisPlugins.get(aIndex);
    			plugin.customize(content);
    		}
    	});
    	content.add(refresh,BorderLayout.SOUTH);
    	
    	// add jpanel to list of jpanels for analysis
    	panels.add(content);

    	tabbedPane.addTab("Analysis " + (++tabCounter), content);
    	

    	framework.selectedDataAnalysis(content, dataPlugins.get(dIndex),
    			analysisPlugins.get(aIndex));
    	
    }
    
    /**
     * get JPanel at specific index
     * @param index the index of the JPanel to return
     * @return the JPanel for the tab specified by the index
     */
    public JPanel getPanelAtIndex(int index) {
    	return panels.get(index);
    }
}

