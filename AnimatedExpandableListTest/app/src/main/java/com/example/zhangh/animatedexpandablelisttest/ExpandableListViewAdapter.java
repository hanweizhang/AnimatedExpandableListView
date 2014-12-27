package com.example.zhangh.animatedexpandablelisttest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by zhangh on 12/25/2014.
 */
public class ExpandableListViewAdapter extends ArrayAdapter<ExpandableListViewItem> {

    private List<ExpandableListViewItem> objects;

    public ExpandableListViewAdapter(Context context, int resource, List<ExpandableListViewItem> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return objects.get(position);
    }
}
