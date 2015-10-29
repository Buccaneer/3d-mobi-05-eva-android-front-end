package evavzw.be.eva21daychallenge.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.activity.base.RESTfulActivity;
import evavzw.be.eva21daychallenge.security.UserManager;

public class SignIn extends RESTfulActivity {

    @Bind(R.id.email)
    EditText emailEditText;
    @Bind(R.id.password)
    EditText passwordEditText;
    @Bind(R.id.signIn)
    Button signIn;
    @Bind(R.id.eva_logo)
    ImageView evaLogo;
    private UserManager mOauthManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentResId(R.layout.activity_sign_in);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        signIn.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);

        int newHeight = getResources().getDisplayMetrics().heightPixels / 6;
        int orgWidth = evaLogo.getDrawable().getIntrinsicWidth();
        int orgHeight = evaLogo.getDrawable().getIntrinsicHeight();
        double newWidth = Math.floor((orgWidth * newHeight) / orgHeight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) newWidth, newHeight);
        evaLogo.setLayoutParams(params);
        evaLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);

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

    private void signIn(String email, String password) {
        AuthorizeTask authorizeTask = new AuthorizeTask();
        authorizeTask.execute(new String[]{email, password});
    }

    class AuthorizeTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            setRefresh(true);
        }

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
            setRefresh(false);
            if(succeed){
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                SignIn.this.finish();
                startActivity(intent);
            }
        }
    }

    private void setRefresh(final boolean toggle){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleProgressBar(toggle);
            }
        });
    }
}
