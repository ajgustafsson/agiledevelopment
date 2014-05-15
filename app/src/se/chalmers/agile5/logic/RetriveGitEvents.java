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

import se.chalmers.agile5.entities.GitDataHandler;
import android.os.AsyncTask;
import android.util.Log;


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
	
	/*
	 * Returns ArrayList<RepositoryBranch> of branches in current repo.
	 */
	public ArrayList<RepositoryBranch> getBranches() {
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
	
	public ArrayList<RepositoryCommit> getCommits() {
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
	
	public ArrayList<RepositoryCommit> getCommitsByBranch(String branch) {
		FetchGitCommitsByBranch fetchGitCommits = new FetchGitCommitsByBranch();
		try {
			return fetchGitCommits.execute(branch).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public RepositoryCommit getExtendedCommit(String sha) {
		FetchSingleGitCommit fsgc = new FetchSingleGitCommit();
		try {
			return fsgc.execute(sha).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	/*
	 * Deprecated, is never used.
	 */
//	private ArrayList<String> repoCommitToCommit(ArrayList<RepositoryCommit> commitsIn) {
//			
//		ArrayList<String> commits = new ArrayList<String>();
//		for(RepositoryCommit commit : commitsIn) {
//			commits.add(commit.getCommit().getMessage());
//		}
//		return commits;
//	}
	
	/*
	 * Deprecated, is never used.
	 */
//	private ArrayList<String> repoBranchtoBranch(ArrayList<RepositoryBranch> repoBranches) {
//		ArrayList<String> branches = new ArrayList<String>();
//		for(RepositoryBranch branch : repoBranches) {
//			branches.add(branch.getName());
//		}
//		
//		return branches;
//	}
	
	private class FetchGitBranches extends AsyncTask<String, Void, ArrayList<RepositoryBranch>> {
		@Override
		protected ArrayList<RepositoryBranch> doInBackground(String... params) {
			RepositoryService repoService = new RepositoryService();
			Repository repo = GitDataHandler.getCurrentGitRepo();
			ArrayList<RepositoryBranch> branches = new ArrayList<RepositoryBranch>();
			try {
				branches = (ArrayList<RepositoryBranch>) repoService.getBranches(repo);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			return branches;
		}
		} 
	
	
	private class FetchGitCommits extends AsyncTask<String, Void, ArrayList<RepositoryCommit>> {
	@Override
	protected ArrayList<RepositoryCommit> doInBackground(String... params) {
		CommitService commitService = new CommitService(GitDataHandler.getGitClient());
		Repository repo = GitDataHandler.getCurrentGitRepo();
		//Branches that the user is tracking
		ArrayList<RepositoryBranch> branches = GitDataHandler.getTrackingBranches();
		ArrayList<RepositoryCommit> repoCommits = new ArrayList<RepositoryCommit>();
		try {
			for(RepositoryBranch branch : branches) {
			repoCommits = (ArrayList<RepositoryCommit>) commitService.getCommits(repo, branch.getName(), null);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return repoCommits;
	}
	}
	
	private class FetchGitCommitsByBranch extends AsyncTask<String, Void, ArrayList<RepositoryCommit>> {
		@Override
		protected ArrayList<RepositoryCommit> doInBackground(String... params) {
			CommitService commitService = new CommitService(GitDataHandler.getGitClient());
			Repository repo = GitDataHandler.getCurrentGitRepo();
			String branchName = params[0];
			//Branches that the user is tracking
			ArrayList<RepositoryBranch> branches = GitDataHandler.getTrackingBranches();
			ArrayList<RepositoryCommit> repoCommits = new ArrayList<RepositoryCommit>();
			try {
				for(RepositoryBranch branch : branches) {
					if(branch.getName().equals(branchName)) {
						repoCommits = (ArrayList<RepositoryCommit>) commitService.getCommits(repo, branch.getName(), null);
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			return repoCommits;
		}
		}
	
	
	private class FetchSingleGitCommit extends AsyncTask<String, Void, RepositoryCommit> {
	@Override
	protected RepositoryCommit doInBackground(String... params) {
		CommitService commitService = new CommitService(GitDataHandler.getGitClient());
		Repository repo = GitDataHandler.getCurrentGitRepo();
		RepositoryCommit rc = null;
//		Log.i("SHA CHECK", "Retrieving extended Commit for: " + params[0]);
		try {
			rc = commitService.getCommit(repo, params[0]);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return rc;
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
	            	String user = GitDataHandler.getCurrentGitUser().getLogin();
	                return service.getRepositories(user);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return null;
	        }
	    }
	

	
}