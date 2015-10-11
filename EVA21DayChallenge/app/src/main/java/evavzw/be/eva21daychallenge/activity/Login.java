package evavzw.be.eva21daychallenge.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.google.android.gms.common.SignInButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import evavzw.be.eva21daychallenge.R;
import evavzw.be.eva21daychallenge.customComponent.LoginButton;
import evavzw.be.eva21daychallenge.customComponent.TwitterButton;
import evavzw.be.eva21daychallenge.security.UserManager;

public class Login extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "evavzw.be.eva21daychallenge.MESSAGE";
    @Bind(R.id.createAccount)
    Button createAccount;
    @Bind(R.id.signIn)
    Button signIn;
    @Bind(R.id.loginFacebookButton)
    LoginButton loginFacebook;
    @Bind(R.id.loginTwitterButton)
    TwitterButton loginTwitter;
    @Bind(R.id.loginGoogleButton)
    SignInButton loginGoogle;
    @Bind(R.id.progress_indicator)
    ProgressBar progressBar;
    @Bind(R.id.eva_logo)
    ImageView evaLogo;
    private UserManager mOAuthManager;


    @Override
    protected void onResume() {
        super.onResume();
        if (mOAuthManager.isTokenPresent() && mOAuthManager.isTokenValid()) {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mOAuthManager = UserManager.getInstance(this);

        signIn.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);
        createAccount.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);

        int newHeight = getResources().getDisplayMetrics().heightPixels / 6;
        int orgWidth = evaLogo.getDrawable().getIntrinsicWidth();
        int orgHeight = evaLogo.getDrawable().getIntrinsicHeight();

        //double check my math, this should be right, though
        double newWidth = Math.floor((orgWidth * newHeight) / orgHeight);

        //Use RelativeLayout.LayoutParams if your parent is a RelativeLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) newWidth, newHeight);
        evaLogo.setLayoutParams(params);
        evaLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);



        //Start a new activity when pressing Register
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Register.class);
                startActivity(intent);
            }
        });

        //Start a new activity when pressing Sign in
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignIn.class);
                startActivity(intent);
            }
        });

        loginFacebook.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {
                                               handleExternalLogin("Facebook");
                                           }
                                       }
        );

        loginGoogle.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {
                                              handleExternalLogin("Google");
                                          }
                }
        );

        loginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExternalLogin("Twitter");
            }
        });
    }

    private void handleExternalLogin(String service) {
        Intent intent = new Intent(getApplicationContext(), WebviewLogin.class);
        intent.putExtra(EXTRA_MESSAGE, service);
        startActivity(intent);
    }
}
