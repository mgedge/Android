/************************************************************************
 *                                                                      *
 * CSCI 428       			  Assignment 2               		 SP2021 *
 *                                                            		    *
 * 	Class Name: SettingsActivity.java	    							*
 * 																		*
 *  Developer: Matthew Gedge											*
 *  Due Date: 18 February 2021							    			*
 *   																	*
 *  Purpose: This generated activity handles the settings activity for  *
 *  user preferences. Here the user can change the settings of the game.*
 *																		*
 * *********************************************************************/

package edu.csi.niu.z1818828.blackjack;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

/**
 * This class handles the settings activity to modify the gameplay
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}