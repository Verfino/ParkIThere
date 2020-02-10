package OskarKowalik.pracaINZ;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class loginActivity extends AppCompatActivity {

    private Button button_accept;
    private static final String TAG = "MyActivity";
    private String email;
    private String name;
    private String password;
    private String api_password;
    //private RequestQueue requestQueue = Volley.newRequestQueue(mContext);
    private RequestQueue requestQueue;
    String userID = "0";
    String userType;

    EditText email_et;
    EditText password_et;

    String baseURL = "http://192.168.1.14:45455/api/ParkingLot/GetUsersByMail?mail=";
    String URL = "http://192.168.1.14:45455/api/ParkingLot/GetUsersByMail?mail=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_my);
        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);


        email_et = (EditText) findViewById(R.id.username);
        password_et = (EditText) findViewById(R.id.password);
        button_accept = (Button) findViewById(R.id.button_accept_login);

        button_accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                password = password_et.getText().toString();
                email = email_et.getText().toString();
                baseURL = URL + email;
                api_password = null;
                jsonParse();



            }
        });
    }
    public void openActivityAfterLogin() {

        Intent intent = new Intent(this, AfterLoginActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("userType", userType);
        intent.putExtra("reservation", "no");
        startActivity(intent);
    }

    private final void jsonParse(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                baseURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        try {
                            JSONObject user = response.getJSONObject(0);
                            api_password = user.getString("password");
                            name = user.getString("firstName");
                            userID = user.getString("userID");
                            userType = user.getString("userType");
                            //Toast.makeText(loginActivity.this, "json " + api_password,
                            // Toast.LENGTH_LONG).show();
                            if(checkLoginData()) {
                                Toast.makeText(loginActivity.this, "Witaj " + name,
                                        Toast.LENGTH_LONG).show();
                                openActivityAfterLogin();
                            }
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
    private boolean checkLoginData(){
        if( password.equals(api_password))
            return true;
        else
        {
            Toast.makeText(loginActivity.this, "Wprowadzono nieprawidłowe dane logowania. Sprawdź email oraz hasło ",
                    Toast.LENGTH_LONG).show();
            return false;
        }

    }
}
