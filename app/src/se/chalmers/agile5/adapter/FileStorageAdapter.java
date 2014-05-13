package se.chalmers.agile5.adapter;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class FileStorageAdapter {
	private final String TAG = "FILE STORAGE";
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private final String myPrefs = "agile5Preferences";
	private String fileListKey = "file_list";
	private final String fileSelectionKey = "file_selection_list";
	
	public FileStorageAdapter(Context context) {
		sharedPreferences = context.getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
	}
	
	public void storeFileList(ArrayList<String> fileList) {
		editor = sharedPreferences.edit();
		Log.i(TAG, "Storing");
		JSONArray jsonArray = new JSONArray(fileList);
		String jsonString = jsonArray.toString();
		editor.putString(fileListKey, jsonString).apply();
	}
	
	public void setFileListKey(String key) {
		fileListKey = key;
	}
	
	public String getFileListKey() {
		return fileListKey;
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileList;
	}
	
	public void storeSelection(ArrayList<String> selectedFiles) {
		editor = sharedPreferences.edit();
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
				Log.i(TAG, "Storing. " + selection.size() + " files tracked");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return selection;
	}
	
}
