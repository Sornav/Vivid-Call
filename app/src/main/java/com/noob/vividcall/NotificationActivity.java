package com.noob.vividcall;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity
{
private RecyclerView notification_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notification_list = findViewById(R.id.notification_list);
        notification_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    } //Declaring View Holder class for Recycler view.We need to inflate and send to the view holder,this usually takes place in the adapter class
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTxt;
        Button acceptBtn,cancelBtn;
        ImageView profileImageView;
        RelativeLayout cardView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTxt=itemView.findViewById(R.id.name_notification);
            acceptBtn=itemView.findViewById(R.id.request_accept_btn);
            cancelBtn=itemView.findViewById(R.id.request_decline_btn);
            profileImageView=itemView.findViewById(R.id.image_notification);
            cardView=itemView.findViewById(R.id.card_view);

        }
    }
}
