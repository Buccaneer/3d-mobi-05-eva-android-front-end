package evavzw.be.eva21daychallenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity {

    //Maybe we can define our API URL's in the values/strings.xml, is this a good practice or not?
    private final String API_URL_REGISTER = "http://evavzwrest.azurewebsites.net/api/Account/Register/";
    private final String API_URL_LOGIN = "http://evavzwrest.azurewebsites.net/Token";
    @Bind(R.id.email)
    EditText emailEditText;
    @Bind(R.id.password)
    EditText passwordEditText;
    @Bind(R.id.confirmPassword)
    EditText confirmPasswordEditText;
    @Bind(R.id.register)
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        //Get string values inside the EditTexts
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        //Set up Json body
        final JsonObject json = new JsonObject();
        json.addProperty("Email", email);
        json.addProperty("Password", password);
        json.addProperty("ConfirmPassword", confirmPassword);

        //Start Ion with our defined API Url that should be used
        //Set the body that we just created, return value will be a JsonObject
        //withResponse() will allow us to query the http header code to see if it was OK or an error
        Ion.with(getApplicationContext())
                .load(API_URL_REGISTER)
                .setJsonObjectBody(json)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception exception, Response<JsonObject> result) {
                        try {
                            //API Call returned http header 200 (= OK, no errors)
                            if (result.getHeaders().code() == 200) {
                                Log.i("success", "Register worked");

                                //Register worked, so let's try to log in our user directly without sending him to an extra activity
                                //Do we want to use email verification? Is this possible on Azure?
                                loginUser(email, password);
                            } else {
                                //TODO: make header error logging system which displays the error as a label
                                //Something went wrong
                                //If error code is 400, the same request should not be sent to the server again before making adjustments
                                Log.e("headerError", String.valueOf(result.getHeaders().code()));
                            }
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
    }

    private void loginUser(String email, String password) {
        Ion.with(getApplicationContext())
                .load(API_URL_LOGIN)
                .setBodyParameter("grant_type", "password")
                .setBodyParameter("username", email)
                .setBodyParameter("password", password)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        //Again, API call returns http header OK
                        if (result.getHeaders().code() == 200) {
                            Log.i("success", "Login after register worked");

                            //Save access token in sharedPreferences to make future authorized API calls
                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("evavzw.be.eva21daychallenge", Context.MODE_PRIVATE);
                            String access_token = result.getResult().get("access_token").toString();
                            preferences.edit().putString("access_token", access_token).apply();

                            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                            startActivity(intent);
                        } else {
                            //TODO: make header error logging system which displays the error as a label
                            //Something went wrong
                            //If error code is 400, the same request should not be sent to the server again before making adjustments
                            Log.e("headerError", String.valueOf(result.getHeaders().code()));
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
