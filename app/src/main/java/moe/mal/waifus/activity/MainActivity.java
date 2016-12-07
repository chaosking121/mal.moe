package moe.mal.waifus.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.User;
import moe.mal.waifus.network.WaifuAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AuthActivity {

    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final int RC_HINT = 2;
    private static final int RC_READ = 3;

    private boolean mIsResolving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instance state
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
        }

        Ougi.getInstance().setUser(new User());

        Ougi.getInstance().setWaifuAPI(new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url_base))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WaifuAPI.class));
    }

    @Override
    public void onStart() {
        super.onStart();

        // Attempt auto-sign in.
        if (!mIsResolving) {
            requestCredentials();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mCredentialsApiClient.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_HINT:
                // Drop into handling for RC_READ
            case RC_READ:
                if (resultCode == RESULT_OK) {
                    boolean isHint = (requestCode == RC_HINT);
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    processRetrievedCredential(credential, isHint);
                } else {
                    showToast("Credential Read Failed");
                }

                mIsResolving = false;
                break;
        }
    }

    /**
     * Attempt to resolve a non-successful Status from an asynchronous request.
     * @param status the Status to resolve.
     * @param requestCode the request code to use when starting an Activity for result,
     *                    this will be passed back to onActivityResult.
     */
    private void resolveResult(Status status, int requestCode) {
        // We don't want to fire multiple resolutions at once since that can result
        // in stacked dialogs after rotation or another similar event.
        if (mIsResolving) {
            return;
        }

        if (status.hasResolution()) {
            try {
                status.startResolutionForResult(MainActivity.this, requestCode);
                mIsResolving = true;
            } catch (IntentSender.SendIntentException e) {
            }
        } else {
            showToast("Could Not Resolve Error");
        }
    }

    /**
     * Process a Credential object retrieved from a successful request.
     * @param credential the Credential to process.
     * @param isHint true if the Credential is hint-only, false otherwise.
     */
    private void processRetrievedCredential(Credential credential, boolean isHint) {

        // If the Credential is not a hint, we should store it an enable the delete button.
        // If it is a hint, skip this because a hint cannot be deleted.
        if (!isHint) {

            Ougi.getInstance().getUser().setCredential(credential);
            attemptLogin();
        } else {
            //TODO: Ask to sign up?
            showToast("Credential Hint Retrieved");
        }
    }

    private void attemptLogin() {
        Call<User> call = Ougi.getInstance().getWaifuAPI()
                .getUserInfo(Ougi.getInstance().getUser().getUsername(),
                        Ougi.getInstance().getUser().getAuth());

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
            showToast("We were unable to log you in with those credentials.");
            Auth.CredentialsApi.delete(mCredentialsApiClient,
                    Ougi.getInstance().getUser().getCredential()).setResultCallback(
                    new ResultCallback() {
                        @Override
                        public void onResult(Result result) {
                            Ougi.getInstance().getUser().setCredential(null);
                            showScreen(LoginActivity.class);
                        }
                    });
            return;
        }

        Credential buffer = Ougi.getInstance().getUser().getCredential();

        User user = response.body();
        user.setCredential(buffer);
        Ougi.getInstance().setUser(user);

        showScreen(SadActivity.class);
    }

    /**
     * Request Credentials from the Credentials API.
     */
    private void requestCredentials() {
        // Request all of the user's saved username/password credentials.  We are not using
        // setAccountTypes so we will not load any credentials from other Identity Providers.
        CredentialRequest request = new CredentialRequest.Builder()
                .setPasswordLoginSupported(true)
                .build();

        Auth.CredentialsApi.request(mCredentialsApiClient, request).setResultCallback(
                new ResultCallback<CredentialRequestResult>() {
                    @Override
                    public void onResult(CredentialRequestResult credentialRequestResult) {
                        Status status = credentialRequestResult.getStatus();
                        if (status.isSuccess()) {
                            // Successfully read the credential without any user interaction, this
                            // means there was only a single credential and the user has auto
                            // sign-in enabled.
                            processRetrievedCredential(credentialRequestResult.getCredential(), false);
                        } else if (status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
                            // This is most likely the case where the user has multiple saved
                            // credentials and needs to pick one
                            resolveResult(status, RC_READ);
                        } else if (status.getStatusCode() == CommonStatusCodes.SIGN_IN_REQUIRED) {
                            // This means only a hint is available, but we are handling that
                            // elsewhere so no need to act here.
                            showScreen(LoginActivity.class);
                        } else {
                            showToast("Error retrieving credentials.");
                        }
                    }
                });
    }


}
