package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AfterLoginActivity extends AppCompatActivity {

    private TextView textViewSearch;
    private TextView textViewCars;
    private TextView textViewPremium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

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
        Toast.makeText(AfterLoginActivity.this, "Kliknięto search textview",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ParkingListActivity.class);
        startActivity(intent);

    }
    public void  openManageCarsActivity() {
        Toast.makeText(AfterLoginActivity.this, "Kliknięto manage cars textview",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, VehicleListActivity.class);
        startActivity(intent);
    }

    public void openBuyPremiumActivity() {
        Toast.makeText(AfterLoginActivity.this, "Kliknięto premium textview",
                Toast.LENGTH_LONG).show();
    }
}



