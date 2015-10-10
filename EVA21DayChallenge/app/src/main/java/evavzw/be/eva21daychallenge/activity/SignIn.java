package evavzw.be.eva21daychallenge.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.security.UserManager;

public class SignIn extends AppCompatActivity {

    private final String API_URL = "http://evavzwrest.azurewebsites.net/Token";
    @Bind(R.id.email)
    EditText emailEditText;
    @Bind(R.id.password)
    EditText passwordEditText;
    @Bind(R.id.signIn)
    Button signIn;
    private UserManager mOauthManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        mOauthManager = UserManager.getInstance(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                signIn(username, password);
            }
        });
    }

    void signIn(String email, String password) {
        AuthorizeTask authorizeTask = new AuthorizeTask();
        authorizeTask.execute(new String[]{email, password});
    }

    class AuthorizeTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... objects) {
            try {
                mOauthManager.login(objects[0], objects[1]);
                return true;
            } catch (IllegalArgumentException aex) {
              // Foutief aanmelden

                return false;
            } catch  (Exception ex) {
            // connectie mislukt

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            if(succeed){
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                SignIn.this.finish();
                startActivity(intent);
            }
        }
    }
}
