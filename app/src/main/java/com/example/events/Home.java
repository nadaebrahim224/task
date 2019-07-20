package com.example.events;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    AppEventsLogger logger = AppEventsLogger.newLogger(this);
    ListView eventlist;
    Button btn_check_details;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_check_details=findViewById(R.id.btn_check_event_details);
        eventlist = findViewById(R.id.event_list);

        //********** Google calender events ****************
        //to update the app by google calender every 30 sec
        Thread th=new Thread()
        {
            @Override
            public void run()
            {
                while(!isInterrupted())
                {
                    try
                    {
                        Thread.sleep(30000); //30 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                check_google_events();
                            }
                        });
                    }

                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
      th.start();

        //********* check event details***********
        btn_check_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go=new Intent(Home.this,weather.class);
                go.putExtras(b);
                startActivity(go);
            }
        });


        //************** facebook events *******************
        AccessTokenTracker token_tracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {

                check_facebook_events(currentAccessToken);

            }
        };

    }

                  // ****************** Google request method ******************

    public void check_google_events()
    {
       String url="https://www.googleapis.com/calendar/v3/calendars/0lh483ognh2m67sp19ctlln9d0@group.calendar.google.com/events";
        JsonObjectRequest event_calender_object=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            JSONArray dtarray=response.getJSONArray("items");
                            ArrayList<event_data>event_list=new ArrayList<>();
                            for(int count=0;count<dtarray.length();count++)
                            {
                                JSONObject jsobject = dtarray.getJSONObject(count);
                                event_data e_data=new event_data();
                                e_data.setEvent_name(jsobject.getString("summary"));
                                JSONObject date_object=jsobject.getJSONObject("start");
                                e_data.setDate(date_object.getInt("dateTime"));
                                int date=e_data.getDate();
                                b=new Bundle();
                                b.putInt("forcats_date",date);
//                                count++;
//                                int next_date=e_data.getDate();
//                                arrange_events(event_list, date);

                                event_list.add(e_data);
                            }

                            event_adapter adapter=new event_adapter(Home.this,R.layout.event_row_design,event_list);
                            eventlist.setAdapter(adapter);
                        }

                        catch (JSONException e)
                        {
                            Toast.makeText(Home.this,"ERROR*************",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(Home.this,"ERROR*************",Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(Home.this).add(event_calender_object);
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
//    public void logViewContentEvent( String contentData) {
//        Bundle params = new Bundle();
////        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
//        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData);
////        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
////        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
//        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, params);
//    }

                  //************facebook events****************
    public void check_facebook_events (AccessToken accessToken)
    {
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/{event-id}",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        try {
                            String content=response.getJSONObject().getString("event-id");
                            ArrayList<event_data> list_data = new ArrayList<>();
                            event_data e_data=new event_data();
                            e_data.setEvent_name(content);

                            event_adapter e_adapter=new event_adapter(Home.this,R.layout.event_row_design,list_data);
                            eventlist.setAdapter(e_adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters=new Bundle();
        parameters.putString("fields","event-id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void arrange_events(ArrayList<event_data> array1, int event_date)
    {
        for (int count=0;count<array1.size();count++)
        {
//            int next_date=event_data{count++};
//           if(event_date<)
        }
    }

    public void check_weather(int forcast_date)
    {

    }


}

