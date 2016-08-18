package mafia.adamzimny.mafia.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import mafia.adamzimny.mafia.util.AppVariable;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.AuthService;
import mafia.adamzimny.mafia.model.json.AuthenticationRequest;
import mafia.adamzimny.mafia.model.json.AuthenticationResponse;
import net.danlew.android.joda.JodaTimeAndroid;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity/* implements LoaderCallbacks<Cursor>*/ {

    private static final int REQUEST_READ_CONTACTS = 0;

 //   private UserLoginTask mAuthTask = null;

    // UI references.

    @BindView(R.id.username_field)
    AutoCompleteTextView usernameField;

    @BindView(R.id.password_field)
    EditText passwordField;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.login_form)
    View mLoginFormView;

    @BindView(R.id.button_login)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        JodaTimeAndroid.init(this);
        ButterKnife.bind(this);
        // Set up the login form.
        //    populateAutoComplete();

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024)
                .build();

         ImageLoader.getInstance().init(config);
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.password_field || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
       /* if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        usernameField.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwordField.setError(getString(R.string.error_invalid_password));
            focusView = passwordField;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            usernameField.setError(getString(R.string.error_field_required));
            focusView = usernameField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
         //   mAuthTask = new UserLoginTask(email, password);
         //   mAuthTask.execute((Void) null);
            Log.d("LoginActivity", "doInBackground");
            final AuthService authService = (AuthService) RetrofitBuilder.getService(AuthService.class);
            Call<AuthenticationResponse> authCall;

            authCall = authService.authenticate(new AuthenticationRequest(username, password));
            authCall.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    Log.d("LoginActivity", "onResponse login");
                    if (response.code() == 200) {
                        Log.d("LoginActivity", "onResponse login 200");
                        AppVariable.token = response.body().getToken();
                        AppVariable.loggedUser = response.body().getUser();
                        Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();

                        Intent i = new Intent(LoginActivity.this,TargetActivity.class);
                        LoginActivity.this.startActivity(i);
                        finish();
                    } else {
                        AppVariable.token = "";
                        AppVariable.loggedUser = null;
                        passwordField.setError(getString(R.string.error_incorrect_password));
                        passwordField.requestFocus();
                        showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Log.d("LoginActivity", "onFailure login " + t);
                    AppVariable.token = "";
                    AppVariable.loggedUser = null;
                    passwordField.setError(getString(R.string.error_incorrect_password));
                    passwordField.requestFocus();
                    showProgress(false);
                }
            });
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
/*

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String mPassword;
        Call<AuthenticationResponse> authCall;

        UserLoginTask(String username, String password) {
            this.username = username;
            mPassword = password;
            Log.d("LoginActivity", "login task created");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("LoginActivity", "doInBackground");
            final AuthService authService = (AuthService) RetrofitBuilder.getService(AuthService.class);
            final boolean[] success = new boolean[1];
            authCall = authService.authenticate(new AuthenticationRequest(username, mPassword));
            authCall.enqueue(new Callback<AuthenticationResponse>() {
                @Override
                public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                    Log.d("LoginActivity", "onResponse login");
                    if (response.code() == 200) {
                        Log.d("LoginActivity", "onResponse login 200");
                        AppVariable.token = response.body().getToken();
                        AppVariable.loggedUser = response.body().getUser();
                        success[0] = true;
                    }
                }

                @Override
                public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                    Log.d("LoginActivity", "onFailure login " + t);
                    AppVariable.token = "";
                    success[0] = false;
                }
            });
            Log.d("LoginActivity", "return success = " + success[0]);
            return success[0];
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            Log.d("LoginActivity", "onPostExecute");
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Log.d("LoginActivity", "onPostExecute success");
                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();

                //TODO Launch GPS activity
            } else {
                Log.d("LoginActivity", "onPostExecute fail");
                passwordField.setError(getString(R.string.error_incorrect_password));
                passwordField.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class LoginLocationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }
    }*/
}

