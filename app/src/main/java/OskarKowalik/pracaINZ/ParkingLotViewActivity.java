package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLotViewActivity extends AppCompatActivity {


    TextView parkingName;
    private RequestQueue requestQueue;
    String updateURL = "http://192.168.1.14:45455/api/ParkingLot/CancelReservation";

    List<Map<String, String>> spaces = new ArrayList<Map<String, String>>();
    private List<Button> buttons;
    String spaceID;
    String status;
    String userID;
    String parkingID;
    String pName;
    Button cancel;


    String getURL;
    String baseURL = "http://192.168.1.14:45455/api/ParkingLot/GetSpacesListByParkingID?parkingID=";


    private static final int[] BUTTON_IDS = {
            R.id.buttonView1,
            R.id.buttonView3,
            R.id.buttonView4,
            R.id.buttonView6,
            R.id.buttonView7,
            R.id.buttonView9,
            R.id.buttonView10,
            R.id.buttonView12,
            R.id.buttonView13,
            R.id.buttonView15,
            R.id.buttonView16,
            R.id.buttonView18,
            R.id.buttonView19,
            R.id.buttonView21,
            R.id.buttonView22,
            R.id.buttonView24,
            R.id.buttonView25,
            R.id.buttonView27,
            R.id.buttonView28,
            R.id.buttonView30,
            R.id.buttonView31,
            R.id.buttonView33,


    };
    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_view);

        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);

        parkingName = (TextView)findViewById(R.id.parking_view_name);
        Intent intent = getIntent();

        userID = intent.getStringExtra("userID");
        parkingID = intent.getStringExtra("parkingID");
        pName = intent.getStringExtra("pName");
        //parkingName.setText(intent.getStringExtra("pName"));

        cancel = findViewById(R.id.buttonCancelReservation);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReservation();

            }
        });

        parkingName.setText(pName);

        getSpaces();



    }

    private final void reservation(){

        Intent intent = new Intent(ParkingLotViewActivity.this, PopUpActivity.class);
        intent.putExtra("spaceID", spaceID);
        intent.putExtra("status", status);
        intent.putExtra("userID", userID);
        startActivityForResult(intent, 0);



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == 0){
            getSpaces();

        }
    }

    private final void getSpaces(){
        spaces.clear();
        getURL = baseURL + parkingID;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                getURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        try {
                            for(int i=0; i < response.length(); i++) {
                                JSONObject space = response.getJSONObject(i);
                                Map<String, String> jakchcesz = new HashMap<String, String>(3);
                                jakchcesz.put("spaceID", space.getString("spaceID"));
                                jakchcesz.put("status", space.getString("status"));
                                jakchcesz.put("isDisabled", space.getString("isDisabled"));
                                spaces.add(jakchcesz);

                            }
                            int counter = 0;
                            for(final int id : BUTTON_IDS) {
                                final Button button = (Button) findViewById(id);
                                button.setText(spaces.get(counter).get("spaceID"));
                                button.setTextColor(Color.parseColor("#2A2724"));
                                //button.setTag(spaces.get(counter).get("status"));
                                button.setTag(R.string.TAGstatus, spaces.get(counter).get("status"));
                                button.setTag(R.string.TAGdisabled, spaces.get(counter).get("isDisabled"));


                                //reserved

                                if (button.getTag(R.string.TAGstatus).equals("1")) {
                                    button.setBackgroundColor(Color.parseColor("#FFCC00"));
                                    button.setBackgroundResource(R.drawable.space_reserved);
                                    button.setTextColor(Color.parseColor("#FFCC00"));
                                }


                                //busy
                                if (button.getTag(R.string.TAGstatus).equals("2")) {
                                    button.setBackgroundColor(Color.parseColor("#ff0000"));
                                    button.setBackgroundResource(R.drawable.space_occupied);
                                    button.setTextColor(Color.parseColor("#FF0000"));
                                }

                                if (button.getTag(R.string.TAGstatus).equals("3")) {
                                    button.setBackgroundColor(Color.TRANSPARENT);
                                    button.setTextColor((Color.TRANSPARENT));
                                }


                                if (button.getTag(R.string.TAGstatus).equals("0") && button.getTag(R.string.TAGdisabled).equals("1")){
                                    button.setBackgroundColor(Color.parseColor("#273cfe"));
                                    button.setBackgroundResource(R.drawable.space_disabled);
                                    button.setTextColor(Color.parseColor("#273cfe"));
                            }


                                counter++;
                            }

                            buttons = new ArrayList<Button>();

                            for(final int id : BUTTON_IDS){
                                final Button button = (Button)findViewById(id);
                                button.setOnClickListener(new View.OnClickListener() {
                                                              @Override
                                                              public void onClick(View v) {
                                                                  //updateURL = baseURL + id;
                                                                  //Toast.makeText(ParkingLotViewActivity.this, "Klik", Toast.LENGTH_LONG).show();
                                                                  spaceID = button.getText().toString();
                                                                  status = button.getTag(R.string.TAGstatus).toString();
                                                                  //Toast.makeText(ParkingLotViewActivity.this, spaceID, Toast.LENGTH_LONG).show();
                                                                  reservation();
                                                              }

                                                          }
                                );
                                buttons.add(button);
                            }

                        } catch (JSONException e) {
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
    }

    private final void cancelReservation(){


        JSONObject params = new JSONObject();
        try {
            params.put("userID", userID);


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
                        Toast.makeText(ParkingLotViewActivity.this, "Odwołano rezerwacje", Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response error", error.toString());
                        Toast.makeText(ParkingLotViewActivity.this, R.string.error, Toast.LENGTH_LONG).show();

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
