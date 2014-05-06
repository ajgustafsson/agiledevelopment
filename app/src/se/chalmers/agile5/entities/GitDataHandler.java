package se.chalmers.agile5.entities;

import java.util.ArrayList;

import android.os.AsyncTask;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private AgileGitHubClient gitClient;

    /**
     * The currently selected GitHub repository
     */
    private Repository currentGitRepo;
	private ArrayList<RepositoryCommit> commits;
	
    //public static GitHubClient getGitClient() {

    /**
     * Instance of the current GitHub user
     */
    private User gitUser;

    /**
     * Determines if the login credentials of the user shall be saved to persistence
     */
    private boolean saveLoginInfoEnabled = true;


    public static AgileGitHubClient getGitClient() {

        return instance.gitClient;
    }

    public static void setGitHubClient(AgileGitHubClient gitClient){
        instance.gitClient = gitClient;
        fetchGitUser();
        instance.currentGitRepo = null; // if a new client has been set, the current repo is reset to null
    }

    public static String getGitUserName(){
        if(isUserLoggedIn()){
            return getCurrentGitUser().getLogin();
        }
        return null;
    }

    public static Repository getCurrentGitRepo() {
        return instance.currentGitRepo;
    }

    public static void setCurrentGitRepo(final Repository repository){
        instance.currentGitRepo = repository;
    }
    
    public static void setCommits(ArrayList<RepositoryCommit> commits) {
    	instance.commits = commits;
    }
    
    public static ArrayList<RepositoryCommit> getCommits() {
    	return instance.commits;
    }
    

    public static boolean isUserLoggedIn(){
        return getGitClient() != null && getCurrentGitUser() != null;
    }

    public static UserService getUserService(){
        return new UserService(getGitClient());
    }

    public static User getCurrentGitUser() {
        return instance.gitUser;
    }

    public static Repository getRepositoryById(String repoId){
        return fetchSingleRepo(repoId);
    }

    public static void setSaveLoginInfoEnabled(boolean save){
        instance.saveLoginInfoEnabled = save;
    }

    public static boolean isSaveLoginInfoEnabled(){
        return instance.saveLoginInfoEnabled;
    }

    private static void fetchGitUser(){
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    instance.gitUser = getUserService().getUser();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        asyncTask.execute((Void)null);
        //TODO handle exceptions, maybe call get() in another thread?
        try {
            asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static Repository fetchSingleRepo(String repoId){
        AsyncTask<String, Void, Repository> asyncTask = new AsyncTask<String, Void, Repository>() {
            @Override
            protected Repository doInBackground(String... params) {
                Repository resultRepo = null;
                RepositoryService service = new RepositoryService(getGitClient());
                try {
                    resultRepo = service.getRepository(RepositoryId.createFromId(params[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return resultRepo;
            }
        };
        asyncTask.execute(repoId);

        try {
            return asyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void logout() {
        instance.gitUser = null;
        instance.currentGitRepo = null;
        instance.gitClient = null;
        instance.saveLoginInfoEnabled = true;
    }
}
