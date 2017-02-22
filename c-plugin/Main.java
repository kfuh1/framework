package edu.cmu.cs.cs214.hw5.plugin;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw5.framework.FrameworkImpl;
import edu.cmu.cs.cs214.hw5.gui.MainMenuGui;

/**
* Main class to test framework and plugins 
* written
* @author Team27
*
*/
public class Main {
   /**
    * Initial method called to start framework.
    * @param args command-line arguments
    */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndStartFramework();
			}
		});
	}

	/**
	 * Method to register plugins and start GUI
	 * uses the simple main of Team30
	 */
	private static void createAndStartFramework() {
		FrameworkImpl fm = new FrameworkImpl();
		FacebookDataPlugin fb = new FacebookDataPlugin();
		fm.addDataPlugin(fb);
		GitHubDataPlugin gh = new GitHubDataPlugin();
		fm.addDataPlugin(gh);
		TumblrDataPlugin td = new TumblrDataPlugin();
		fm.addDataPlugin(td);
		NumberPostsAnalysisPlugin np = new NumberPostsAnalysisPlugin();
		fm.addAnalysisPlugin(np);
		PieChartAnalysisPlugin pc = new PieChartAnalysisPlugin();
		fm.addAnalysisPlugin(pc);
		AnalysisPlugin wordLenPlugin = new WordLengthAnalysisPlugin();
		fm.addAnalysisPlugin(wordLenPlugin);
		JFrame frame = new JFrame("Social Network Analysis FrameWork");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		// add the panel
		MainMenuGui gui = new MainMenuGui(frame, fm);
		frame.add(gui);
		frame.setContentPane(gui);
		frame.setVisible(true);
		frame.pack();

		fm.subscribe(gui);
	}

}
