package edu.cmu.cs.cs214.hw5.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

import edu.cmu.cs.cs214.hw5.framework.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.DataUnit;
import edu.cmu.cs.cs214.hw5.framework.User;
/**
 * Sample data plugin for GitHub data.
 * @author kfuh
 *
 */
public class GithubSampleDataPlugin implements DataPlugin {
    /* These are some of the top github contributors found online. 
     * If fetchData(num) is called (i.e. the method that doesn't give
     * a starting point user, one of the default users is picked at random 
     * and their data will be passed back) */
    private static final String[] DEFAULT_USERS = {"Ocramius", "michalbe", 
        "kevinsawicki", "brianchandotcom", "fabpot", "weierophinney", 
        "GrahamCampbell", "defunkt", "mrmrs", "rkh", "md-5", "ornicar"};
    
    private List<DataUnit> data;
    private String token;
    private Random rand;
    /**
     * Constructor for the sample data plugin 
     */
    public GithubSampleDataPlugin(){
        rand = new Random();
        data = new ArrayList<DataUnit>();
        
        token = "0311295e2903b4cf7fc75f3e27a232a2c3026883";
    }
    /** {@inheritDoc} */
    public List<DataUnit> fetchData(int num){
        String username = DEFAULT_USERS[rand.nextInt(DEFAULT_USERS.length)];
        return fetch(num, username);
    }
    /** {@inheritDoc} */
    public List<DataUnit> fetchData(int num, String username){
        return fetch(num, username);
    }
    /**
     * Method called by the fetchData methods to actually do work of fetching.
     * @param num number of data units
     * @param username name of user to get data about.
     * @return list of data units associated with user.
     */
    private List<DataUnit> fetch(int num, String username){
        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(token);
        RepositoryService rService = new RepositoryService();
        CommitService comService = new CommitService();
        try {
            /* get the user's repositories and from those repositories
             * get the commits in order to create data units. */
            for(Repository repo : rService.getRepositories(username)){
                for(RepositoryCommit rc : comService.getCommits(repo)){
                    Commit c = rc.getCommit();
                    String msg = c.getMessage();
                    CommitUser user = c.getCommitter();
                    String name = user.getName();
                    Date dt = user.getDate();
                    DataUnit du = new DataUnit(new User(0, name, dt), null, msg);
                    data.add(du);
                    if(data.size() == num){
                        return data;
                    }
                }
            }
            
        } catch (IOException e) {
            /* return what we have if API rate limit is hit */
            System.out.println(e.getMessage());
            return data;
        }
        /* if there's not enough data to be gotten return what we have */
        return data;
    }
    /** {@inherit} */
    public String getName(){
        return "GitHub Data Plugin";
    }
}
