package se.chalmers.agile5.entities;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;

/**
 * Class to store and handle github api data, implemented as singleton.
 *
 * @author Armin
 */
public class GitDataHandler {
    private static final GitDataHandler instance = new GitDataHandler();
    public static GitDataHandler getInstance() {
        return instance;
    }

    /**
     * GitHub API client
     */
    private GitHubClient gitClient;

    /**
     * The currently selected GitHub repository
     */
    private Repository currentGitRepo;

    public static GitHubClient getGitClient() {
        return instance.gitClient;
    }

    public static void setGitHubClient(GitHubClient gitClient){
        instance.gitClient = gitClient;
    }

    public static String getGitUserName(){
        if(isUserLoggedIn()){
            try {
                return getUserService().getUser().getLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Repository getCurrentGitRepo() {
        return instance.currentGitRepo;
    }

    public static void setCurrentGitRepo(final Repository repository){
        instance.currentGitRepo = repository;
    }

    public static boolean isUserLoggedIn(){
        return getGitClient() != null && getGitClient().getUser() != null;
    }

    public static UserService getUserService(){
        return new UserService(getGitClient());
    }
    
}
