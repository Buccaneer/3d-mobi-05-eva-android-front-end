package evavzw.be.eva21daychallenge.activity;

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
import evavzw.be.eva21daychallenge.R;

public class SignIn extends AppCompatActivity {

    private final String API_URL = "http://evavzwrest.azurewebsites.net/Token";
    @Bind(R.id.email)
    EditText emailEditText;
    @Bind(R.id.password)
    EditText passwordEditText;
    @Bind(R.id.signIn)
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                Ion.with(getApplicationContext())
                        .load(API_URL)
                        .setBodyParameter("grant_type", "password")
                        .setBodyParameter("username", username)
                        .setBodyParameter("password", password)
                        .asJsonObject()
                        .withResponse()
                        .setCallback(new FutureCallback<Response<JsonObject>>() {
                            @Override
                            public void onCompleted(Exception e, Response<JsonObject> result) {
                                if (result.getHeaders().code() == 200) {
                                    Log.i("success", "Login after register worked");

                                    //Save access token in sharedPreferences to make future authorized API calls
                                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("evavzw.be.eva21daychallenge", Context.MODE_PRIVATE);
                                    String access_token = result.getResult().get("access_token").toString();
                                    Log.i("access_token", access_token);
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
