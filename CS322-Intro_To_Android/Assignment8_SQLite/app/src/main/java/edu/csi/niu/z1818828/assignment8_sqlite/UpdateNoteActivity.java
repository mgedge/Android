package edu.csi.niu.z1818828.assignment8_sqlite;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateNoteActivity extends AppCompatActivity {
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
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_save:
                update();
                break;
            case R.id.menu_delete:
                delete();
                break;
            default:
                break;
        }

        return true;
    }

    public void update() {
        String title = titleEditText.getText().toString();
        String notet = noteEditText.getText().toString();

        Note note = new Note(title, notet);
        dbManager.insert(note);

        Toast.makeText(this, "Note successfully added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void delete() {
        dbManager.deleteById(1); //TODO change to match the calling card Pos

        Toast.makeText(this, "Note successfully deleted!", Toast.LENGTH_SHORT).show();
        finish();
    }
}