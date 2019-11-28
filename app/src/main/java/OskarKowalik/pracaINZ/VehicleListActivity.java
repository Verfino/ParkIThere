package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemClickListener;

public class VehicleListActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    List<Map<String, String>> vehicles = new ArrayList<Map<String, String>>();

    String baseURL = "http://192.168.1.14:45455/api/ParkingLot/GetVehiclesByUser?id=";
    //dopisać URL
    String deleteURL = "http://192.168.1.14:45455/api/ParkingLot/DelVehicle?delVehicle=";
    String updateURL;
    String addURL;
    String preURL;

    ListView vehicle;
    Button add;
    Button modify;
    Button delete;

    String selectedItem = "0";
    String userID = null;
    String vehicleID = "brak";
    String regNum;

    String decision;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);
        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);

        add = findViewById(R.id.add_car);
        modify = findViewById(R.id.modify_car);
        delete = findViewById(R.id.delete_car);
        vehicle = (ListView)findViewById(R.id.vehicle_list);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        preURL = baseURL + userID;


        jsonParseGet(this);

        vehicle.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedItem = position;
                selectedItem = vehicles.get(position).get("ID");
                regNum= vehicles.get(position).get("Second");
                vehicleID = selectedItem;
                //Toast.makeText(VehicleListActivity.this, "Position: " + position + " id :" + selectedItem, Toast.LENGTH_LONG).show();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                decision = "add";
                Intent intent = new Intent(VehicleListActivity.this, CarSettingsActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("decision", decision);
                intent.putExtra("vehicleID", "brak");
                startActivity(intent);

            }
        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decision = "modify";
                //find selected item
                if(selectedItem != "0"){
                    preURL = updateURL + regNum;
                    //Toast.makeText(VehicleListActivity.this, "URL: " + preURL, Toast.LENGTH_LONG).show();
                    //TODO /drugi put extra z id pojazdu
                    Intent intent = new Intent(VehicleListActivity.this, CarSettingsActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("vehicleID", regNum);
                    intent.putExtra("decision", decision);
                    startActivity(intent);
                }
                else
                    Toast.makeText(VehicleListActivity.this, "Wybierz pojazd", Toast.LENGTH_LONG).show();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //find selected item
                if(selectedItem !="0"){
                    preURL = deleteURL + selectedItem;
                        //preURL = deleteURL;
                    jsonParseDelete(VehicleListActivity.this, selectedItem);

                }
                else
                    Toast.makeText(VehicleListActivity.this, "Wybierz pojazd", Toast.LENGTH_LONG).show();

            }
        });

    }

    private final void jsonParseGet(final VehicleListActivity context){
        vehicles.clear();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                preURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        try {


                            for(int i=0; i < response.length(); i++){
                                JSONObject item = response.getJSONObject(i);
                                Map<String, String> jakchcesz = new HashMap<String, String>(3);
                                jakchcesz.put("First",item.getString("brand") + " | " + item.getString("model"));
                                jakchcesz.put("Second", item.getString("registrationNumber"));
                                jakchcesz.put("ID", item.getString("vehicleID"));
                                vehicles.add(jakchcesz);
                            }

                            SimpleAdapter adapter = new SimpleAdapter(context, vehicles,
                                    android.R.layout.simple_list_item_2,
                                    new String[] {"First", "Second" },
                                    new int[] {android.R.id.text1, android.R.id.text2 }){

                                public View getView(int position, View convertView, ViewGroup parent) {

                                    View view = super.getView(position, convertView, parent);
                                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                                    text1.setTextColor(Color.parseColor("#ffcc00"));
                                    text1.setBackgroundColor(Color.parseColor("#707070"));
                                    text2.setBackgroundColor(Color.parseColor("#707070"));
                                    text1.setTextSize(23);
                                    text2.setTextSize(18);
                                    return view;
                                };

                            };

                            vehicle = (ListView)findViewById(R.id.vehicle_list);
                            vehicle.setAdapter(adapter);

                        } catch (Exception e) {
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


    private final void jsonParseDelete(final VehicleListActivity context, String selectedItem){

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.DELETE,
                preURL,
                null,

        new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                            Toast.makeText(VehicleListActivity.this, "Usunięto pojazd", Toast.LENGTH_LONG).show();
                           // jsonParseGet(VehicleListActivity.this);
                        preURL = baseURL + userID;
                        jsonParseGet(VehicleListActivity.this);

                        }

                    },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response Error", error.toString());

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
