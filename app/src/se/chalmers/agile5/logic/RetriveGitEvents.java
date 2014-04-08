package se.chalmers.agile5.logic;

import java.io.IOException;

import org.eclipse.egit.github.core.service.CommitService;

import se.chalmers.agile5.entities.GitDataHandler;

public class RetriveGitEvents {

	public RetriveGitEvents() {
		
		GitDataHandler handler = new GitDataHandler();
		CommitService commitService = new CommitService(handler.getGitClient());
		try {
			System.out.println(commitService.getCommits(handler.getCurrentGitRepo()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
