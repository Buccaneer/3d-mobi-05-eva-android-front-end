package evavzw.be.eva21daychallenge.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.*;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.rest.RegisterFailedException;
import evavzw.be.eva21daychallenge.security.UserManager;

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
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userManager  = UserManager.getInstance(this);

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
         String email = emailEditText.getText().toString();
         String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        RegisterTask rt = new RegisterTask();
        rt.execute(email,password,confirmPassword);

    }

    class RegisterTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... objects) {
            try {
                userManager.register(objects[0], objects[1], objects[2]);
                return true;
            } catch (RegisterFailedException rex) {
                List<String> messages = rex.getMessages();
                for(String message : messages)

                Log.w("RegisterWarning", message);
                return false;
            } catch (IllegalArgumentException aex) {
                // Foutief
                Log.w("RegisterWarning", aex.getMessage());
                return false;
            } catch  (Exception ex) {
                // connectie mislukt
                Log.e("RegisterError", ex.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            if(succeed){
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                Register.this.finish();
                startActivity(intent);
            }
        }
    }


}
