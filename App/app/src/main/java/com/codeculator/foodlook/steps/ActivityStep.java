package com.codeculator.foodlook.steps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.StepSliderAdapter;
import com.codeculator.foodlook.local.AddStep;
import com.codeculator.foodlook.local.DeleteAllStep;
import com.codeculator.foodlook.local.FetchCallback;
import com.codeculator.foodlook.local.LoadStep;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.local.StoreCallback;
import com.codeculator.foodlook.services.HTTPRequest;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ActivityStep extends AppCompatActivity{

    private ViewPager sliderView;
    private LinearLayout sliderNavigation, mDotLayout;

    private StepSliderAdapter stepAdapter;

    HTTPRequest request;
    ArrayList<Step> steps = new ArrayList<>();
    private TextView[] mDots;

    Button prevBtn, nextBtn;
    private int mCurrentPage;

    ProgressBar loadingBar;

    AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        sliderView = findViewById(R.id.slideViewer);
        sliderNavigation = findViewById(R.id.sliderNavigation);
        mDotLayout = findViewById(R.id.dotsLayout);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextButton);
        loadingBar = findViewById(R.id.loadingBar);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentPage == mDots.length - 1){
                    Intent i = new Intent(getBaseContext(), SubmitActivity.class);
                    startActivity(i);
                }else{
                    sliderView.setCurrentItem(mCurrentPage + 1);
                }
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderView.setCurrentItem(mCurrentPage - 1);
            }
        });

        request = new HTTPRequest(this);
        HTTPRequest.Response<String> response = new HTTPRequest.Response<>();
        new DeleteAllStep(ActivityStep.this, new StoreCallback() {
            @Override
            public void preProcess() {

            }

            @Override
            public void postProcess() {
                response.onError(e->{
                    System.out.println("Request error: " + e.getMessage());
                });
                response.onSuccess(res->{
                    System.out.println("response: " + res);
                    try{
                        ArrayList<Step> fetchedSteps = new ArrayList<>();
                        JSONArray json = new JSONArray(res);

                        for (int i = 0; i < json.length(); i++) {
                            fetchedSteps.add(new Step(json.getJSONObject(i).getInt("id"),
                                    json.getJSONObject(i).getInt("order"),
                                    json.getJSONObject(i).getString("title"),
                                    json.getJSONObject(i).getString("url"),
                                    json.getJSONObject(i).getString("description"),
                                    json.getJSONObject(i).getInt("duration")));
                        }
                        new AddStep(ActivityStep.this, new StoreCallback() {
                            @Override
                            public void preProcess() {

                            }

                            @Override
                            public void postProcess() {
                                System.out.println("Steps added");
                                new LoadStep(ActivityStep.this, new FetchCallback<ArrayList<Step>>() {
                                    @Override
                                    public void preProcess() {

                                    }

                                    @Override
                                    public void postProcess(ArrayList<Step> data) {
                                        steps.clear();
                                        steps.addAll(data);
                                        System.out.println("Fetched steps size: " + steps.size());
                                        stepAdapter = new StepSliderAdapter(ActivityStep.this, steps);
                                        sliderView.setAdapter(stepAdapter);
                                        addDotsIndicator(0);
                                        sliderView.addOnPageChangeListener(viewListener);

                                        loadingBar.setVisibility(View.INVISIBLE);
                                    }
                                }).execute();
                            }
                        }).execute(fetchedSteps);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                });
                HashMap<String,String> data = new HashMap<>();
                request.get("https://fl.codeculator.com/api/v1/recipe/34/summary",data,response);
                loadingBar.setVisibility(View.VISIBLE);
            }
        }).execute();
        System.out.println("just called request");
    }

    public void addDotsIndicator(int pos){
        mDots = new TextView[steps.size()];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparent_white));

            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[pos].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            mCurrentPage = position;
            stopRinging();
            if(steps.get(mCurrentPage).duration > 0)
                startTimer(steps.get(mCurrentPage).duration);
            if(mCurrentPage == 0){
                nextBtn.setEnabled(true);
                nextBtn.setText("NEXT");
                prevBtn.setEnabled(false);
                prevBtn.setVisibility(View.INVISIBLE);
                prevBtn.setText("");
            }else if(mCurrentPage == mDots.length - 1){
                nextBtn.setEnabled(true);
                nextBtn.setText("Finish");
                prevBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);
                prevBtn.setText("Back");
            }else{
                nextBtn.setEnabled(true);
                nextBtn.setText("Next");
                prevBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);
                prevBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public void startTimer(int seconds){
        System.out.println("ringing");
        String repeatMessage = "Time's Up!";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, seconds);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String repeatTime = formatter.format(cal.getTime());
        alarmReceiver.setRepeatingAlarm(getBaseContext(), AlarmReceiver.TYPE_REPEATING,
                repeatTime, repeatMessage);
    }

    public void stopRinging(){
        alarmReceiver.stopLoopingNotifSound(getBaseContext());
    }
}