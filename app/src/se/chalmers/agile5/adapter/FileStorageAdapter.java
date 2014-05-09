package se.chalmers.agile5.adapter;

import java.util.ArrayList;

import org.eclipse.egit.github.core.RepositoryBranch;
import org.json.JSONArray;
import org.json.JSONException;

import se.chalmers.agile5.logic.RetriveGitEvents;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class FileStorageAdapter {
	private final String TAG = "FILE STORAGE";
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private final String myPrefs = "agile5Preferences";
	private final String fileListKey = "file_list";
	private final String fileSelectionKey = "file_selection_list";
	private final String trackingBranchesKey = "tracking_branches";
	private Context context;
	
	public FileStorageAdapter(Context context) {
		this.context = context;
		sharedPreferences = context.getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}
	
	public void storeFileList(ArrayList<String> fileList) {
		Log.i(TAG, "Storing");
		JSONArray jsonArray = new JSONArray(fileList);
		String jsonString = jsonArray.toString();
		editor.putString(fileListKey, jsonString).apply();
		Toast.makeText(context, "File list stored", Toast.LENGTH_LONG).show();
	}
	
	public ArrayList<String> retrieveFileList() {
		ArrayList<String> fileList = new ArrayList<String>();
		String s = sharedPreferences.getString(fileListKey, "");
		
		JSONArray a;
		try {
			a = new JSONArray(s);
			if (a != null) {
				for (int i=0; i < a.length(); i++)
					fileList.add(a.getString(i));
			}
			Log.i(TAG, "Found. " + fileList.get(0));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileList;
	}
	
	public void storeSelection(ArrayList<String> selectedFiles) {
		Log.i(TAG, "Storing selection");
		JSONArray jsonArray = new JSONArray(selectedFiles);
		String jsonString = jsonArray.toString();
		editor.putString(fileSelectionKey, jsonString).apply();
	}
	
	public ArrayList<String> loadSelection() {
		ArrayList<String> selection = new ArrayList<String>();
		String s = sharedPreferences.getString(fileSelectionKey, "");
		
		JSONArray array;
		try {
			array = new JSONArray(s);
			if (array != null) {
				for (int i=0; i < array.length(); i++)
					selection.add(array.getString(i));
			}
			Log.i(TAG, "Found. " + selection.get(0));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return selection;
	}
	
	public void storeTrackingBranches(ArrayList<RepositoryBranch> branches) {
		ArrayList<String> temp = new ArrayList<String>();
		for(RepositoryBranch branch : branches) {
			temp.add(branch.getName());
		}
		Log.i(TAG, "Storing Tracking branch");
		JSONArray jsonArray = new JSONArray(temp);
		String jsonString = jsonArray.toString();
		editor.putString(trackingBranchesKey, jsonString).apply();
		Toast.makeText(context, "Branch stored", Toast.LENGTH_LONG).show();
	}
	
	public ArrayList<RepositoryBranch> loadTrackingsBranches() {
		ArrayList<String> trackingBranches = new ArrayList<String>();
		ArrayList<RepositoryBranch> branchesOnRepo = new ArrayList<RepositoryBranch>();
		ArrayList<RepositoryBranch> branches = new ArrayList<RepositoryBranch>();
		RetriveGitEvents git = new RetriveGitEvents();
		branchesOnRepo = git.getBranches();
		String s = sharedPreferences.getString(trackingBranchesKey, "");
		
		JSONArray array;
		try {
			array = new JSONArray(s);
			if (array != null) {
				for (int i=0; i < array.length(); i++)
					trackingBranches.add(array.getString(i));
			}
			Log.i(TAG, "Found. " + trackingBranches.get(0));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(RepositoryBranch branch : branchesOnRepo) {
			for(String branchName : trackingBranches) {
				if(branch.getName().equals(branchName) && !branches.contains(branch)) {
					branches.add(branch);
				}
			}
			
		}
		
		return branches;
	}
}
