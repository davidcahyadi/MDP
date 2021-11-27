package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.local.Step;

import java.util.ArrayList;
import java.util.Random;

public class StepSliderAdapter extends PagerAdapter {

    Context context;
    public ArrayList<Step> steps = new ArrayList<>();

    LayoutInflater layoutInflater;

    public StepSliderAdapter(Context context, ArrayList<Step> steps){
        this.context = context;
        this.steps = steps;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.step_slider, container, false);

        ImageView stepImage = (ImageView) view.findViewById(R.id.stepImage);
        TextView stepContentTv = (TextView) view.findViewById(R.id.stepContentTv);

        stepContentTv.setText(steps.get(position).description);

        if(steps.get(position).url.equalsIgnoreCase("")){
            int[] randomIcon = new int[3];
            randomIcon[0] = R.drawable.ic_random_icon_1;
            randomIcon[1] = R.drawable.ic_random_icon_2;
            randomIcon[2] = R.drawable.ic_random_icon_3;
            Random rand = new Random();
            int doRand = rand.nextInt(3);
            stepImage.setImageResource(randomIcon[doRand]);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
