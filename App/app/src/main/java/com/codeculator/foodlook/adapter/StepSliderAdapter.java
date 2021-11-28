package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.steps.AlarmReceiver;
import com.codeculator.foodlook.steps.DatePickerFragment;
import com.codeculator.foodlook.steps.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class StepSliderAdapter extends PagerAdapter implements DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener{

    Context context;
    public ArrayList<Step> steps = new ArrayList<>();
    String DATE_PICKER_TAG = "DatePicker";
    String TIME_PICKER_ONCE_TAG = "TimePickerOnce";
    String TIME_PICKER_REPEAT_TAG = "TimePickerRepeat";
    AlarmReceiver alarmReceiver;

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



        EditText edtOnceMessage;
//
//        ImageButton btnOnceDate = view.findViewById(R.id.btn_once_date);
//        ImageButton btnOnceTime = view.findViewById(R.id.btn_once_time);
//        Button btnSetOnce = view.findViewById(R.id.btn_set_once_alarm);
//        edtOnceMessage = view.findViewById(R.id.edt_once_message);
//
//        alarmReceiver = new AlarmReceiver();
//
//        btnOnceDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerFragment datePickerFragment = new DatePickerFragment();
//
//                datePickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), DATE_PICKER_TAG);
//            }
//        });
//        btnOnceTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TimePickerFragment timePickerFragmentOne = new TimePickerFragment();
//                timePickerFragmentOne.show(((FragmentActivity)context).getFragmentManager(), TIME_PICKER_ONCE_TAG);
//            }
//        });
//        btnSetOnce.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar cal = Calendar.getInstance();
//
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                String onceDate = formatter.format(cal.getTime());
//
//                cal.add(Calendar.SECOND, 2);
//                formatter = new SimpleDateFormat("HH:mm:ss");
//                String onceTime = formatter.format(cal.getTime());
//
//                String onceMessage = edtOnceMessage.getText().toString();
//                alarmReceiver.setOneTimeAlarm(context, AlarmReceiver.TYPE_ONE_TIME,
//                        onceDate,
//                        onceTime,
//                        onceMessage);
//            }
//        });
//
//        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @Override
    public void onDialogDateSet(String tag, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    }

    @Override
    public void onDialogTimeSet(String tag, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }
}
