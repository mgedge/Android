/*********************************************************************
 CSCI 322/522  			  Assignment 7               		 FA2020

 Class Name: MainActivity.java

 Developer: Matthew Gedge
 Due Date: 6 November 2020

 Purpose: This java class starts the activity and runs the UI.
 When a card is pressed, A second activity is started which will
 contain more information about the card.
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

        //Set the title
        appTitle = findViewById(R.id.textView);
        appTitle.setText("Powerlifting");

        //Set recycler view
        recyclerView = findViewById(R.id.recyclerView);

        //Setup layout manager for the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Create an empty data set
        dataSet = new ArrayList<>();

        //setup the adapter for the recycler view data
        adapter = new layoutAdapter(this, dataSet);
        recyclerView.setAdapter(adapter);

        //Populate the data
        prepareRecycler();
    }

    /*
    This class will add Item data to the List
     */
    private void prepareRecycler() {
        ItemC item = new ItemC("Squat", "Squatting a barbell on the back", R.drawable.squat);
        dataSet.add(item);

        item = new ItemC("Bench", "Pushing a barbell off the chest", R.drawable.bench_press2);
        dataSet.add(item);

        item = new ItemC("Deadlift", "Lifting a barbell off the floor", R.drawable.deadlift);
        dataSet.add(item);

        item = new ItemC("Overhead Press", "Pushing a barbell above the head", R.drawable.overhead);
        dataSet.add(item);

        item = new ItemC("Row", "Pulling a barbell to the body", R.drawable.row);
        dataSet.add(item);
    }
}