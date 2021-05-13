package com.example.quang.chatbot.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.example.quang.chatbot.Adapter.MessageAdapter;
import com.example.quang.chatbot.CallApi.CustomJsonArrayRequest;
import com.example.quang.chatbot.Model.Buttons;
import com.example.quang.chatbot.Model.Message;
import com.example.quang.chatbot.Model.Tables;
import com.example.quang.chatbot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private ImageButton buttonSend;
    private RecyclerView.LayoutManager layoutManager;
    public ArrayList<Message> messages = new ArrayList<>();
    private TextView noRecordTextView;
    private String botResponse;
    private static Context mContext;
    private JSONObject result;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        buttonSend = findViewById(R.id.sendButton);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final MessageAdapter messageAdapter = new MessageAdapter(messages, this);
        System.out.print(messages.size());
        recyclerView.smoothScrollToPosition(messages.size());
        recyclerView.setAdapter(messageAdapter);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Message message = new Message(text, true,"text");
                messageAdapter.add(message);
                noRecordTextView = findViewById(R.id.noRecordTextView);
                noRecordTextView.setVisibility(View.GONE);
                if (text.length() > 0) {
                    editText.getText().clear();
                }
                String postUrl = "http://c304a2aed595.ngrok.io/webhooks/rest/webhook";
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);

                JSONObject postData = new JSONObject();
                try {
                    postData.put("sender", "sender7");
                    postData.put("message", text);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CustomJsonArrayRequest jsonArrayRequest = new CustomJsonArrayRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0;i<response.length();i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                if(jsonObject.has("text")) {
                                    String botMessage = jsonObject.getString("text");
                                    Message botResponse = new Message(botMessage, false, "text");
                                    messageAdapter.add(botResponse);
                                }
                                if (jsonObject.has("buttons")) {
                                    ArrayList<Buttons> buttons = new ArrayList<>();
                                    JSONArray jsonArrayButton = jsonObject.getJSONArray("buttons");
                                    for (int j = 0; j < jsonArrayButton.length(); j++) {
                                        JSONObject jsonButton = jsonArrayButton.getJSONObject(j);
                                        String title = jsonButton.getString("title");
                                        Buttons button = new Buttons(title);
                                        buttons.add(button);
                                    }
                                    Message botResponseButtons = new Message(" ", false, "button");
                                    messageAdapter.add(botResponseButtons);
                                    messageAdapter.addButtons(buttons);
                                }
                                if (jsonObject.has("custom")) {
                                    JSONObject jsonCustom=jsonObject.getJSONObject("custom");
                                    JSONObject jsonData = jsonCustom.getJSONObject("data");
                                    JSONArray table= jsonData.getJSONArray("table");
                                    Message botResponseButtons = new Message(" ", false, "table");
                                    botResponseButtons.setTable(table);
                                    messageAdapter.add(botResponseButtons);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("err",error.toString());
                    }
                });
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonArrayRequest);
            }

        });
    }

//    public JSONObject volleyPost(String text) {
//        String postUrl = "http://192.168.56.1:5005/webhooks/rest/webhook";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JSONObject postData = new JSONObject();
//        try {
//            postData.put("sender", "sender");
//            postData.put("message", text);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        CustomJsonArrayRequest jsonArrayRequest = new CustomJsonArrayRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                try {
//                    JSONObject jsonObject = response.getJSONObject(0);
//                    result = jsonObject;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("err", error.toString());
//            }
//        });
//        System.out.println(result);
//        requestQueue.add(jsonArrayRequest);
//        return result;
//    }
}