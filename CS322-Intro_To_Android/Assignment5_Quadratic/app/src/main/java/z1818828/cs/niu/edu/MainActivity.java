/*********************************************************************
 CSCI 322/522  			  Assignment 5               		 FA2020

 Class Name: MainActivity.java

 Developer: Matthew Gedge
 Due Date: 11 September 2020

 Purpose: This java class starts the activity and runs the UI.
 When the calculate button is pressed, the inputs are checked for
 validity and the discriminate and x values are calculated.
 When the clear button is pressed, inputs and outputs are emptied.
 *********************************************************************/
package z1818828.cs.niu.edu;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    //Input text
    EditText editTextQuadraticA;
    EditText editTextQuadraticB;
    EditText editTextQuadraticC;
    //Output text
    TextView xValue1;
    TextView xValue2;
    TextView calculatedQuadratic;
    //Input values
    private double quadraticA;
    private double quadraticB;
    private double quadraticC;

    /******************************************************
     onCreate creates the activity and runs the application
    ******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set quadratic formulas
        ((TextView) findViewById(R.id.textViewTitle2)).setText(R.string.default_equation);
        ((TextView) findViewById(R.id.textViewCalculatedQuadratic)).setText(R.string.default_equation);

        //Set the inputs
        editTextQuadraticA = findViewById(R.id.editTextQuadraticA);
        editTextQuadraticB = findViewById(R.id.editTextQuadraticB);
        editTextQuadraticC = findViewById(R.id.editTextQuadraticC);

        //Set the outputs
        xValue1 = findViewById(R.id.textViewFirstValue);
        xValue2 = findViewById(R.id.textViewSecondValue);
        calculatedQuadratic = findViewById(R.id.textViewCalculatedQuadratic);

        /******************************************************
         calculateButton.setOnClickListener verifies the input
         and calculates both the discriminate and x values.
         Once solved, output is displayed
        ******************************************************/
        final Button calculateButton = findViewById(R.id.buttonCalculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if empty
                if (TextUtils.isEmpty(editTextQuadraticA.getText().toString()) ||
                        TextUtils.isEmpty(editTextQuadraticB.getText().toString()) ||
                        TextUtils.isEmpty(editTextQuadraticC.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter values", Toast.LENGTH_SHORT).show();
                } else {
                    //Get the inputs
                    String temp = editTextQuadraticA.getText().toString();
                    quadraticA = Double.parseDouble(temp);

                    temp = editTextQuadraticB.getText().toString();
                    quadraticB = Double.parseDouble(temp);

                    temp = editTextQuadraticC.getText().toString();
                    quadraticC = Double.parseDouble(temp);

                    //Verify A is greater than 0
                    if (quadraticA <= 0) {
                        Toast.makeText(MainActivity.this, "'A' must be greater than 0!", Toast.LENGTH_LONG).show();
                        editTextQuadraticA.setText("");
                    } else {
                        DecimalFormat df = new DecimalFormat("0.00");

                        //Calculate discriminate
                        int discriminates = calculateDiscriminate(quadraticA, quadraticB, quadraticC);

                        //Rewrite the quadratic formula using inputs
                        String quadratic = editTextQuadraticA.getText().toString() + "x² + " + editTextQuadraticB.getText().toString() + "x + " + editTextQuadraticC.getText().toString() + " = 0";
                        calculatedQuadratic.setText(quadratic);

                        //Calculate x values and print
                        if (discriminates > 0) {
                            double firstValue = calculateQuadratic(quadraticA, quadraticB, quadraticC, 1);
                            double secondValue = calculateQuadratic(quadraticA, quadraticB, quadraticC, 0);

                            xValue1.setText(df.format(firstValue));
                            xValue2.setText(df.format(secondValue));
                        } else if (discriminates == 0) {
                            double firstValue = calculateQuadratic(quadraticA, quadraticB, quadraticC, 1);

                            xValue1.setText(df.format(firstValue));
                            xValue2.setText(R.string.no_vale);
                        } else {
                            xValue1.setText(R.string.imaginary);
                            xValue2.setText(R.string.imaginary);
                        }
                    }
                }
            }
        });

        /******************************************************
         clearButton.setOnClickLister restores the i/o values to default
        ******************************************************/
        final Button clearButton = findViewById(R.id.buttonClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear editTexts
                editTextQuadraticA.setText("");
                editTextQuadraticB.setText("");
                editTextQuadraticC.setText("");

                //Clear output
                xValue1.setText("X");
                xValue2.setText("X");
                calculatedQuadratic.setText(R.string.default_equation);
            }
        });

        //When device is rotated, reset variables and set texts
        if (savedInstanceState != null) {
            //Quadratic A
            String temp = savedInstanceState.getString("quadA");
            if (!temp.isEmpty()) {
                quadraticA = Double.parseDouble(savedInstanceState.getString("quadA"));
                editTextQuadraticA.setText(temp);
            }

            //Quadratic B
            temp = savedInstanceState.getString("quadB");
            if (!temp.isEmpty()) {
                quadraticB = Double.parseDouble(savedInstanceState.getString("quadB"));
                editTextQuadraticB.setText(temp);
            }

            //Quadratic C
            temp = savedInstanceState.getString("quadC");
            if (!temp.isEmpty()) {
                quadraticC = Double.parseDouble(savedInstanceState.getString("quadC"));
                editTextQuadraticC.setText(temp);
            }

            //Calculated Output
            temp = savedInstanceState.getString("quadratic");
            calculatedQuadratic.setText(String.valueOf(temp));

            //X Values
            temp = savedInstanceState.getString("X1");
            xValue1.setText(String.valueOf(temp));

            temp = savedInstanceState.getString("X2");
            xValue2.setText(String.valueOf(temp));
        }
    }

    /******************************************************
     onRestoreInstanceState restores state
    ******************************************************/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /******************************************************
     onSaveInstanceState saves the variables for state change
    ******************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save Input
        outState.putString("quadA", editTextQuadraticA.getText().toString());
        outState.putString("quadB", editTextQuadraticB.getText().toString());
        outState.putString("quadC", editTextQuadraticC.getText().toString());

        //Save Output
        outState.putString("quadratic", calculatedQuadratic.getText().toString());
        outState.putString("X1", xValue1.getText().toString());
        outState.putString("X2", xValue2.getText().toString());
    }

    /******************************************************
     calculateQuadratic takes 3 doubles and a binary int,
     returns the quadratic value for a single set.
    ******************************************************/
    private double calculateQuadratic(double a, double b, double c, int pos) {
        double quadratic;
        double sqrt = Math.sqrt(Math.pow(b, 2) - (4 * a * c));

        if (pos == 1) {
            quadratic = (-b + sqrt) / (2 * a);
        } else {
            quadratic = (-b - sqrt) / (2 * a);
        }

        return quadratic;
    }

    /******************************************************
     calculateDiscriminate takes 3 doubles, calculates  and
     returns the discriminate.
    ******************************************************/
    private int calculateDiscriminate(double a, double b, double c) {
        int discriminate;

        discriminate = (int) ((int) (b * b) - (4 * a * c));

        return discriminate;
    }
}