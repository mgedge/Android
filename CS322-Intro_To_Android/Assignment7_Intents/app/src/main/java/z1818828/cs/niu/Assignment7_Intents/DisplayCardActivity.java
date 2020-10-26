/*********************************************************************
 CSCI 322/522  			  Assignment 7               		 FA2020

 Class Name: DisplayCardActivity.java

 Developer: Matthew Gedge
 Due Date: 6 November 2020

 Purpose: This java class runs the secondary activity. It simply sets the
 corresponding values for the layout. Additionally, it will setup the toolbar
 back button to support backwards navigation.
 *********************************************************************/
package z1818828.cs.niu.assignment7_intents;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayCardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_card);

        //Get the activity data from caller
        Intent intent = getIntent();
        String title = intent.getStringExtra(layoutAdapter.EXTRA);

        //Setup backwards navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set the variables
        TextView textviewTitle = findViewById(R.id.title);
        textviewTitle.setText(title);

        //Set up the gif viewer
        WebView gif = findViewById(R.id.gif);
        WebSettings webSettings = gif.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Create the description variable
        TextView textViewDesc = findViewById(R.id.description);
        textViewDesc.setMovementMethod(new ScrollingMovementMethod());


        //Set the description and the gif based on the title
        switch (textviewTitle.getText().toString()) {
            case "Squat":
                gif.loadUrl("file:///android_asset/squat-resize.gif");
                textViewDesc.setText("The squat is an exercise...");
                break;
            case "Bench":
                gif.loadUrl("file:///android_asset/bench.gif");
                textViewDesc.setText("The squat is an exercise...");
                break;
            case "Deadlift":
                gif.loadUrl("file:///android_asset/deadlift.webp");
                textViewDesc.setText("At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.");
                break;
            case "Overhead Press":
                gif.loadUrl("file:///android_asset/overhead.gif");
                textViewDesc.setText("The squat is an exercise...");
                break;
            case "Row":
                gif.loadUrl("file:///android_asset/row.gif");
                textViewDesc.setText("The squat is an exercise...");
                break;
            default:
                break;
        }
    }
}