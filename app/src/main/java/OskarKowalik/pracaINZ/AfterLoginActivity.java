package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringJoiner;

public class AfterLoginActivity extends AppCompatActivity {

    private TextView textViewSearch;
    private TextView textViewCars;
    private TextView textViewPremium;
    public String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

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
        Toast.makeText(AfterLoginActivity.this, "Moduł obecnie niedostępny",
                Toast.LENGTH_LONG).show();
    }
}



