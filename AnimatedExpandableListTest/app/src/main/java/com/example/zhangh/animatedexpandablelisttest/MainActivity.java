package com.example.zhangh.animatedexpandablelisttest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ExpandableListViewItem> parentList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            ExpandableListViewItem item = new ExpandableListViewItem(this, getTextView("Item " + i), getListView(i));
            parentList.add(item);
        }

        ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(this, android.R.layout.simple_list_item_1, parentList);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private TextView getTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(30);
        return textView;
    }

    private List<View> getListView(int num) {
        List<View> children = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            TextView child = getTextView("Sub Item " + i);
            child.setTextSize(20);
            children.add(child);
        }
        return children;
    }
}
