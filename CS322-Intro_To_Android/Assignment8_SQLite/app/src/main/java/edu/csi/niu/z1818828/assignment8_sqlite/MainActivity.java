/************************************************************************
 *                                                                      *
 * CSCI 322/522  			  Assignment 8               		 FA2020 *
 *                                                            		    *
 * 	Class Name: MainActivity.java										*
 * 																		*
 *  Developer: Matthew Gedge											*
 *   Due Date: 4 December 2020							    			*
 *   																	*
 *    Purpose: This java class is the driver for the sql app.           *
 *    This activity will list all the notes the user adds using a       *
 *    database and a recycler view adapter. Additionally, it will       *
 *    support an interface listener to allow for long click selection   *
 *    of notes. If a user long clicks a note, they can click multiple   *
 *    notes then delete them. Alternatively, a select all button will   *
 *    select the notes at once.
 *																		*
 * *********************************************************************/

package edu.csi.niu.z1818828.assignment8_sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ADD_NOTE = 1;
    private static final int UPDATE_NOTE = 2;
    private TextView noNotes;
    private TextView appDesc;
    private DatabaseManager dbManager;
    private notesLayoutAdapter adapter;
    private ActionMode actionMode;
    protected ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup the toolbar
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Note Keeper");

        //Setup the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        //Get the no notes view
        noNotes = findViewById(R.id.textViewNoNotes);
        appDesc = findViewById(R.id.textViewAppDesc);

        //Setup the database manager
        dbManager = new DatabaseManager(MainActivity.this);

        //Setup the adapter
        adapter = new notesLayoutAdapter(notes, null);

        //Create the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        //Get the data from database
        storeData();

        //Update nonotes
        updateNoNotes();

        //FAB listener
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE);
        });

        //Listener for adapter interface
        adapter.setListener(new notesLayoutAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Note note = notes.get(position);

                //If any notes are selected, enable quick select
                if (adapter.bSelectionGroup) {
                    adapter.toggleSelection(position);
                    adapter.notifyDataSetChanged();
                    enableActionMode(position);
                }
                //If no notes are selected, edit the note
                else {
                    Intent intent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
                    intent.putExtra("INDEX", note.getId());
                    intent.putExtra("TITLE", note.getTitle());
                    intent.putExtra("NOTE", note.getNote());
                    startActivityForResult(intent, UPDATE_NOTE);
                }
            }

            @Override
            public void onNoteLongClick(int position) {
                Log.w(TAG, "onLongClick: pressed");
                enableActionMode(position);
            }

            @Override
            public void onSelection(boolean b) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //Select all notes, unless all notes already selected
        if (id == R.id.menu_selectall) {
            if (adapter.isAllSelected()) {
                adapter.deselectAll();
            } else {
                adapter.selectAll();
                enableActionMode(-1);
            }
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //When note is added, refresh the view
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            clearData();
            storeData();
            adapter.notifyDataSetChanged();
            updateNoNotes();
        }

        //When note is updated, refresh the view
        if (requestCode == UPDATE_NOTE && resultCode == RESULT_OK) {
            clearData();
            storeData();
            adapter.notifyDataSetChanged();
            updateNoNotes();
        }
    }

    /**
     * Stores the database items into the notes list, if any exist
     * *
     */
    void storeData() {
        Cursor cursor = dbManager.readAll();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Note note = new Note(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2));
                notes.add(note);
            }
        }
    }

    /**
     * Removes all objects in the notes list
     */
    void clearData() {
        notes.clear();
    }

    /**
     * Updates the visibility for the texts
     */
    void updateNoNotes() {
        if ((adapter.getItemCount() == 0) || (notes.size() == 0)) {
            noNotes.setVisibility(View.VISIBLE);
            appDesc.setVisibility(View.VISIBLE);
        } else {
            noNotes.setVisibility(View.INVISIBLE);
            appDesc.setVisibility(View.INVISIBLE);
        }
    }


    //Add notes to the adapter and the database for testing purposes
    void debugPopulateNotes() {
        try {
            clearData();
            dbManager.deleteAll();
        } catch (Exception e) {
            Log.d(TAG, "clearData and deleteAll failed");
        }

        Note note = new Note(null, "Test 1", "This is text for note 1");
        dbManager.insert(note);

        note = new Note(null, "Test 2", "This is text for note 2");
        dbManager.insert(note);

        note = new Note(null, "Test 3", "This is text for note 3");
        dbManager.insert(note);

        note = new Note(null, "Test 4", "This is text for note 4");
        dbManager.insert(note);

        note = new Note(null, "Test 5", "This is text for note 5");
        dbManager.insert(note);
    }

    //When a note is selected, open ActionMode

    /**
     * Create the ActionMode when any note is selected. Additionally, update the title to
     * detail how many notes were selected.
     *
     * @param position the adapter position that was selected
     */
    private void enableActionMode(int position) {
        Log.w(TAG, "enableActionMode: " + position);

        if (actionMode == null) {
            actionMode = startSupportActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_select, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    Log.w(TAG, "enableActionMode: onActionItemClicked item: " + item);

                    if (item.getItemId() == R.id.menu_delete) {
                        //Go through the notes. Delete selected from database
                        for (Note note : notes) {
                            if (note.isSelected())
                                dbManager.deleteById(Integer.parseInt(note.getId()));
                        }

                        //Delete selected notes from the adapter
                        adapter.deleteSelectedNotes();

                        //Reset the note objects so they are false
                        adapter.deselectAll();

                        //Refresh the view
                        adapter.notifyDataSetChanged();

                        //Show the no notes text if all notes removed
                        if (adapter.getSelectedSize() == 0)
                            updateNoNotes();

                        mode.finish();
                        return true;
                    }

                    return false;
                }

                @Override
                public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
                    //Make sure all notes are deselected
                    if ((adapter.getSelectedSize() != 0))
                        adapter.deselectAll();

                    //destroy the actionMode
                    actionMode = null;
                }
            });
        }

        //Set the dynamic title
        final int size = adapter.getSelectedSize();
        if (size == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(size + "");
            actionMode.invalidate();
        }
    }
}