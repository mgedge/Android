/************************************************************************
 *                                                                      *
 * CSCI 322/522  			  Assignment 5               		 FA2020 *
 *                                                            		    *
 * 	Class Name: MainActivity.java										*
 * 																		*
 *  Developer: Matthew Gedge											*
 *   Due Date: 9 October 2020							    			*
 *   																	*
 *    Purpose: This java class is the driver for the "burger" app.      *
 *    It creates the view, and has listeners for when inputs are changed*
 *    When changed, the calories are calculated and the calories are    *
 *    updated on the bottom.                                            *
 *																		*
 * *********************************************************************/

package z1818828.cs.niu.assignment6_radiobuttons;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int pattySelector = 0;
    int baconSelector = 0;
    int cheeseSelector = 0;
    int sauceSelector = 0;

    /******************************************************
     * onCreate starts the activity, creates the view, and monitors
     *      for changed inputs.
     ******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a calorieInfo object to track the calories
        final CalorieInfo calorieTotal = new CalorieInfo();

        //Calorie counter
        final TextView calories = findViewById(R.id.textViewCalories);
        calories.setText("Calories: " + CalorieInfo.calculateCalories());

        //Patty Radio Group Listener
        final RadioGroup patty = (RadioGroup) findViewById(R.id.pattyGroup);
        patty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioTurkeyPatty:
                        pattySelector = 1;
                        break;
                    case R.id.radioVeggiePatty:
                        pattySelector = 2;
                        break;
                    default:
                        pattySelector = 0;
                        break;
                }

                //Set the patty calories and update the calories
                calorieTotal.setPatty(pattySelector);
                calories.setText("Calories: " + CalorieInfo.calculateCalories());
            }
        });

        //Cheese Radio Group Listener
        final RadioGroup cheese = (RadioGroup) findViewById(R.id.cheeseGroup);
        cheese.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioCheddar:
                        cheeseSelector = 1;
                        break;
                    case R.id.radioMozz:
                        cheeseSelector = 2;
                        break;
                    default:
                        cheeseSelector = 0;
                        break;
                }

                //set the cheese calories and update the calories
                calorieTotal.setCheese(cheeseSelector);
                calories.setText("Calories: " + CalorieInfo.calculateCalories());
            }
        });

        //Bacon Check Box Listener
        final  CheckBox bacon = findViewById(R.id.toggleButtonBacon);
        bacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    baconSelector = 1;
                else
                    baconSelector = 0;

                //Set the cheese calories and update the calories
                calorieTotal.setBacon(baconSelector);
                calories.setText("Calories: " + CalorieInfo.calculateCalories());
            }
        });

        //Sauce Seekbar listener
        final SeekBar sauce = findViewById(R.id.seekBarSauce);
        sauce.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sauceSelector = progress;

                //Set the sauce and update the calories
                calorieTotal.setSpecialSauce(sauceSelector);
                calories.setText("Calories: " + CalorieInfo.calculateCalories());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //do nothing
            }
        });
    }
}