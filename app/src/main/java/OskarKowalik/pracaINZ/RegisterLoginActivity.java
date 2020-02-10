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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;

public class RegisterLoginActivity extends AppCompatActivity {

    String ifExistsURL = "http://192.168.1.14:45455/api/ParkingLot/GetUsersByMail?mail=";
    String createUserURL = "";
    String URL = null;

    EditText email;
    EditText password;
    EditText password2;

    Button accept;
    Button cancel;

    String pass1;
    String pass2;
    String emailCheck;
    Boolean exists = true;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        final Context mContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);

        email = findViewById(R.id.user_email);
        password = findViewById(R.id.new_password);
        password2 = findViewById(R.id.new_password_again);
        accept = findViewById(R.id.button_accept);
        cancel = findViewById(R.id.button_back);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass1 = password.getText().toString();
                pass2 = password2.getText().toString();

                if(!pass1.equals(pass2))
                    Toast.makeText(RegisterLoginActivity.this, R.string.passNotEqualPass2,  Toast.LENGTH_LONG).show();

                else {
                    //Toast.makeText(RegisterLoginActivity.this, "Hasła są równe",  Toast.LENGTH_LONG).show();
                    URL = ifExistsURL + email.getText().toString();
                    if(isEmailValid(email.getText()) && checkPassLength())
                        jsonParse();
                    else
                        Toast.makeText(RegisterLoginActivity.this, "Wprowadzony email lub hasło są nieprawidłowe",  Toast.LENGTH_LONG).show();

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isEmailValid(CharSequence email) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    };

    private boolean checkPassLength(){
        if (pass1.length() < 8){
            Toast.makeText(RegisterLoginActivity.this, "Hasło jest za krótkie",  Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    };

    private void checkUserExists() {
        if(!exists)
            parseNewUser();
        else
            Toast.makeText(RegisterLoginActivity.this, R.string.userExists ,  Toast.LENGTH_LONG).show();
        //Toast.makeText(RegisterLoginActivity.this, "OnCLick" + email.getText().toString(),  Toast.LENGTH_LONG).show();
        System.out.println("OnCLick" + email.getText().toString());
    }

    private final void parseNewUser(){
        //parsowanie i otworzenie register activity 2
         Intent intent = new Intent(RegisterLoginActivity.this, activity_setting_login_contact.class);
         intent.putExtra("email", email.getText().toString());
         intent.putExtra("password", pass1);
         startActivity(intent);
    }


    private final void jsonParse(){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Rest Response", response.toString());
                        try {
                            if(response.isNull(0) ){
                                exists = false;
                            }
                            else {
                                JSONObject user = response.getJSONObject(0);
                                emailCheck = user.getString("email");
                                //Toast.makeText(loginActivity.this, "json " + api_password,
                                // Toast.LENGTH_LONG).show();
                                if(emailCheck.equals(email.getText().toString()))
                                    System.out.println("Check" + email.getText().toString());
                                exists = true;

                            }
                            checkUserExists();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            exists = false;
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
}
