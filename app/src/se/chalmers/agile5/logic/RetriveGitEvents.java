package se.chalmers.agile5.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.WatcherService;

import se.chalmers.agile5.activities.GitLoginActivity;
import se.chalmers.agile5.entities.GitDataHandler;
import android.content.Intent;
import android.os.AsyncTask;


public class RetriveGitEvents {

	public ArrayList<Repository> getRepos() {
		ArrayList<Repository> repos = new ArrayList<Repository>();
		GitHubClient client = GitDataHandler.getGitClient();
		
		if (client == null) 
			return null;
		
		FetchStarredReposTask starredrepos = new FetchStarredReposTask();
		starredrepos.execute(client);
		try {
			repos.addAll(starredrepos.get());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		FetchOwnedReposTask fetchOwnedReposTask = new FetchOwnedReposTask();
        fetchOwnedReposTask.execute(client);
        try {
			repos.addAll(fetchOwnedReposTask.get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return repos;
	}
	
	public ArrayList<String> getBranches() {
		FetchGitBranches fetchGitBranches = new FetchGitBranches();
		try {
			return fetchGitBranches.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getCommits() {
		FetchGitCommits fetchGitCommits = new FetchGitCommits();
		try {
			return fetchGitCommits.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	private ArrayList<String> repoCommitToCommit(ArrayList<RepositoryCommit> commitsIn) {
			
		ArrayList<String> commits = new ArrayList<String>();
		for(RepositoryCommit commit : commitsIn) {
			commits.add(commit.getCommit().getMessage());
		}
		
		return commits;
	}
	
	private ArrayList<String> repoBranchtoBranch(ArrayList<RepositoryBranch> repoBranches) {
		ArrayList<String> branches = new ArrayList<String>();
		for(RepositoryBranch branch : repoBranches) {
			branches.add(branch.getName());
		}
		
		return branches;
	}
	
	private class FetchGitBranches extends AsyncTask<String, Void, ArrayList<String>> {
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			CommitService commitService = new CommitService(GitDataHandler.getGitClient());
			RepositoryService repoService = new RepositoryService();
			Repository repo = GitDataHandler.getCurrentGitRepo();
			ArrayList<RepositoryBranch> branches = new ArrayList<RepositoryBranch>();
			try {
				branches = (ArrayList<RepositoryBranch>) repoService.getBranches(repo);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			return repoBranchtoBranch(branches);
		}
		} 
	
	
	private class FetchGitCommits extends AsyncTask<String, Void, ArrayList<String>> {
	@Override
	protected ArrayList<String> doInBackground(String... params) {
		CommitService commitService = new CommitService(GitDataHandler.getGitClient());
		RepositoryService repoService = new RepositoryService();
		Repository repo = GitDataHandler.getCurrentGitRepo();
		ArrayList<RepositoryBranch> branches = new ArrayList<RepositoryBranch>();
		ArrayList<RepositoryCommit> repoCommits = new ArrayList<RepositoryCommit>();
		try {
			branches = (ArrayList<RepositoryBranch>) repoService.getBranches(repo);
			repoCommits = (ArrayList<RepositoryCommit>) commitService.getCommits(repo, branches.get(0).getName(), null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return repoCommitToCommit(repoCommits);
	}
	}

	
	
	  private class FetchStarredReposTask extends AsyncTask<GitHubClient, Void, List<Repository>> {
	        @Override
	        protected List<Repository> doInBackground(GitHubClient... params) {
	            final GitHubClient client = params[0];
	            final WatcherService watchService = new WatcherService(client);
	            try {
	                return watchService.getWatched();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return null;
	        }
	    }
	  
	  private class FetchOwnedReposTask extends AsyncTask<GitHubClient, Void, List<Repository>> {
	        @Override
	        protected List<Repository> doInBackground(GitHubClient... params) {
	            final GitHubClient client = params[0];
	            final RepositoryService service = new RepositoryService(client);
	            try {
	                return service.getRepositories(client.getUser());
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return null;
	        }
	    }
	

	
}