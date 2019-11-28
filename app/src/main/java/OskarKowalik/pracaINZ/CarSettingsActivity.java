package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class CarSettingsActivity extends AppCompatActivity {

    String userID = null;
    String vehicleID = null;
    String registerNumber;

    String updateURL = "http://192.168.1.14:45455/api/ParkingLot/UpVehicle";
    String addURL = "http://192.168.1.14:45455/api/ParkingLot/AddVehicle";
    String getURL = "http://192.168.1.14:45455/api/ParkingLot/GetVehiclesByRegNum?regNum=";
    String preURL = "";
    String URL;

    Button accept;
    Button cancel;

    EditText brand;
    EditText model;
    EditText regNum;
    EditText fuel;
    String exists;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_settings);

        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);

        //pobranie userID
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        vehicleID = intent.getStringExtra("vehicleID");
        exists = intent.getStringExtra("decision");


        accept = findViewById(R.id.button_accept);
        cancel = findViewById(R.id.button_back);

        brand = findViewById(R.id.vehicle_brand);
        model = findViewById(R.id.vehicle_model);
        regNum = findViewById(R.id.vehicle_number);
        fuel = findViewById(R.id.vehicle_fuel);

        if(exists.equals("modify")){
            URL = getURL + vehicleID;
            getVehicleParse();
        }


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exists.equals("modify")){
                    modifyParse();
                }
                else{
                    addParse();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open VehicleListActivity
                Intent intent = new Intent(CarSettingsActivity.this, VehicleListActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });


    }

    private final void modifyParse(){
        JSONObject params = new JSONObject();
        try {
            params.put("vehicleID", vehicleID);
            params.put("brand", brand.getText().toString());
            params.put("model", model.getText().toString());
            params.put("registrationNumber", regNum.getText().toString());
            params.put("fuelType", fuel.getText().toString());
            params.put("vehicleUserID", userID);

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
                        Toast.makeText(CarSettingsActivity.this, "Edytowano pojazd", Toast.LENGTH_LONG).show();
                        openCarList();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response error", error.toString());
                        Toast.makeText(CarSettingsActivity.this, R.string.error, Toast.LENGTH_LONG).show();

                    }
                }
        );

        arrayRequest.setRetryPolicy( new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(arrayRequest);

    }

    private final void addParse(){
        JSONObject params = new JSONObject();
        try {
            params.put("brand", brand.getText().toString());
            params.put("model", model.getText().toString());
            params.put("registrationNumber", regNum.getText().toString());
            params.put("fuelType", fuel.getText().toString());
            params.put("vehicleUserID", userID);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest arrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                addURL,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());
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
    private final void getVehicleParse(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        try {
                            JSONObject vehicle = response.getJSONObject(0);
                            brand.setText(vehicle.getString("brand"));
                            model.setText(vehicle.getString("model"));
                            regNum.setText(vehicle.getString("registrationNumber"));
                            fuel.setText(vehicle.getString("fuelType"));
                            vehicleID = vehicle.getString("vehicleID");
                            //Toast.makeText(loginActivity.this, "json " + api_password,
                            // Toast.LENGTH_LONG).show();

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

    private final void openCarList(){
        Intent intent = new Intent (CarSettingsActivity.this, VehicleListActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);

    }
}
