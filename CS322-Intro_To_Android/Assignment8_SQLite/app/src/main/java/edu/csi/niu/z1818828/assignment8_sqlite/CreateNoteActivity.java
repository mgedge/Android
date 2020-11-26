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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CreateNoteActivity extends AppCompatActivity {
    private DatabaseManager dbManager;
    private EditText titleEditText;
    private EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        //setup action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //link the database
        dbManager = new DatabaseManager(this);

        //get the edit texts
        titleEditText = findViewById(R.id.editTextTextTitle);
        noteEditText = findViewById(R.id.editTextTextMultiLineNote);

        //set title to focus on entry
        titleEditText.setFocusableInTouchMode(true);
        titleEditText.setFocusable(true);
        titleEditText.requestFocus();
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

        Note note = new Note(null, title, desc);
        dbManager.insert(note);

        Toast.makeText(this, "Note successfully added!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}