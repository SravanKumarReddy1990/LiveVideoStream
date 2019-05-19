package com.bsmaps.chatting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.provider.Settings.Secure;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bsmaps.chatting.liveVideoBroadcaster.*;
import com.bsmaps.chatting.liveVideoPlayer.LiveVideoPlayerActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * PLEASE WRITE RTMP BASE URL of the your RTMP SERVER.
     */
    public static String RTMP_BASE_URL = "rtmp://18.216.202.144:1935/bstv/";
    public ListView listView;
    List<Ffriends> heroList;
    FndListAdapter adapter;
    private SharedPreferences pref;
    String android_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final TextView broadcast=(TextView)findViewById(R.id.broadcast);
        broadcast.setVisibility(View.GONE);
        TextView broadcastplay=(TextView)findViewById(R.id.broadcastplay);
        broadcastplay.setVisibility(View.GONE);
        android_id = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TextView deviceid=(TextView)findViewById(R.id.deviceid);
        deviceid.setText(android_id);
        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            alert.setMessage("Enter Your Channel Name");
            alert.setTitle("Channel Name");

            alert.setView(edittext);

            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    //Editable YouEditTextValue = edittext.getText();
                    //OR
                   try {
                       String YouEditTextValue = edittext.getText().toString();
                       if(!YouEditTextValue.isEmpty()) {
                           SharedPreferences.Editor editor = pref.edit();
                           editor.putString("logid", YouEditTextValue);        // Saving boolean - true/false
                           editor.commit();
                           OkHttpClient client = new OkHttpClient();

                           Log.d("Main Activity", "http://resumecollection.orgfree.com/webservices/registeruser.php?loginid=" + android_id.replace(" ", "") + "&names=aa&pass=asd&emailid=asas&mobileno=as");
                           Request request = new Request.Builder()
                                   .url("http://resumecollection.orgfree.com/webservices/registeruser.php?loginid=" + android_id.replace(" ", "") + "&names=" + YouEditTextValue.replace(" ", "+") + "&pass=asd&emailid=asas&mobileno=as")
                                   .get()
                                   .build();
                           Response response = client.newCall(request).execute();
                           JSONObject jsonObject = new JSONObject(response.body().string());
                           String msg = jsonObject.getString("msg");
                           if (msg.equalsIgnoreCase("Done")) {
                               broadcast.setVisibility(View.VISIBLE);
                               RTMP_BASE_URL = jsonObject.getString("url");
                               SharedPreferences.Editor editor1 = pref.edit();
                               editor1.putString("url", RTMP_BASE_URL);        // Saving boolean - true/false
                               editor1.commit();
                           }
                       }
                   }catch (Exception e){

                   }

                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });
            String iden=pref.getString("logid", null);
            if(iden==null) {
                alert.show();
                checkUrl();
            }else {
                broadcast.setVisibility(View.VISIBLE);
            }
            heroList = new ArrayList<>();
            listView = (ListView) findViewById(R.id.listView);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                            listView.getFooterViewsCount()) >= (adapter.getCount() - 1)) {
//                        if(processDialog==0) {
//                            processDialog=1;
//                            itemsLoad();
//
//                            listno1=listno1+1;
//                            adapter.notifyDataSetChanged();
//                            Log.i("Post Fragment", "Scroll Ends");
//                            processDialog=0;
//                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });


            //creating the adapter
            adapter = new FndListAdapter(MainActivity.this,getApplicationContext(), R.layout.channel_custom_list, heroList);

            //attaching adapter to the listview
            listView.setAdapter(adapter);


        }catch (Exception e){
            Log.e("Main Activity",e+"");
        }
        getMyFriends();
    }
    public void checkUrl(){
        try {
            OkHttpClient client = new OkHttpClient();

            Log.d("Main Activity", "http://resumecollection.orgfree.com/webservices/downloadchatpersons.php?loginid=" + android_id.replace(" ", "") + "");
            Request request = new Request.Builder()
                    .url("http://resumecollection.orgfree.com/webservices/downloadchatpersons.php?loginid=" + android_id.replace(" ", "") + "")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            String msg = jsonObject.getString("msg");
            if (msg.equalsIgnoreCase("Not Done")) {
                //broadcast.setVisibility(View.VISIBLE);
                RTMP_BASE_URL = "aksdjvnakjvnkajsnvka";
            }else{
                String url = jsonObject.getString("url");
                RTMP_BASE_URL = url;
            }
        }catch (Exception e){}
    }
    public void openPlayer(String channelid){
        Intent i = new Intent(MainActivity.this, LiveVideoPlayerActivity.class);
        i.putExtra("channelid",channelid);
        startActivity(i);
    }
    public void getMyFriends(){
        try {
            // String url = serverurl+"/rest/myFrindsList/"+sqlcontactmodel.getUserId()
            OkHttpClient client = new OkHttpClient();
//        MediaType JSON
//                = MediaType.parse("application/json; charset=utf-8");
//        JSONObject postJson=new JSONObject();
//        postJson.put("title",postname.getText().toString());
//        postJson.put("userid","Ram13");
//        postJson.put("itemurl",downloadurl);
//        postJson.put("description",postdescription.getText().toString());
//        RequestBody body = RequestBody.create(JSON, postJson.toString());
            Request request = new Request.Builder()
                    .url("http://resumecollection.orgfree.com/webservices/downloadtrack.php")
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
                JSONObject myFndList = new JSONObject(res);
                for (int i = 0; i < myFndList.length(); i++) {
                    try {
                        JSONObject myFndLists = myFndList.getJSONObject(i + "");
                        // String user_type = myFndLists.getString("user_type");
                        String user_id = myFndLists.getString("userid");
                        String user_name = myFndLists.getString("user_name");
                        heroList.add(new Ffriends(R.drawable.icon, user_name, user_id));
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){

                    }
                }


//            if(count==0){
//                itemsLoad();
//            }
//            count++;
        }catch(Exception e){
            Log.e("FndFragment",e+"");
        }

    }
    public void openVideoBroadcaster(View view) {
        Intent i = new Intent(this, LiveVideoBroadcasterActivity.class);
        i.putExtra("channelid",android_id);
        startActivity(i);
    }

    public void openVideoPlayer(View view) {
        Intent i = new Intent(this, LiveVideoPlayerActivity.class);
        startActivity(i);
    }
}
