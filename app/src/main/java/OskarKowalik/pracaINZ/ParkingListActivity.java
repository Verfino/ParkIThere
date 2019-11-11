package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ParkingListActivity extends AppCompatActivity {

    ListView parking;
    //String vehicleList[][] = {{"BMW","DW8Y476"},{"KIA","DW090SW"}};
    String parkingList[] = {"Magnolia", "Bielany"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_list);

        parking = (ListView)findViewById(R.id.parking_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_parking_listview, R.id.textView, parkingList);
        parking.setAdapter(arrayAdapter);
    }
}
