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
import android.text.Html;
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

        //Hide the title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                textViewDesc.setText(Html.fromHtml(getString(R.string.Description_Long_Squat)));
                break;
            case "Bench":
                gif.loadUrl("file:///android_asset/bench-resize.gif");
                textViewDesc.setText(Html.fromHtml(getString(R.string.Description_Long_Bench)));
                break;
            case "Deadlift":
                gif.loadUrl("file:///android_asset/deadlift-resize.webp");
                textViewDesc.setText(Html.fromHtml(getString(R.string.Description_Long_Deadlift)));
                break;
            case "Overhead Press":
                gif.loadUrl("file:///android_asset/overhead-resize.gif");
                textViewDesc.setText(Html.fromHtml(getString(R.string.Description_Long_Overhead)));
                break;
            case "Row":
                gif.loadUrl("file:///android_asset/row-resize.gif");
                textViewDesc.setText(Html.fromHtml(getString(R.string.Description_Long_Row)));
                break;
            default:
                break;
        }
    }
}