/************************************************************************
 *                                                                      *
 * CSCI 322/522  			  Assignment 8               		 FA2020 *
 *                                                            		    *
 * 	Class Name: CreateNoteActivity.java									*
 * 																		*
 *  Developer: Matthew Gedge											*
 *   Due Date: 4 December 2020							    			*
 *   																	*
 *    Purpose: This activity is allows the user to create a new note    *
 *    and add it to the database.
 *																		*
 * *********************************************************************/
package edu.csi.niu.z1818828.assignment8_sqlite;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CreateNoteActivity extends AppCompatActivity {
    private DatabaseManager dbManager;
    private EditText titleEditText;
    private EditText noteEditText;
    private final ArrayList<String> notes = new ArrayList<String>();
    private TextView textViewExistingNotes;
    private TextView textViewExistingNotesTitle;
    private ListView notesListView;
    private View divider;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        divider = findViewById(R.id.divider);

        //setup action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Note");

        //link the database
        dbManager = new DatabaseManager(this);

        //get the edit texts
        titleEditText = findViewById(R.id.editTextTextTitle);
        noteEditText = findViewById(R.id.editTextTextMultiLineNote);

        //set title to focus on entry
        titleEditText.setFocusableInTouchMode(true);
        titleEditText.setFocusable(true);
        titleEditText.requestFocus();

        //Setup adapter
        storeData();
        notesListView = findViewById(R.id.listView_notes);
        adapter = new ArrayAdapter<String>(this, R.layout.list_view_note, notes);
        notesListView.setAdapter(adapter);

        //Show notes
        textViewExistingNotes = findViewById(R.id.textViewExistingNotes);
        textViewExistingNotesTitle = findViewById(R.id.textViewExistingNotesTitle);
        updateNoNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_save:
                insert();
                break;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * turn the editText views into strings, then add the note to the database
     */
    public void insert() {
        String title = titleEditText.getText().toString();
        String desc = noteEditText.getText().toString();

        Note note = new Note(null, title, desc, false);
        dbManager.insert(note);

        Toast.makeText(this, "Note successfully added!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Stores the database items into the notes list, if any exist
     * *
     */
    void storeData() {
        Cursor cursor = dbManager.readAll();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(1);
                notes.add(title);
            }
        }
    }

    /**
     * Updates the visibility for the texts
     */
    void updateNoNotes() {
        if (notes.size() == 0) {
            textViewExistingNotes.setVisibility(View.VISIBLE);

        } else {
            textViewExistingNotes.setVisibility(View.INVISIBLE);
        }
    }
}