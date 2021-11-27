package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.WelcomeCard;

import java.util.List;

public class WelcomeCardAdapter extends PagerAdapter {
    private Context context;
    private List<WelcomeCard> welcomeCardList;
    private OnClickListener<WelcomeCard> listener;
    private LayoutInflater layoutInflater;

    public WelcomeCardAdapter(Context context,List<WelcomeCard> welcomeCardList){
        this.context = context;
        this.welcomeCardList = welcomeCardList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setOnClickListener(OnClickListener<WelcomeCard>  listener){
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return welcomeCardList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_card, container,false);

        LottieAnimationView animationView = view.findViewById(R.id.animationView);
        TextView title = view.findViewById(R.id.title);
        WelcomeCard wc = welcomeCardList.get(position);

        animationView.setAnimation(wc.getResource());
        title.setText(wc.getTitle());

        if(listener != null){
            view.setOnClickListener(v -> {
                listener.onClick(wc);
            });
        }
        container.addView(view);
        return view;
    }
}

