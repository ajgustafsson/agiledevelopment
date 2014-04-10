package se.chalmers.agile5.adapter;

import java.util.List;
import java.util.Map;

import se.chalmers.agile5.R;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
    private Map<String, List<String>> listCollections;
    private List<String> headers;
 
    public ExpandableListAdapter(Activity context, List<String> headers,
            Map<String, List<String>> listCollections) {
        this.context = context;
        this.listCollections = listCollections;
        this.headers = headers;
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return listCollections.get(headers.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
    		boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
 
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item, null);
        }
        
        TextView item = (TextView) convertView.findViewById(R.id.child);
        CheckBox follow = (CheckBox) convertView.findViewById(R.id.follow);
        follow.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
            	CheckBox cb = (CheckBox) v;
            	if(cb.isChecked()){
            		//logic to follow
            		
            		/*//OPTIONAL
	                AlertDialog.Builder builder = new AlertDialog.Builder(context);
	                builder.setMessage("Do you want to follow this story?");
	                builder.setCancelable(false);
	                builder.setPositiveButton("Yes",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                                //TODO: Follow story
	                            }
	                        });
	                builder.setNegativeButton("No",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                                dialog.cancel();
	                            }
	                        });
	                AlertDialog alertDialog = builder.create();
	                alertDialog.show();
	                */
            	}else{
            		//logic for unfollow
            	}
            }
        });
 
        item.setText(laptop);
        return convertView;
    }
 
    public int getChildrenCount(int groupPosition) {
        return listCollections.get(headers.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }
 
    public int getGroupCount() {
        return headers.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
