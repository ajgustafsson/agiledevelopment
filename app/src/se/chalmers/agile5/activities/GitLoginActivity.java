package se.chalmers.agile5.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

        if (GitDataHandler.isUserLoggedIn()) {
            //user already logged in
            ChangeLoginDialogFragment dialog = new ChangeLoginDialogFragment();
            dialog.show(getFragmentManager(), "Login as other user?");
            //TODO configure back button of phone such that it means going back (negative button)
        }

        // Set up the login form.
        //gitUserName = getIntent().getStringExtra(EXTRA_EMAIL);
        userNameView = (EditText) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);

        SharedPreferences settings = getSharedPreferences(MyActivity.GIT_PREFS, 0);
        String userName = settings.getString("latestUserName", null);
        if (userName != null) {
            ((TextView)(findViewById(R.id.loginInfoView))).setText("Latest user was "+userName+ ". Please" +
                    " type in your password or login to another account.");
            userNameView.setText(userName);
            passwordView.requestFocus();
        } else {
            ((TextView)(findViewById(R.id.loginInfoView))).setText("Please login to GitHub below.");
            userNameView.setText(gitUserName);
        }

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

    /**
     * Dialog asking the user if he wants to log in to another account or abort
     */
    public class ChangeLoginDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_already_logged_in)
                    .setPositiveButton(R.string.dialog_login_as_other, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public void showTokenInputDialog(View view){
        (new OAuthTokenDialogFragment()).show(getFragmentManager(), "Input OAuth token");
    }

    public class OAuthTokenDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Input your OAuth token for GitHub");
            final EditText tokenInputView = new EditText(getActivity());
            builder.setView(tokenInputView);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String tokenString = tokenInputView.getText().toString();
                    //now write to preference file
                    SharedPreferences.Editor editor = getSharedPreferences(MyActivity.GIT_PREFS, 0).edit();
                    editor.putString("latestUserToken", tokenString);
                    editor.commit();

                    System.err.println("DBUG token input is: "+ tokenString);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            return builder.create();
        }
    }
}
