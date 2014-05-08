package se.chalmers.agile5.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

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
		Toast.makeText(context, "Selection stored", Toast.LENGTH_LONG).show();
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
}
