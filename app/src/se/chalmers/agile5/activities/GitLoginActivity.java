package se.chalmers.agile5.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.AgileGitHubClient;
import se.chalmers.agile5.entities.GitDataHandler;

/**
 * Activity enabling the user to login to github.com
 */
public class GitLoginActivity extends BaseActivity {

    private static final int GIT_MIN_PW_LENGTH = 7;
    private static final int GIT_OAUTH_TOKEN_LENGTH = 40;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // Values for password and password at the time of the login attempt.
    private String gitUserName;
    private String gitPassword;
    private String oAuthToken;

    // UI references.
    private EditText userNameView;
    private EditText passwordView;
    private EditText oAuthView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.git_login_activity);

        // Set up the login form.
        //gitUserName = getIntent().getStringExtra(EXTRA_EMAIL);
        userNameView = (EditText) findViewById(R.id.gitUserName);
        passwordView = (EditText) findViewById(R.id.gitPassword);
        oAuthView = (EditText) findViewById(R.id.gitOAuthToken);

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        oAuthView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showOAuthInfoToast();
                }
            }
        });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        boolean useToken = true;
        boolean cancel = false;
        View focusView = null;

        // Reset errors.
        userNameView.setError(null);
        passwordView.setError(null);

        oAuthToken = oAuthView.getText().toString();
        if(!TextUtils.isEmpty(oAuthToken)){
            if(oAuthToken.length() != GIT_OAUTH_TOKEN_LENGTH){
                oAuthView.setError(getString(R.string.error_token_length));
                focusView = oAuthView;
                cancel = true;
            }
        } else {
            useToken = false;
            // Store values at the time of the login attempt.
            gitUserName = userNameView.getText().toString();
            gitPassword = passwordView.getText().toString();
            // Check for a valid password.
            if (TextUtils.isEmpty(gitPassword)) {
                passwordView.setError(getString(R.string.error_field_required));
                focusView = passwordView;
                cancel = true;
            } else if (gitPassword.length() < GIT_MIN_PW_LENGTH) {
                passwordView.setError(getString(R.string.error_invalid_password));
                focusView = passwordView;
                cancel = true;
            }
            // Check for a valid email address.
            if (TextUtils.isEmpty(gitUserName)) {
                userNameView.setError(getString(R.string.error_field_required));
                focusView = userNameView;
                cancel = true;
            }
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mAuthTask = new UserLoginTask();
            mAuthTask.execute((Boolean) useToken);
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

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void showOAuthInfoToast() {
        Context context = getApplicationContext();
        CharSequence text = "Personal access tokens can be used to access Git over HTTPS without using " +
                "a password. A token has a length of " + GIT_OAUTH_TOKEN_LENGTH + ". See or create tokens" +
                "at github.com/settings/applications";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Boolean, Void, Boolean> {

        private boolean useTokenToLogin = true;

        @Override
        protected Boolean doInBackground(Boolean... params) {
            if(!params[0]){
                // parameter is false, try log in with username and pw
                useTokenToLogin = false;
                return doGitLogin(gitUserName, gitPassword);
            } else {
                // try logging in with token
                return doGitLogin(oAuthToken);
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //TODO pass on the context of the loginActivity to use as context in the intent
                Intent gitSelectIntent = new Intent(getBaseContext(), GitSelectActivity.class);
                startActivity(gitSelectIntent);
                finish();
            } else {
                if(useTokenToLogin){
                    oAuthView.setError(getString(R.string.error_incorrect_token));
                    oAuthView.requestFocus();
                } else {
                    passwordView.setError(getString(R.string.error_incorrect_password));
                    passwordView.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private boolean doGitLogin(final String token){
            final AgileGitHubClient client = new AgileGitHubClient();
            client.setOAuth2Token(token);
            GitDataHandler.setGitHubClient(client);
            return GitDataHandler.isUserLoggedIn();
        }

        private boolean doGitLogin(final String userName, final String password) {
            final AgileGitHubClient client = new AgileGitHubClient();
            client.setCredentials(userName, password);
            GitDataHandler.setGitHubClient(client);
            return GitDataHandler.isUserLoggedIn();
        }
    }
}
