package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button_login;
    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_login = (Button) findViewById(R.id.button_login);
        button_register = (Button) findViewById((R.id.button_register));

        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivityLogin();
            }
        });

        button_register.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityRegister();
            }
        }));
    }
    public void openActivityLogin() {
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }
    public void openActivityRegister() {
        Intent intent = new Intent(this, RegisterLoginActivity.class);
        startActivity(intent);
    }
}

