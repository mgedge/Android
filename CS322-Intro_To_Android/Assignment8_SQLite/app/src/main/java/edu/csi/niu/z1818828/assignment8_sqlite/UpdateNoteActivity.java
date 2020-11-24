package edu.csi.niu.z1818828.assignment8_sqlite;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateNoteActivity extends AppCompatActivity {
    private static final String TAG = "UpdateNoteActivity";
    private DatabaseManager dbManager;
    private EditText titleEditText;
    private EditText noteEditText;
    String title;
    String desc;
    String index;
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        dbManager = new DatabaseManager(this);

        title = getIntent().getStringExtra("TITLE");
        desc = getIntent().getStringExtra("NOTE");
        index = getIntent().getStringExtra("INDEX");
        try {
            id = Integer.parseInt(index);
        } catch(NumberFormatException nfe) {
            Toast.makeText(this, "INVALID ID!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: Invalid id");
        }

        titleEditText = findViewById(R.id.editTextTextTitle);
        titleEditText.setText(title);
        noteEditText = findViewById(R.id.editTextTextMultiLineNote);
        noteEditText.setText(desc);

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
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    public void update() {
        String title = titleEditText.getText().toString();
        String note = noteEditText.getText().toString();

        dbManager.updateByID(id, title, note);

        Toast.makeText(this, "Note successfully updated!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    public void delete() {
        if(id != -1)
            dbManager.deleteById(id);
        else {
            Toast.makeText(this, "Cannot delete this note!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Note successfully deleted!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}