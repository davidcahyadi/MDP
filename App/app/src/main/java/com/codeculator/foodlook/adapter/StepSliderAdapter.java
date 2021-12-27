package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.steps.AlarmReceiver;
import com.codeculator.foodlook.steps.DatePickerFragment;
import com.codeculator.foodlook.steps.TimePickerFragment;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class StepSliderAdapter extends PagerAdapter{

    Context context;
    ArrayList<View> views;
    public ArrayList<Step> steps;
    LayoutInflater layoutInflater;

    CountDownTimer timer;
    Animation rotating;
    ImageView bgStopwatch;

    Vibrator v;
    MediaPlayer mMediaPlayer;

    public StepSliderAdapter(Context context, ArrayList<Step> steps){
        this.context = context;
        this.steps = steps;
        views = new ArrayList<>();
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
        if(views.size() <= position)
            views.add(view);
        ImageView stepImage = view.findViewById(R.id.stepImage);
        TextView stepContentTv = view.findViewById(R.id.stepContentTv);

        LinearLayout timerIndicator = view.findViewById(R.id.timerIndicator);
        TextView timerSeconds = view.findViewById(R.id.timerSeconds);
        TextView timerMinutes = view.findViewById(R.id.timerMinutes);

        ImageView arrowStopwatch = view.findViewById(R.id.arrowStopwatch);
        bgStopwatch = view.findViewById(R.id.bgStopwatch);

        Button startBtn = view.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer(position, arrowStopwatch, timerSeconds, timerMinutes);
            }
        });

        Button stopBtn = view.findViewById(R.id.stopBtn);
        stopBtn.setAlpha(0);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopNotification(position);
            }
        });

        int recipeDuration = steps.get(position).duration;
        timerSeconds.setText(String.format("%2s", recipeDuration % 60).replace(' ', '0'));
        timerMinutes.setText(String.format("%2s", recipeDuration / 60).replace(' ', '0'));

        if(recipeDuration > 0){
            stepImage.setVisibility(View.INVISIBLE);
            arrowStopwatch.setVisibility(View.VISIBLE);
            bgStopwatch.setVisibility(View.VISIBLE);
            timerIndicator.setVisibility(View.VISIBLE);
            startBtn.setVisibility(View.VISIBLE);
        }else{
            stepImage.setVisibility(View.VISIBLE);
            arrowStopwatch.setVisibility(View.INVISIBLE);
            bgStopwatch.setVisibility(View.INVISIBLE);
            timerIndicator.setVisibility(View.INVISIBLE);
            startBtn.setVisibility(View.INVISIBLE);
        }

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
        container.removeView((LinearLayout)object);
    }

    public void stopTimer(int position){
        if(this.timer!=null)
            this.timer.cancel();
        View view = views.get(position);
        TextView timerSeconds = view.findViewById(R.id.timerSeconds);
        TextView timerMinutes = view.findViewById(R.id.timerMinutes);
        ImageView arrowStopwatch = view.findViewById(R.id.arrowStopwatch);
        timerSeconds.setText("00");
        timerMinutes.setText("00");
        arrowStopwatch.clearAnimation();
    }

    public void startTimer(int position, ImageView arrow, TextView timeSeconds, TextView timeMinutes){
        int recipeDuration = steps.get(position).duration;
        if(recipeDuration > 0){
            recipeDuration++;
            rotating = AnimationUtils.loadAnimation(context, R.anim.rotating);
            rotating.setDuration((int)recipeDuration * 1000);
            rotating.setRepeatMode(Animation.RESTART);
            arrow.startAnimation(rotating);
            timer = new CountDownTimer((recipeDuration) * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    int secondsRemaining = (int) millisUntilFinished / 1000;
                    timeSeconds.setText(String.format("%2s", secondsRemaining % 60).replace(' ', '0'));
                    timeMinutes.setText(String.format("%2s", secondsRemaining / 60).replace(' ', '0'));
                }
                public void onFinish() {
                    arrow.clearAnimation();
                    startRinging(position);
                }
            }.start();
        }
    }

    public void startRinging(int position){
        vibrateThePhone();
        ringThePhone();

        Button stopBtn = views.get(position).findViewById(R.id.stopBtn);
        stopBtn.animate().alpha(1).translationY(-100).setDuration(500).start();
        Button startBtn = views.get(position).findViewById(R.id.startBtn);
        startBtn.animate().alpha(0).translationY(40).setDuration(500).start();
    }

    public void vibrateThePhone(){
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1500, 1000};
        v.vibrate(pattern, 0);
    }

    public void ringThePhone(){
        try {
            Uri alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch(Exception e) {

        }
    }

    public void stopNotification(int position){
        v.cancel();
        mMediaPlayer.stop();
        Button stopBtn = views.get(position).findViewById(R.id.stopBtn);
        stopBtn.animate().alpha(0).translationY(40).setDuration(500).start();
        Button startBtn = views.get(position).findViewById(R.id.startBtn);
        startBtn.animate().alpha(1).translationY(-10).setDuration(500).start();
    }
}


