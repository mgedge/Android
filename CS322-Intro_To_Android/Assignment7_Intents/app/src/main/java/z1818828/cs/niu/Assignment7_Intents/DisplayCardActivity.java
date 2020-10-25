package z1818828.cs.niu.assignment7_intents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_card);

        Intent intent = getIntent();
        String title = intent.getStringExtra(layoutAdapter.EXTRA);

        TextView textView = findViewById(R.id.textView2);
        textView.setText(title);
    }
}