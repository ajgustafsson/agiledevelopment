package se.chalmers.agile5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.RoadMapEntry;

import java.util.LinkedList;


public class RoadMapActivity extends BaseActivity {

    private LinkedList<RoadMapEntry> roadMapList = new LinkedList<RoadMapEntry>();

    private ListView roadMapListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadmap_activity);
        roadMapListView = (ListView)findViewById(R.id.roadMapTasksListView);
        fillLists();

        roadMapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(RoadMapActivity.this, RoadMapDetailActivity.class);
                RoadMapEntry entryClicked = roadMapList.get(position);
                detailIntent.putExtra("title", entryClicked.getTitle());
                detailIntent.putExtra("description", entryClicked.getDescription());
                startActivity(detailIntent);
            }
        });
    }

    private void fillLists() {
        ListView macroList = (ListView)findViewById(R.id.roadMapMacroListView);
        String[] macroStrings = {"Class", "Algo"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                macroStrings);
        macroList.setAdapter(arrayAdapter);


        roadMapList.add(new RoadMapEntry("Task1", "Description blablab"));
        roadMapList.add(new RoadMapEntry("Task2", "Description blaBlub"));
        roadMapList.add(new RoadMapEntry("Task3", "Description........"));
        ArrayAdapter<RoadMapEntry> arrayAdapter2 = new ArrayAdapter<RoadMapEntry>(
                this,
                android.R.layout.simple_list_item_1,
                roadMapList);
        roadMapListView.setAdapter(arrayAdapter2);
    }


}