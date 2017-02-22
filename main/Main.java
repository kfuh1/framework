package edu.cmu.cs.cs214.hw5.main;
import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw5.framework.SocialMediaFramework;
import edu.cmu.cs.cs214.hw5.framework.SocialMediaFrameworkGUI;
import edu.cmu.cs.cs214.hw5.framework.SocialMediaFrameworkImp;
import edu.cmu.cs.cs214.hw5.plugin.FreeChartAnalysisPlugin;
import edu.cmu.cs.cs214.hw5.plugin.GithubSampleDataPlugin;
import edu.cmu.cs.cs214.hw5.plugin.MsgLengthAnalysisPlugin;
import edu.cmu.cs.cs214.hw5.plugin.NumberPostsAnalysisPlugin;
import edu.cmu.cs.cs214.hw5.plugin.TwitterDataPlugin;
/**
 * Main class to start framework and GUI
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
	 */
	private static void createAndStartFramework() {
		SocialMediaFramework core = new SocialMediaFrameworkImp();
		core.registerDataPlugin(new GithubSampleDataPlugin());
		core.registerDataPlugin(new TwitterDataPlugin());
		core.registerAnalysisPlugin(new FreeChartAnalysisPlugin());
		core.registerAnalysisPlugin(new MsgLengthAnalysisPlugin());
		core.registerAnalysisPlugin(new NumberPostsAnalysisPlugin());
		new SocialMediaFrameworkGUI(core);
	}

}