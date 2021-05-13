package com.example.quang.chatbot.Adapter;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quang.chatbot.Model.Buttons;
import com.example.quang.chatbot.Model.Message;
import com.example.quang.chatbot.R;

import java.util.ArrayList;
import java.util.List;

public class tableMessageAdapter extends  RecyclerView.Adapter<tableMessageAdapter.MyViewHolder>  implements View.OnClickListener {
    private ArrayList<Buttons> buttons ;
    private Activity activity;
    Context context;
    public tableMessageAdapter(ArrayList<Buttons> buttons){
        this.buttons=buttons;

    }

    public Buttons getItem(int position) {
        return buttons.get(position);
    }
    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public tableMessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buttons, parent, false);
        v.setOnClickListener(this);
        MyViewHolder vh = new MyViewHolder(v,viewType);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull tableMessageAdapter.MyViewHolder holder, int position) {
        Buttons button = buttons.get(position);
        String itemText="- "+button.getTitle();
        holder.listButtonView.setText(itemText);
    }

    @Override
    public int getItemCount() {
        return buttons.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView listButtonView;

        public MyViewHolder(View v,int viewType ) {
            super(v);
            listButtonView = v.findViewById(R.id.title_button);
        }
    }
}
