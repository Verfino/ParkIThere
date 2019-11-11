package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    private Button button_accept;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_my);

        button_accept = (Button) findViewById(R.id.button_accept_login);
        button_accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivityLogin();
            }
        });
    }
    public void openActivityLogin() {
        Toast.makeText(loginActivity.this, "Welcome!",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, AfterLoginActivity.class);
        startActivity(intent);
    }
}
