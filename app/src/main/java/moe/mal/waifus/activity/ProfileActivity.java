package moe.mal.waifus.activity;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;

public class ProfileActivity extends AuthActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void logoutPressed(View v) {
        Auth.CredentialsApi.delete(mCredentialsApiClient,
                Ougi.getInstance().getUser().getCredential()).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(Result result) {
                        Ougi.getInstance().getUser().setCredential(null);
                        showScreen(LoginActivity.class);
                    }
                });

    }
}
