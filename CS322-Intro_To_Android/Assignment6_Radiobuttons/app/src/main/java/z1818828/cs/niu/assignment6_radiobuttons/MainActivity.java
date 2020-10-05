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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final calorieInfo caloriesInfo = new calorieInfo();

        final TextView calories = findViewById(R.id.textViewCalories);
        calories.setText("Calories: " + calorieInfo.calculateCalories());

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

                caloriesInfo.setPatty(pattySelector);
                calories.setText("Calories: " + calorieInfo.calculateCalories());
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
                caloriesInfo.setCheese(cheeseSelector);
                calories.setText("Calories: " + calorieInfo.calculateCalories());
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
                caloriesInfo.setBacon(baconSelector);
                calories.setText("Calories: " + calorieInfo.calculateCalories());
            }
        });

        final SeekBar sauce = findViewById(R.id.seekBarSauce);
        sauce.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sauceSelector = progress;
                caloriesInfo.setSpecialSauce(sauceSelector);
                calories.setText("Calories: " + calorieInfo.calculateCalories());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}