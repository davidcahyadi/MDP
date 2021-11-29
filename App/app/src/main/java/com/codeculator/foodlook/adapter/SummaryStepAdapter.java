package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Step;

import java.util.ArrayList;

public class SummaryStepAdapter extends RecyclerView.Adapter<SummaryStepAdapter.SummaryStepHolder> {
    Context context;
    ArrayList<Step> steps;
    OnClickListener<Step> onClick;

    public SummaryStepAdapter(Context context,ArrayList<Step> steps){
        this.context = context;
        this.steps = steps;
    }

    public void setOnClickListener(OnClickListener<Step> onClick){
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public SummaryStepAdapter.SummaryStepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_summary,parent,false);
        return new SummaryStepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryStepAdapter.SummaryStepHolder holder, int position) {
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class SummaryStepHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvDescription,tvType;

        public SummaryStepHolder(@NonNull View iv) {
            super(iv);

            tvTitle = iv.findViewById(R.id.title);
            tvDescription = iv.findViewById(R.id.description);
            tvType = iv.findViewById(R.id.type);
        }

        public void bind(Step step){
            tvTitle.setText(step.title);
            tvDescription.setText(step.description);
            if(step.duration != 0){
                tvType.setText("Timer");
            }
            else if (step.url.length()  > 0){
                tvType.setText("Photo");
            }
            else {
                tvType.setText("Text");
            }
        }
    }
}
