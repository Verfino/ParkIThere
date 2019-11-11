package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VehicleListActivity extends AppCompatActivity {

    ListView vehicle;
    //String vehicleList[][] = {{"BMW","DW8Y476"},{"KIA","DW090SW"}};
    String vehicleList[] = {"BMW", "KIA"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        vehicle = (ListView)findViewById(R.id.vehicle_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_vehicle_listview, R.id.textView, vehicleList);
        vehicle.setAdapter(arrayAdapter);
    }
}
