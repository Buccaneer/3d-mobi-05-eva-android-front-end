package evavzw.be.eva21daychallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignIn extends AppCompatActivity {

    @Bind(R.id.email)
    EditText emailEditText;

    @Bind(R.id.password)
    EditText passwordEditText;

    @Bind(R.id.signIn)
    Button signIn;

    private final String API_URL = "http://evavzwrest.azurewebsites.net/Token/";

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
                String grant_type = "password";

                Ion.with(getApplicationContext())
                        .load(API_URL)
                        .setBodyParameter("grant_type", grant_type)
                        .setBodyParameter("username", username)
                        .setBodyParameter("password", password)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                try {
                                    Log.i("jsonString", result);
                                    JSONObject json = new JSONObject(result);
                                    String json_result = json.getString("result");

                                    if(json_result.equalsIgnoreCase("ok")){
                                        Log.i("log", "Login success");
                                    }else{
                                        String error = json.getString("error");
                                        Log.i("log", "Login error: " + error);
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
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
