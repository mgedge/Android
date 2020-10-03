package z1818828.cs.niu.edu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean hello = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.helloWorldButton);
        final TextView text = findViewById(R.id.helloWorldTextView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hello) {
                    text.setText("Goodbye!");
                    hello = false;
                }
                else {
                    text.setText("Hello World!");
                    hello = true;
                }
            }
        });
    }
}