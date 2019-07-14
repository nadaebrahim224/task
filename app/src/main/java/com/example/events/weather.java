package com.example.events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class weather extends AppCompatActivity {

    TextView date,temp,temp_status,city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        date=findViewById(R.id.date);
        temp=findViewById(R.id.temp);
//        temp_status=findViewById(R.id.temp_status);
        city=findViewById(R.id.city);

        findweather();
    }

    public void findweather()
    {
        String URL="https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22";
        JsonObjectRequest json=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject jason_obj=new response.getJSONObject("main");
                    JSONArray jason_arr=new response.getJSONArray("weather");
                    JSONObject object=jason_arr.getJSONObject(0);
                    String tempreture=String.valueOf(jason_obj.getDouble("temp"));
                    String mycity=response.getString("name");
                    String description=object.getString("description");

                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat w_date=new SimpleDateFormat("yyyyy-mm-dd");
                    String formated_date=w_date.format(calendar.getTime());

                    double temp_int=Double.parseDouble(tempreture);
                    double centi=(temp_int -32)/1.8000;
                    centi=Math.round(centi);
                    int i=(int)centi;


                    date.setText(formated_date);
                    temp.setText(String.valueOf(i));
                    city.setText(mycity);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        Volley.newRequestQueue(weather.this).add(json);
    }
}
