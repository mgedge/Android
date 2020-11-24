package edu.csi.niu.z1818828.assignment8_sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    private static final int ADD_NOTE = 1;
    private static final int UPDATE_NOTE = 2;
    private TextView noNotes;
    private DatabaseManager dbManager;
    private RecyclerView recyclerView;
    private notesLayoutAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ActionMode actionMode;
    protected ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noNotes = findViewById(R.id.textViewNoNotes);
        recyclerView = findViewById(R.id.main_recyclerView);
        dbManager = new DatabaseManager(MainActivity.this);

        layoutManager = new LinearLayoutManager(this);

        adapter = new notesLayoutAdapter(this, notes);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        storeData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE);
            }
        });

        adapter.setListener(new notesLayoutAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Note note = notes.get(position);
                Intent intent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
                intent.putExtra("INDEX", note.getId());
                intent.putExtra("TITLE", note.getTitle());
                intent.putExtra("NOTE", note.getNote());
                startActivityForResult(intent, UPDATE_NOTE);
            }

            @Override
            public void onNoteLongClick(int position) {
                if (actionMode != null)
                    return;

                enableActionMode(position);
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

        switch(id) {
            case R.id.menu_selectall:
                adapter.selectAll();
                enableActionMode(-1);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            clearData();
            storeData();
            adapter.notifyDataSetChanged();
            updateNoNotes();
        }

        if (requestCode == UPDATE_NOTE && resultCode == RESULT_OK) {
            clearData();
            storeData();
            adapter.notifyDataSetChanged();
            updateNoNotes();
        }
    }

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

    void clearData() {
        notes.clear();
    }

    void updateNoNotes() {
        if (adapter.getItemCount() == 0) {
            noNotes.setVisibility(View.VISIBLE);
        } else
            noNotes.setVisibility(View.INVISIBLE);
    }

    private void enableActionMode(int position) {
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
                if (item.getItemId() == R.id.menu_delete) {
                    for(Note note : notes) {
                        if(note.isSelected())
                            dbManager.deleteById(Integer.parseInt(note.getId()));
                    }

                    adapter.deleteNotes();

                    ArrayList<Note> notes = adapter.getNotes();
                    for(Note note : notes) {
                        if(note.isSelected())
                            note.setSelected(false);
                    }

                    adapter.notifyDataSetChanged();

                    mode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
                ArrayList<Note> notes = adapter.getNotes();
                for(Note note : notes) {
                    if(note.isSelected())
                        note.setSelected(false);
                }

                adapter.notifyDataSetChanged();
                actionMode = null;
            }
        });

        if((actionMode != null) && (position != -1)) {
            adapter.toggleSelection(position);
            final int size = adapter.selectedItems.size();
            if (size == 0) {
                actionMode.finish();
            } else {
                actionMode.setTitle(size + "");
                actionMode.invalidate();
            }
        }
    }
}