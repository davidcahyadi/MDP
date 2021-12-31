package com.codeculator.foodlook.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.User;

import java.util.ArrayList;

public class AdminUserListAdapter extends RecyclerView.Adapter<AdminUserListAdapter.AdminListUserHolder>{
    ArrayList<User> users;
    Context context;
    public ListClickListener listClickListener;

    public AdminUserListAdapter(ArrayList<User> users, Context context){
        this.users = users;
        this.context = context;
    }

    public void setListClickListener(ListClickListener listClickListener){
        this.listClickListener = listClickListener;
    }

    @NonNull
    @Override
    public AdminListUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_component_user_list,parent,false);
        return new AdminListUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListUserHolder holder, int position) {
        holder.bind(users.get(position), position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class AdminListUserHolder extends RecyclerView.ViewHolder {
        ImageView displayPictureIV;
        ImageButton userMoreButton;
        TextView displayNameTV, emailTV, joinedSinceTV;
        public AdminListUserHolder(@NonNull View itemView) {
            super(itemView);
            displayNameTV = itemView.findViewById(R.id.displayNameTV);
            emailTV = itemView.findViewById(R.id.emailTV);
            joinedSinceTV = itemView.findViewById(R.id.joinedSinceTV);
            displayPictureIV = itemView.findViewById(R.id.displayPicIV);
            userMoreButton = itemView.findViewById(R.id.user_more_btn);
        }

        public void bind(User user, int index){
            displayNameTV.setText(user.getName());
            emailTV.setText(user.getEmail());
            joinedSinceTV.setText("Joined since: " + user.getCreated_at());
//            displayPictureIV.setImageResource();
            userMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listClickListener.moreButtonClick(user.getId(), userMoreButton);
                }
            });
        }
    }

    public interface ListClickListener{
        void moreButtonClick(int userID, ImageButton btn);
    }
}
