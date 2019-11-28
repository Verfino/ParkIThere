package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.StringJoiner;

public class AfterLoginActivity extends AppCompatActivity {

    private TextView textViewSearch;
    private TextView textViewCars;
    private TextView textViewPremium;
    public String userID;
    String userType;

    String URL = "http://192.168.1.14:45455/api/ParkingLot/GetUsersByMail?mail=";
    String updateURL = "http://192.168.1.14:45455/api/ParkingLot/UpUserType";

    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");

        textViewSearch = (TextView) findViewById(R.id.search_parking);
        textViewCars = (TextView) findViewById(R.id.manage_cars);
        textViewPremium = (TextView) findViewById(R.id.activate_premium);

        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openParkingListActivity();
            }
        });

        textViewCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageCarsActivity();
            }
        });

        textViewPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBuyPremiumActivity();
            }
        });
    }


    public void openParkingListActivity() {

        Intent intent = new Intent(this, ParkingListActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);

    }
    public void  openManageCarsActivity() {
        Intent intent = new Intent(this, VehicleListActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void openBuyPremiumActivity() {
        //Toast.makeText(AfterLoginActivity.this, "Moduł obecnie niedostępny",Toast.LENGTH_LONG).show();
        //TODO sciągnąc dane użytkownika
        if(userType.equals("0"))
            userType = "1";
        else
            userType = "0";
        modifyParse();
    }

    private final void modifyParse(){
        JSONObject params = new JSONObject();
        try {
            params.put("userID", userID);
            params.put("userType", userType);

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
                        Toast.makeText(AfterLoginActivity.this, "Ustawiono userType= " + userType, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response error", error.toString());


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



