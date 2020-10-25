/*********************************************************************
 CSCI 322/522  			  Assignment 7               		 FA2020

 Class Name: MainActivity.java

 Developer: Matthew Gedge
 Due Date: 11 September 2020

 Purpose: This java class starts the activity and runs the UI.
 When the calculate button is pressed, the inputs are checked for
 validity and the discriminate and x values are calculated.
 When the clear button is pressed, inputs and outputs are emptied.
 *********************************************************************/

package z1818828.cs.niu.assignment7_intents;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ItemC> dataSet;
    private TextView appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appTitle = findViewById(R.id.textView);
        appTitle.setText("Title of the app!");

        //Set recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //Setup layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Create an empty data set
        dataSet = new ArrayList<>();

        //setup the adapter for the recycler view data
        adapter = new layoutAdapter(this, dataSet);
        recyclerView.setAdapter(adapter);

        //Populate the data
        prepareRecycler();
    }

    private void prepareRecycler() {
        ItemC item = new ItemC("Item 1", "This is item 1", R.drawable.ic_launcher_background);
        dataSet.add(item);

        item = new ItemC("Item 2", "This is item 2", R.drawable.ic_launcher_background);
        dataSet.add(item);

        item = new ItemC("Item 3", "This is item 3", R.drawable.ic_launcher_background);
        dataSet.add(item);
    }
}