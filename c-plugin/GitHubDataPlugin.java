package edu.cmu.cs.cs214.hw5.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import edu.cmu.cs.cs214.hw5.framework.Post;
import edu.cmu.cs.cs214.hw5.framework.UserData;

/**
 * github data plugin for team 30's framework
 * @author Team27
 *
 */
public class GitHubDataPlugin implements DataPlugin {
    /* this is the number of repositories to look through for data
     * once we fetch all repositories for the user */
    private static final int NUM_REPOS = 5;
    private String token;
    
    public GitHubDataPlugin(){
        /* token removed */
        token = "****************************************";
    }
    /** {@inheritDoc} */
    public String getName() {
        return "GitHubData";
    }
    /** {@inheritDoc} */
    public UserData getUserData(String userName){
        UserData ud = new UserData(userName, getName());
        int repoCount = 0;
        User user = null;
        List<Repository> repos = null;
        List<RepositoryCommit> commits = new ArrayList<RepositoryCommit>();
        List<Post> userCommits = new ArrayList<Post>();
        
        /* set up connections with GitHub to get data */
        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(token);
        UserService userService = new UserService(client);
        RepositoryService repoService = new RepositoryService(client);
        CommitService comService = new CommitService(client);
        
        try {
            user = userService.getUser(userName);
        } catch (IOException e) {
            ud.setErroMsg("User doesn't exist or has private account");
        }
        if(user == null){
            return new UserData(userName, getName());
        }
        try {
            repos = repoService.getRepositories(userName);
        } catch (IOException e) {
            ud.setErroMsg("Can't get user's repositories");
        }
        /* set location */
        String loc = user.getLocation();
        if(loc != null){
            ud.setLocation(loc);
        }
        /* get Posts by getting repositories and going through the
         * repositories' commits that belong to the specified user */
        for(Repository r : repos){
            if(repoCount == NUM_REPOS) break;
            try {
                commits = comService.getCommits(r);
            } catch (IOException e) {
                ud.setErroMsg("Can't get repository commits");
            }
            for(RepositoryCommit rc : commits){
                Commit c = rc.getCommit();
                CommitUser cUser = c.getCommitter();
                /* only get this particular user's commits */
                if(cUser.getName().equals(user.getName())){
                    Date dt = cUser.getDate();
                    String msg = c.getMessage();
                    Post p = new Post(msg ,dt);
                    userCommits.add(p);
                }
            }
            repoCount++;
        }
        /* set id, number of followers and followees, and fetched commits */
        ud.setId(Integer.toString(user.getId()));
        ud.setFollowers(user.getFollowers());
        ud.setFollowees(user.getFollowing());
        ud.setPostList(userCommits);
 
        return ud;
    }
}
