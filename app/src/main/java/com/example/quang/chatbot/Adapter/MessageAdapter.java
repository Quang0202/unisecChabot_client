package com.example.quang.chatbot.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quang.chatbot.Model.Buttons;
import com.example.quang.chatbot.Model.Message;
import com.example.quang.chatbot.Model.Tables;
import com.example.quang.chatbot.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
public class MessageAdapter extends  RecyclerView.Adapter<MessageAdapter.MyViewHolder>  implements View.OnClickListener {
    private ArrayList<Message> messages;
    private ArrayList<Tables> tables=new ArrayList<>();
    private ArrayList<Buttons> buttons;
    private Activity activity;
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, BUTTON_MESSAGE=2,TABLE_MESSAGE=3;
    Context context;
    private int flag=0;
    public MessageAdapter(ArrayList<Message>myDataset,Activity mActivity){
        this.messages=myDataset;
        this.activity=mActivity;
    }
    public void add(Message message) {
        this.messages.add(message);
        notifyItemInserted(messages.size());
        notifyDataSetChanged();
    }
    public void addButtons(ArrayList<Buttons> buttons){
        this.buttons=buttons;
        notifyItemInserted(buttons.size());
        notifyDataSetChanged();
    }
    public void addTable(Tables table){
       this.tables.add(table);
        notifyItemInserted(tables.size());
    }
    @Override
    public int getItemViewType(int position) {
        Message item = getItem(position);
        if (item.isMine()) {
            flag=0;
            return MY_MESSAGE;
        }
        else {
             if(item.getType()=="text") {
                 flag=0;
                 return OTHER_MESSAGE;
             }
             if(item.getType()=="button") {
                 flag=1;
                 return BUTTON_MESSAGE;
             }
             if(item.getType()=="table"){
                 flag=2;
                 return TABLE_MESSAGE;
             }
        }
        return -1;
    }
    public Message getItem(int position) {
        return messages.get(position);
    }
    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==0){
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_send_message, parent, false);
            v.setOnClickListener(this);
            MyViewHolder vh = new MyViewHolder(v,viewType);
            return vh;
        }
        else if(viewType==1){
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_response_message, parent, false);
            v.setOnClickListener(this);
           MyViewHolder vh = new MyViewHolder(v,viewType);
            return vh;
        }
        else if(viewType==2){
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_response_buttons_message, parent, false);
            v.setOnClickListener(this);
            MyViewHolder vh = new MyViewHolder(v,viewType);
            return vh;
        }
        else {
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_response_table_message, parent, false);
            v.setOnClickListener(this);
            MyViewHolder vh = new MyViewHolder(v,viewType);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        String hourStr= String.valueOf(hour);
        int minutes=currentTime.get(Calendar.MINUTE);
        String minutesStr=String.valueOf(minutes);
        String time= hourStr + ":"+ minutesStr;
        if(flag==0) {
            Message message = messages.get(position);
            holder.sendMesTextView.setText(message.getText());
            holder.timeTexView.setText(time);
        }
        if(flag==1) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    holder.buttonsList.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            );
            holder.buttonsList.setLayoutManager(layoutManager);
            final buttonsMessageAdapter buttonsMessageAdapter = new buttonsMessageAdapter(buttons);
            holder.buttonsList.setAdapter(buttonsMessageAdapter);
            holder.timeTexView.setText(time);
            Log.d("test","vao 1");
            flag=0;
        }
        if(flag==2){
            holder.tableLayout.removeAllViews();
            Message message = messages.get(position);
            Log.d("test",String.valueOf(position));
            JSONArray table2=message.getTable();
            for(int i=0;i<table2.length();i++){
                TableRow tbrow = new TableRow(holder.tableLayout.getContext());
                try {
                    JSONArray rowItem= table2.getJSONArray(i);
                    for(int j=0;j<rowItem.length();j++){
                        TextView item = new TextView(holder.tableLayout.getContext());
                        String textItem=rowItem.getString(j);
                        item.setText(textItem);
                        item.setGravity(Gravity.CENTER);
                        item.setTextColor(Color.BLACK);
                        item.setSingleLine(false);
                        item.setPadding(10,10,10,10);
                        if(i==0){
                            item.setBackgroundResource(R.drawable.item_boder_header);
                        }else{
                            item.setBackgroundResource(R.drawable.item_boder);
                        }
                        tbrow.addView(item);
                    }
                    holder.tableLayout.addView(tbrow);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            holder.timeTexView.setText(time);
            flag=0;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView sendMesTextView;
        public RecyclerView buttonsList;
        public TextView timeTexView;
        public TableLayout tableLayout;
        public MyViewHolder(View v,int viewType ) {
            super(v);
            if(viewType==0){
                sendMesTextView = v.findViewById(R.id.text_gchat_message_me);
                timeTexView=v.findViewById(R.id.text_gchat_timestamp_me);
            }
            else if(viewType==1){
                sendMesTextView = v.findViewById(R.id.text_gchat_message_bot);
                timeTexView=v.findViewById(R.id.text_gchat_timestamp_bot);
            }
            else if(viewType==2){
                buttonsList=v.findViewById(R.id.button_list);
                timeTexView=v.findViewById(R.id.text_gchat_timestamp_buttons);
            }
            else{
                tableLayout=v.findViewById(R.id.layout_gchat_container_table);
                timeTexView=v.findViewById(R.id.text_gchat_timestamp_table);
            }
        }
    }
}
