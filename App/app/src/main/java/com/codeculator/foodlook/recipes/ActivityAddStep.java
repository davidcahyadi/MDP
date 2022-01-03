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
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.RetrofitApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddStep extends AppCompatActivity {

    EditText step_title,step_description,step_countdown;
    Spinner spinner_step;
    Button btn_add_step;

    final int RECIPE_ID = 0;

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
                            int id = 0;
                            int order = 0;
                            String title = step_title.getText().toString();
                            int duration = 0;
                            String url = "";
                            if(step_countdown.getHint().toString().equals("Step Countdown")){
                                duration = Integer.parseInt(step_countdown.getText().toString());
                            }
                            else{
                                url = step_countdown.getText().toString();
                            }
                            String desc = step_description.getText().toString();
                            Step step = new Step(id,order,RECIPE_ID ,title,url,desc,duration);
                            insertStep(step);
                            finish();
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

    public void insertStep(Step step){
        PrefHelper prefHelper = new PrefHelper(this);
        Call<Step> call = RetrofitApi.getInstance().getRecipeService().addStep(step, prefHelper.getAccess());
        call.enqueue(new Callback<Step>() {
            @Override
            public void onResponse(Call<Step> call, Response<Step> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Insert Step Successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Step> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Insert Step Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}