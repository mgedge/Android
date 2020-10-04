package z1818828.cs.niu.edu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView)findViewById(R.id.textViewTitle2)).setText(Html.fromHtml("Ax<sup>2</sup>+ Bx + C = 0"));
        ((TextView)findViewById(R.id.textViewCalculatedQuadratic)).setText(Html.fromHtml("Ax<sup>2</sup>+ Bx + C = 0"));

        final Button calculateButton = findViewById(R.id.buttonCalculate);
        final Button clearButton = findViewById(R.id.buttonClear);

        final EditText editTextQuadraticA = findViewById(R.id.editTextQuadraticA);
        final EditText editTextQuadraticB = findViewById(R.id.editTextQuadraticB);
        final EditText editTextQuadraticC = findViewById(R.id.editTextQuadraticC);

        final TextView xValue1 = findViewById(R.id.textViewFirstValue);
        final TextView xValue2 = findViewById(R.id.textViewSecondValue);

        final TextView calculatedQuadratic = findViewById(R.id.textViewCalculatedQuadratic);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if empty, check if zero
                if(TextUtils.isEmpty(editTextQuadraticA.getText().toString()) ||
                        TextUtils.isEmpty(editTextQuadraticB.getText().toString()) ||
                        TextUtils.isEmpty(editTextQuadraticC.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter values", Toast.LENGTH_SHORT).show();
                }
                else {
                    String temp = editTextQuadraticA.getText().toString();
                    double quadraticA = Double.parseDouble(temp);

                    temp = editTextQuadraticB.getText().toString();
                    double quadraticB = Double.parseDouble(temp);

                    temp = editTextQuadraticC.getText().toString();
                    double quadraticC = Double.parseDouble(temp);

                    if(quadraticA <= 0) {
                        Toast.makeText(MainActivity.this, "A must be greater than 0!", Toast.LENGTH_LONG).show();
                        editTextQuadraticA.setText("");
                    }
                    else {
                        DecimalFormat df = new DecimalFormat("0.00");

                        int discriminates = calculateDiscriminate(quadraticA,quadraticB,quadraticC);

                        String quadratic = editTextQuadraticA.getText().toString() + "x² + " + editTextQuadraticB.getText().toString() + "x + " + editTextQuadraticC.getText().toString() + " = 0";
                        calculatedQuadratic.setText(quadratic);

                        if(discriminates > 0) {
                            double firstValue = calculateQuadratic(quadraticA,quadraticB,quadraticC, 1);
                            double secondValue = calculateQuadratic(quadraticA,quadraticB,quadraticC, 0);

                            xValue1.setText(df.format(firstValue));
                            xValue2.setText(df.format(secondValue));
                        }
                        else if(discriminates == 0) {
                            double firstValue = calculateQuadratic(quadraticA,quadraticB,quadraticC, 1);

                            xValue1.setText(df.format(firstValue));
                            xValue2.setText("No value");
                        }
                        else {
                            xValue1.setText("Imaginary");
                            xValue2.setText("Imaginary");
                        }
                    }
                }

            }
        });

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
                calculatedQuadratic.setText(Html.fromHtml("Ax<sup>2</sup>+ Bx + C = 0"));
            }
        });
    }

    private double calculateQuadratic(double a, double b, double c, int pos) {
        double quadratic = 0;

        if(pos == 1) {
            quadratic = (-b + Math.sqrt(Math.pow(b,2) - (4 * a * c))) / (2 * a);
        }
        else {
            quadratic = (-b - Math.sqrt(Math.pow(b,2) - (4 * a * c))) / (2 * a);
        }

        return quadratic;
    }

    private int calculateDiscriminate(double a, double b, double c) {
        int discriminate = 0;

        try {
            discriminate = (int) ((int)(b * b) - (4 * a * c));
        } catch (Exception e) {
            System.err.println("Calculating discriminate failed! Reason: " + e);
            return 0;
        }
        return discriminate;    }
}