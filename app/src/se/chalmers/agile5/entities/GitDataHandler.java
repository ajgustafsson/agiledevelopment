package se.chalmers.agile5.entities;

import android.os.AsyncTask;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
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
    private GitHubClient gitClient;

    /**
     * The currently selected GitHub repository
     */
    private Repository currentGitRepo;

    /**
     * Instance of the current GitHub user
     */
    private User gitUser;

    public static GitHubClient getGitClient() {
        return instance.gitClient;
    }

    public static void setGitHubClient(GitHubClient gitClient){
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
            asyncTask.get(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
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
    
}
