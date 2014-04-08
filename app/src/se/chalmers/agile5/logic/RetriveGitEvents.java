package se.chalmers.agile5.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.TypedResource;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.WatcherService;

import android.os.AsyncTask;
import se.chalmers.agile5.entities.GitDataHandler;
import se.chalmers.agile5.entities.UserStory;

public class RetriveGitEvents extends AsyncTask<String, Void, ArrayList<String>> {

	

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


	private ArrayList<String> repoCommitToCommit(ArrayList<RepositoryCommit> commitsIn) {
			
		ArrayList<String> commits = new ArrayList<String>();
		TypedResource temp = new TypedResource();
		for(RepositoryCommit commit : commitsIn) {
			commits.add(commit.getCommit().getMessage());
		}
		
		return commits;
	}
	
}