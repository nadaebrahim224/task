package com.example.events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    AppEventsLogger logger = AppEventsLogger.newLogger(this);
    ListView eventlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        eventlist = findViewById(R.id.event_list);


        AccessTokenTracker token_tracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {

                graph_req_for_events(currentAccessToken);

            }
        };

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


    public void graph_req_for_events (AccessToken accessToken)
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
}

