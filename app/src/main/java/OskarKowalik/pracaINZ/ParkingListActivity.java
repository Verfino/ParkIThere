package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.AdapterView.OnItemClickListener;

public class ParkingListActivity extends AppCompatActivity {

    private RequestQueue requestQueue;


    List<Map<String, String>> parkings = new ArrayList<Map<String, String>>();

    ListView parking;
    TextView desing;

    Button filterByDistance;
    Button filterByName;
    Button filterByCity;
    Button filter;

    EditText filterInput;

    Integer filterDecision = 0;
    Context context;

    String baseURL = "http://192.168.1.14:45455/api/ParkingLot/GetParkingList";
    String preURL;
    String urlAll = "http://192.168.1.14:45455/api/ParkingLot/GetParkingList";
    String urlDistance;
    String urlName = "http://192.168.1.14:45455/api/ParkingLot/GetParkingLotByName?name=";
    String urlCity = "http://192.168.1.14:45455/api/ParkingLot/GetParkingLotsByCity?cityName=";

    String selectedItem = "0";
    String parkingID;
    String userID;
    boolean clicked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list);
        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);
        preURL = baseURL;

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");


        filterByCity = findViewById(R.id.filter_by_city);
        filterByName = findViewById(R.id.filter_by_name);
        filterByDistance = findViewById(R.id.filter_by_distance);
        filter = findViewById(R.id.accept_filter);
        filterInput = findViewById(R.id.filter_name_input);
        desing = findViewById(R.id.design_list);

        jsonParse(this);

    }

    private final void jsonParse(final ParkingListActivity context){
        parkings.clear();
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
                                jakchcesz.put("First",item.getString("name") + " (" + item.getString("city") + ")" );
                                jakchcesz.put("Second","Odległość: --- " + "Wolne miejsca: " + item.getString("freeSpots"));
                                jakchcesz.put("ID", item.getString("parkingID"));
                                parkings.add(jakchcesz);

                            }

                            SimpleAdapter adapter = new SimpleAdapter(context, parkings,
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

                            parking = (ListView)findViewById(R.id.parking_list);
                            parking.setAdapter(adapter);

                            InitNewWindow();

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
               10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(arrayRequest);

    };
    private final void openActivity(){
        Intent intent = new Intent(ParkingListActivity.this, ParkingPositionViewActivity.class);
        intent.putExtra("parkingID", parkingID);
        startActivity(intent);

    }

    private void InitNewWindow() {
        parking.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selectedItem = position;
                selectedItem = parkings.get(position).get("ID");
                parkingID = selectedItem;
                openActivity();
                //Toast.makeText(VehicleListActivity.this, "Position: " + position + " id :" + selectedItem, Toast.LENGTH_LONG).show();
            }
        });


        filterByCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDecision = 3;
                baseURL = urlCity;
                filterInput.setEnabled(true);
                filterInput.setVisibility(View.VISIBLE);
                filter.setEnabled(true);
                filter.setVisibility(View.VISIBLE);
                clicked = true;

            }
        });
        filterByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDecision = 2;
                baseURL = urlName;
                filterInput.setEnabled(true);
                filterInput.setVisibility(View.VISIBLE);
                filter.setEnabled(true);
                filter.setVisibility(View.VISIBLE);
                clicked = true;
            }
        });
        filterByDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDecision = 1;
                baseURL = urlAll;
                filterInput.setEnabled(false);
                filterInput.setVisibility(View.INVISIBLE);
                filter.setEnabled(false);
                filter.setVisibility(View.INVISIBLE);
                clicked = true;
                preURL = urlAll;
                jsonParse(ParkingListActivity.this);

            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
                if (filterDecision != 1)
                    preURL = baseURL + filterInput.getText();
                else
                    preURL = urlAll;
                jsonParse(ParkingListActivity.this);
            }
        });
    }



}

