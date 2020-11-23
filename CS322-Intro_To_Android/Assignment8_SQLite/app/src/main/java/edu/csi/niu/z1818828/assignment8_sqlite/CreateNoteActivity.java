package edu.csi.niu.z1818828.assignment8_sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class CreateNoteActivity extends AppCompatActivity {
    private DatabaseManager dbManager;
    private EditText titleEditText;
    private EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbManager = new DatabaseManager(this);

        titleEditText = findViewById(R.id.editTextTextTitle);
        noteEditText = findViewById(R.id.editTextTextMultiLineNote);

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
            default:
                break;
        }

        return true;
    }

    public void insert() {
        String title = titleEditText.getText().toString();
        String notet = noteEditText.getText().toString();

        Note note = new Note(title, notet);
        dbManager.insert(note);

        Toast.makeText(this, "Note successfully added!", Toast.LENGTH_SHORT).show();
        finish();
    }
}