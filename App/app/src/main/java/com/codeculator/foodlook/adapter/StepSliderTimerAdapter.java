package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Step;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class StepSliderTimerAdapter extends PagerAdapter {
    Context context;
    public ArrayList<Step> steps = new ArrayList<>();
    LayoutInflater layoutInflater;

    public StepSliderTimerAdapter(Context context, ArrayList<Step>steps){
        this.context = context;
        this.steps = steps;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.step_slider, container, false);

        LottieAnimationView animation = view.findViewById(R.id.timerAnimation);
        animation.setAnimation(R.raw.timer);
        TextView stepContentTv = (TextView) view.findViewById(R.id.stepContentTv);

        stepContentTv.setText(steps.get(position).description);

        container.addView(view);
        return view;
    }
}
