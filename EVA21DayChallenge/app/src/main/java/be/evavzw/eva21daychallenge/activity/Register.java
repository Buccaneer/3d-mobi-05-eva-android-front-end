package be.evavzw.eva21daychallenge.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import be.evavzw.eva21daychallenge.R;
import be.evavzw.eva21daychallenge.activity.base.RESTfulActivity;
import be.evavzw.eva21daychallenge.activity.profile_setup.ProfileSetup;
import be.evavzw.eva21daychallenge.exceptions.RegisterFailedException;
import be.evavzw.eva21daychallenge.security.UserManager;
import butterknife.Bind;
import butterknife.ButterKnife;

public class Register extends RESTfulActivity {

    @Bind(R.id.email)
    EditText emailEditText;
    @Bind(R.id.password)
    EditText passwordEditText;
    @Bind(R.id.confirmPassword)
    EditText confirmPasswordEditText;
    @Bind(R.id.register)
    Button register;
    @Bind(R.id.eva_logo)
    ImageView evaLogo;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentResId(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        // Set button background color
        register.getBackground().setColorFilter(Color.parseColor("#afc137"), PorterDuff.Mode.MULTIPLY);

        // Set size for EVA logo
        int newHeight = getResources().getDisplayMetrics().heightPixels / 6;
        int orgWidth = evaLogo.getDrawable().getIntrinsicWidth();
        int orgHeight = evaLogo.getDrawable().getIntrinsicHeight();
        double newWidth = Math.floor((orgWidth * newHeight) / orgHeight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) newWidth, newHeight);
        evaLogo.setLayoutParams(params);
        evaLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Get instance of user manager
        userManager = UserManager.getInstance(this);

        // TODO: Replace this with a Butterknife onclick method
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    /**
     * Starts the register service once the user clicks the button
     */
    private void registerUser() {
        //Get string values inside the EditTexts
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        RegisterTask rt = new RegisterTask();
        rt.execute(email, password, confirmPassword);
    }

    class RegisterTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            setRefresh(true);
        }

        @Override
        protected Boolean doInBackground(String... objects) {
            try {
                userManager.register(objects[0], objects[1], objects[2]);
                return true;
            } catch (RegisterFailedException rex) {
                Map<String, List<String>> errors = rex.getMessages();
                if (errors.containsKey("email")) {
                    StringBuilder errorDetails = new StringBuilder();
                    for (String error : errors.get("email")) {
                        if (errorDetails.length() == 0) {
                            errorDetails.append(error);
                        } else {
                            errorDetails.append("\n").append(error);
                        }
                    }
                    setEmailError(errorDetails.toString());
                }
                if (errors.containsKey("password")) {
                    StringBuilder errorDetails = new StringBuilder();
                    for (String error : errors.get("password")) {
                        if (errorDetails.length() == 0) {
                            errorDetails.append(error);
                        } else {
                            errorDetails.append("\n").append(error);
                        }
                    }
                    setPasswordError(errorDetails.toString());
                }
                if (errors.containsKey("confirmPassword")) {
                    StringBuilder errorDetails = new StringBuilder();
                    for (String error : errors.get("confirmPassword")) {
                        if (errorDetails.length() == 0) {
                            errorDetails.append(error);
                        } else {
                            errorDetails.append("\n").append(error);
                        }
                    }
                    setConfirmPasswordError(errorDetails.toString());

                }
                return false;
            } catch (final Exception ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            setRefresh(false);
            if (succeed) {
                Intent intent = new Intent(getApplicationContext(), ProfileSetup.class);
                Register.this.finish();
                intent.putExtra("CALLED_FROM", "register");
                startActivity(intent);
            }
        }
    }

    private void setPasswordError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                passwordEditText.setError(error);
            }
        });
    }

    private void setConfirmPasswordError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                confirmPasswordEditText.setError(error);
            }
        });
    }

    private void setEmailError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emailEditText.setError(error);
            }
        });
    }

    /**
     * Sets the ProgressBar visible or invisible, is called in the AsyncTasks at <code>onPreExecute()</code> or <code>onPostExecute()</code>
     *
     * @param toggle
     */
    private void setRefresh(final boolean toggle) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleProgressBar(toggle);
            }
        });
    }
}
