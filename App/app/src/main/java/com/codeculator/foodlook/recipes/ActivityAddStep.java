package com.codeculator.foodlook.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codeculator.foodlook.R;

public class ActivityAddStep extends AppCompatActivity {

    EditText step_title,step_description,step_countdown;
    Spinner spinner_step;
    Button btn_add_step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_step);

        step_title = findViewById(R.id.step_title);
        step_description = findViewById(R.id.step_description);
        step_countdown = findViewById(R.id.step_countdown);
        spinner_step = findViewById(R.id.spinner_step);
        btn_add_step = findViewById(R.id.btn_add_step);

        btn_add_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(step_title.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Step Title is missing", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(step_description.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Step Description is missing", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(step_countdown.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "Step Countdown / Photo Url is missing", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // todo masukkin ke retrofitnya
                        }
                    }
                }
            }
        });

        spinner_step.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_step.getItemAtPosition(i).toString().equalsIgnoreCase("timer")){
                    step_countdown.setHint("Step Countdown");
                }
                else{
                    step_countdown.setHint("Photo Url");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner_step.setSelection(0);
            }
        });
    }
}