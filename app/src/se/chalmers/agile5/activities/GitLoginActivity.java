package se.chalmers.agile5.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import se.chalmers.agile5.R;
import se.chalmers.agile5.entities.GitDataHandler;

import java.io.IOException;

/**
 * Activity enabling the user to login to github.com
 */
public class GitLoginActivity extends BaseActivity {

/**
 * Keep track of the login task to ensure we can cancel it if requested.
 */
private UserLoginTask mAuthTask = null;

// Values for password and password at the time of the login attempt.
private String gitUserName;
private String gitPassword;

// UI references.
private EditText userNameView;
private EditText passwordView;
private View mLoginFormView;
private View mLoginStatusView;
private TextView mLoginStatusMessageView;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.git_login_activity);

        // Set up the login form.
        //gitUserName = getIntent().getStringExtra(EXTRA_EMAIL);
        userNameView = (EditText) findViewById(R.id.email);
        userNameView.setText(gitUserName);

        passwordView = (EditText) findViewById(R.id.password);
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

        // Reset errors.
        userNameView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        gitUserName = userNameView.getText().toString();
        gitPassword = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(gitPassword)) {
        passwordView.setError(getString(R.string.error_field_required));
        focusView = passwordView;
        cancel = true;
        } else if (gitPassword.length() < 7) {
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
        mAuthTask.execute((Void) null);
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

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Void... params) {
        final GitHubClient gitClient = doGitLogin(gitUserName, gitPassword);

        //saving GitHubClient
        if(gitClient != null){
            GitDataHandler.setGitHubClient(gitClient);
            return true;
        }
        return false;
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
            passwordView.setError(getString(R.string.error_incorrect_password));
            passwordView.requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mAuthTask = null;
        showProgress(false);
    }

   private GitHubClient doGitLogin(final String userName,final String password){
        final GitHubClient client = new GitHubClient();
        client.setCredentials(userName, password);

        final UserService userService = new UserService(client);
       try {
           //login seems ok
           userService.getUser().getName();
           return client;

       } catch (IOException e) {
           //login to github failed
           e.printStackTrace();
           return null;
       }
    }
}
}
