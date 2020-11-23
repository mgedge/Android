package edu.csi.niu.z1818828.assignment8_sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements notesLayoutAdapter.OnNoteListener {
    private TextView noNotes;
    private DatabaseManager dbManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Note> notes;

    private ArrayList<String> id, title, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noNotes = findViewById(R.id.textViewNoNotes);
        recyclerView = findViewById(R.id.main_recyclerView);

        notes = new ArrayList<>();

        dbManager = new DatabaseManager(MainActivity.this);

        storeData();

        adapter = new notesLayoutAdapter(this, notes, this);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //notes = dbManager.readAll();

        if(adapter.getItemCount() == 0) {
            noNotes.setVisibility(View.VISIBLE);
        } else
            noNotes.setVisibility(View.INVISIBLE);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    void storeData() {
        Cursor cursor = dbManager.readAll();
        if(cursor.getCount() != 0) {
            while(cursor.moveToNext()) {
                Note note = new Note(cursor.getString(1), cursor.getString(2));
                notes.add(note);
            }
        }
    }

    @Override
    public void onNoteClick(int position) {
        notes.get(position);
        Intent intent = new Intent(this, UpdateNoteActivity.class);
        startActivity(intent);
    }
}