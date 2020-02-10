package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class PopUpActivity extends AppCompatActivity {

    String spaceID;
    String status;
    String userID;

    Button accept;
    Button cancel;
    String baseURL;
    String getURL;
    String updateURL = "http://192.168.1.14:45455/api/ParkingLot/UpParkingSpaceByUser";



    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        //Test formatu daty
        Date currentTime = Calendar.getInstance().getTime();
        System.out.println(currentTime);

        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.3));

        Intent intent = getIntent();
        spaceID = intent.getStringExtra("spaceID");
        status = intent.getStringExtra("status");
        userID = intent.getStringExtra("userID");

        accept = findViewById(R.id.accept_reservation);
        cancel = findViewById(R.id.cancel_reservation);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("0")) {
                    makeReservation(spaceID);
                    //finishActivity(0);


                }
                else
                    Toast.makeText(PopUpActivity.this, "Miejsce niedostępne", Toast.LENGTH_LONG).show();
                    //finishActivity(0);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //finishActivity(0);
                PopUpActivity.super.onBackPressed();
            }
        });



    }

    private final void makeReservation(String spaceID){
        final Date currentTime = Calendar.getInstance().getTime();

        JSONObject params = new JSONObject();
        try {
            params.put("status", "1");
            params.put("spaceID", spaceID);
            params.put("userID", userID);
            params.put("statusChangeTime", currentTime.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest arrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                updateURL,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());
                        Toast.makeText(PopUpActivity.this, "Dokonano rezerwacji", Toast.LENGTH_LONG).show();
                        System.out.println(currentTime);
                        setResult(0);
                        finish();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response error", error.toString());
                        Toast.makeText(PopUpActivity.this, R.string.error, Toast.LENGTH_LONG).show();

                    }
                }
        );

        arrayRequest.setRetryPolicy( new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(arrayRequest);
    }
}
