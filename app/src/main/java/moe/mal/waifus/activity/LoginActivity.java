package moe.mal.waifus.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.Status;

import butterknife.BindView;
import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AuthActivity {


    private static final int RC_SAVE = 1;

    @BindView(R.id.usernameField) EditText usernameField;
    @BindView(R.id.passwordField) EditText passwordField;
    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.signUpText) TextView signUpPrompt;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Ougi.getInstance().getUser().isLoggedIn()) {
            usernameField.setText(Ougi.getInstance().getUser().getUsername());
            passwordField.setText(Ougi.getInstance().getUser().getPassword());
        }

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginPressed();
                    }
                }
        );

        signUpPrompt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signUpPressed();
                    }
                }
        );
    }

    /**
     * Called when the save button is clicked.  Reads the entries in the email and password
     * fields and attempts to save a new Credential to the Credentials API.
     */
    private void saveCredentialClicked() {
        username = usernameField.getText().toString();
        password = passwordField.getText().toString();

        attemptLogin();
    }

    private void attemptLogin() {
        Call<User> call = Ougi.getInstance().getWaifuAPI()
                .getUserInfo(username, User.buildAuth(username, password));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                handleLoginResponse(response);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                handleLoginResponse(null);
            }
        });
    }

    private void handleLoginResponse(Response<User> response) {
        if ((response == null) || (response.code() != 200)) {
            onLoginFailed();
            return;
        }

        // Create a Credential with the user's email as the ID and storing the password.  We
        // could also add 'Name' and 'ProfilePictureURL' but that is outside the scope of this
        // minimal sample.
        final Credential credential = new Credential.Builder(username)
                .setPassword(password)
                .build();

        User user = response.body();
        user.setCredential(credential);
        Ougi.getInstance().setUser(user);

        // NOTE: this method unconditionally saves the Credential built, even if all the fields
        // are blank or it is invalid in some other way.  In a real application you should contact
        // your app's back end and determine that the credential is valid before saving it to the
        // Credentials backend.
        Auth.CredentialsApi.save(mCredentialsApiClient, credential).setResultCallback(
                new ResolvingResultCallbacks<Status>(this, RC_SAVE) {
                    @Override
                    public void onSuccess(Status status) {
                    }

                    @Override
                    public void onUnresolvableFailure(Status status) {
                    }
                });

        showScreen(SadActivity.class);
    }

    public boolean validate() {
        boolean valid = true;

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (username.isEmpty()) {
            usernameField.setError("enter a valid username address");
            valid = false;
        } else {
            usernameField.setError(null);
        }

        if (password.isEmpty()) {
            passwordField.setError("enter a valid password");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }


    public void onLoginFailed() {
        showToast("Login Failed");
    }

    public void loginPressed() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        saveCredentialClicked();
    }

    public void signUpPressed() {

    }

}
