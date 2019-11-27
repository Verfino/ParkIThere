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

import java.util.HashMap;
import java.util.Map;

public class activity_setting_login_contact extends AppCompatActivity {

    EditText name;
    EditText surname;
    EditText phone;

    String baseURL = "http://192.168.1.14:45455/api/ParkingLot/AddUser";
    String URL;
    String email;
    String password;
    String nameText;
    String surnameText;
    String phoneText;

    Button accept;
    Button cancel;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_login_contact);
        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        System.out.println(email + password);

        name = findViewById(R.id.person_name);
        surname = findViewById(R.id.person_surname);
        phone = findViewById(R.id.person_phone);
        accept = findViewById(R.id.button_accept);
        cancel = findViewById(R.id.button_back);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText = name.getText().toString();
                surnameText = surname.getText().toString();
                phoneText = phone.getText().toString();

                URL = baseURL;
                parseCreate();

                }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_setting_login_contact.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //tylko update
    }

    private final void parseCreate(){
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
            params.put("firstName", nameText);
            params.put("lastName", surnameText);
            params.put("phone", phoneText);
            params.put("seed", "-brak-");
            params.put("userType", "0");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest arrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());
                        Toast.makeText(activity_setting_login_contact.this,"Konto zostało utworzone", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(activity_setting_login_contact.this, loginActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response error", error.toString());
                        Toast.makeText(activity_setting_login_contact.this,"Nie udało się utworzyć konta", Toast.LENGTH_LONG).show();

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

