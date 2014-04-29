package se.chalmers.agile5.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import se.chalmers.agile5.R;

public class RoadMapDetailActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadmap_task_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String title = bundle.getString("title");
            if(title != null){
                TextView titleView = (TextView)findViewById(R.id.taskTitleTextView);
                titleView.setText(title);
            }
            String description = bundle.getString("description");
            if(description != null){
                TextView descView = (TextView)findViewById(R.id.taskDescTextView);
                descView.setText(description);
            }
        }
    }
}