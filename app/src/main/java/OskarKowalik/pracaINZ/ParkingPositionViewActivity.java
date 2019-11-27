package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ParkingPositionViewActivity extends AppCompatActivity {

    String baseURL = "http://192.168.1.14:45455/api/ParkingLot/GetParkingLotByID?id=";
    String mainURL;
    String parkingID = null;

    TextView  pName;
    TextView pAddress;
    TextView pContact;

    private RequestQueue requestQueue;

    Button parkIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_position_view);
        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);

        Intent intent = getIntent();
        parkingID = intent.getStringExtra("parkingID");

        parkIT = findViewById(R.id.button_parkIT);
        pName = findViewById(R.id.parking_name);
        pAddress = findViewById(R.id.parking_address);
        pContact = findViewById(R.id.parking_contact);
        //pContact.setMovementMethod(LinkMovementMethod.getInstance());

        mainURL = baseURL + parkingID;
        JSONparse();

        parkIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParkingPositionViewActivity.this, ParkingLotViewActivity.class);
                startActivity(intent);
                Toast.makeText(ParkingPositionViewActivity.this, "Parkuj !",
                 Toast.LENGTH_LONG).show();
            }
        });
    }

    private final void JSONparse(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mainURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        try {

                            JSONObject parking = response.getJSONObject(0);
                            pName.setText(parking.getString("name"));
                            pAddress.setText(parking.getString("city"));
                            pContact.setText(parking.getString("site") + "\nWolne miejsca: " + parking.getString("freeSpots"));

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    };

}
