package moe.mal.waifus.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.Status;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AuthActivity {

    private boolean status;

    private static final int RC_SAVE = 1;

    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        usernameField.setText(Ougi.getInstance().getUsername());
        passwordField.setText(Ougi.getInstance().getPassword());
    }

    /**
     * Called when the save button is clicked.  Reads the entries in the email and password
     * fields and attempts to save a new Credential to the Credentials API.
     */
    private void saveCredentialClicked() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (!(verifyCredentials(username, password))) {
            showToast("Those credentials were invalid.");
            return;
        }


        // Create a Credential with the user's email as the ID and storing the password.  We
        // could also add 'Name' and 'ProfilePictureURL' but that is outside the scope of this
        // minimal sample.
        final Credential credential = new Credential.Builder(username)
                .setPassword(password)
                .build();

        Ougi.getInstance().setCredential(credential);

        // NOTE: this method unconditionally saves the Credential built, even if all the fields
        // are blank or it is invalid in some other way.  In a real application you should contact
        // your app's back end and determine that the credential is valid before saving it to the
        // Credentials backend.
        Auth.CredentialsApi.save(mCredentialsApiClient, credential).setResultCallback(
                new ResolvingResultCallbacks<Status>(this, RC_SAVE) {
                    @Override
                    public void onSuccess(Status status) {
                        showToast("Credential Saved");
                    }

                    @Override
                    public void onUnresolvableFailure(Status status) {
                        showToast("Credential Save Failed");
                    }
                });

        showScreen(SadActivity.class);
    }

    private boolean verifyCredentials(String username, String password) {

        Call<User> call = Ougi.getInstance().getWaifuAPI()
                .getUserInfo(username, Ougi.buildAuth(username, password));

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                handleLoginResponse(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                handleLoginResponse(null);
            }
        });

        return status;
    }

    private void handleLoginResponse(User user) {
        status = (user != null);
    }

    public void loginPressed(View v) {
        saveCredentialClicked();
    }

    public void signUpPressed(View v) {

    }

}
