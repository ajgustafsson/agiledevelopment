package se.chalmers.agile5.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.chalmers.agile5.R;
 
import se.chalmers.agile5.adapter.ExpandableListAdapter;
 
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
 
public class ExpandableListExample extends BaseActivity {
 
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandablelist);
 
        createGroupList();
 
        createCollection();
 
        expListView = (ExpandableListView) findViewById(R.id.expandlist);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, laptopCollection);
        expListView.setAdapter(expListAdapter);
 
        //setGroupIndicatorToRight();
 
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();
 
                return true;
            }
        });
    }
 
    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Icebox");
        groupList.add("Backlog");
        groupList.add("Current");
        groupList.add("Done");
    }
 
    private void createCollection() {
        // preparing laptops collection(child)
        String[] icebox = { "Hej1", "Hej2",
                "Hej3" };
        String[] backlog = { "Hej1", "Hej2",
        "Hej3" };
        String[] current = { "Hej1", "Hej2",
        "Hej3" };
        String[] done = { "Hej1", "Hej2",
        "Hej3" };
 
        laptopCollection = new LinkedHashMap<String, List<String>>();
 
        for (String laptop : groupList) {
            if (laptop.equals("Icebox")) {
                loadChild(icebox);
            } else if (laptop.equals("Backlog"))
                loadChild(backlog);
            else if (laptop.equals("Current"))
                loadChild(current);
            else if (laptop.equals("Done"))
                loadChild(done);

 
            laptopCollection.put(laptop, childList);
        }
    }
 
    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }
 
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
 
        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }
 
    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
}
