package se.chalmers.agile5.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import se.chalmers.agile5.R;

public class RoadMapDetailActivity extends BaseActivity {

    private String title;
    private String description;
    private int index;
    private boolean detailsChanged = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadmap_task_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            title = bundle.getString("title");
            if(title != null){
                TextView titleView = (TextView)findViewById(R.id.taskTitleTextView);
                titleView.setText(title);
            }
            description = bundle.getString("description");
            if(description != null){
                TextView descView = (TextView)findViewById(R.id.taskDescTextView);
                descView.setText(description);
            }
            index = bundle.getInt("index");
        }


    }

    @Override
    public void onBackPressed() {
        if(detailsChanged){
            Intent output = new Intent();
            output.putExtra(RoadMapActivity.INTENT_TITLE_RESULT, title);
            output.putExtra(RoadMapActivity.INTENT_DESC_RESULT, description);
            output.putExtra(RoadMapActivity.INTENT_INDEX_RESULT, index);
            setResult(RoadMapActivity.RESULT_DETAIL_CHANGED, output);
            finish();
        } else {
            setResult(RoadMapActivity.RESULT_CANCELED);
            finish();
        }
    }


    public void changeTitle(View v){
        final ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.taskTitleSwitcher);
        switcher.showNext();
        final TextView textView = (TextView) switcher.findViewById(R.id.taskTitleTextView);
        final EditText editText = (EditText) switcher.findViewById(R.id.taskTitleEditText);
        editText.setText(textView.getText());
        editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager)getBaseContext().
                getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // Perform action on key press
                    final String newTitle = editText.getText().toString();

                    if (!newTitle.isEmpty()) {
                        title = newTitle;
                        textView.setText(newTitle);
                        detailsChanged = true;
                    }

                    switcher.showNext();

                    InputMethodManager inputManager = (InputMethodManager)getBaseContext().
                            getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);

                    editText.clearFocus();
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    switcher.showNext();
                }
            }
        });
    }

    public void changeDescription(View v){
        final ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.taskDescriptionSwitcher);
        switcher.showNext();
        final TextView textView = (TextView) switcher.findViewById(R.id.taskDescTextView);
        final EditText editText = (EditText) switcher.findViewById(R.id.taskDescEditText);
        editText.setText(textView.getText());
        editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager)getBaseContext().
                getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, 0);

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    final String newDescription = editText.getText().toString();

                    description = newDescription;
                    textView.setText(newDescription);
                    detailsChanged = true;

                    switcher.showNext();

                    InputMethodManager inputManager = (InputMethodManager)getBaseContext().
                            getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);

                    editText.clearFocus();
                    return true;
                }
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    switcher.showNext();
                }
            }
        });
    }
}