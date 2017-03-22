package moe.mal.waifus.activity;

import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Abstract activity for classes that need access to the Credential API
 * Created by Arshad on 04/12/2016.
 */

public abstract class AuthActivity extends GenericActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected static String EMAIL_REGEX = "^(.+)@(.+)$";
    protected static String USERNAME_REGEX = "[A-Za-z0-9_]+";

    protected GoogleApiClient mCredentialsApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate GoogleApiClient.  This is a very simple GoogleApiClient that only connects
        // to the Auth.CREDENTIALS_API, which does not require the user to go through the sign-in
        // flow before connecting.  If you are going to use this API with other Google APIs/scopes
        // that require sign-in (such as Google+ or Google Drive), it may be useful to separate
        // the CREDENTIALS_API into its own GoogleApiClient with separate lifecycle listeners.
        mCredentialsApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCredentialsApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCredentialsApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
}
