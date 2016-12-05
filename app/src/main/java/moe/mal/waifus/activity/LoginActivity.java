package moe.mal.waifus.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Token;
import moe.mal.waifus.network.WaifuAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends GenericActivity {

    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((EditText) findViewById(R.id.usernameField)).setText(Ougi.getInstance().getUsername());
        ((EditText) findViewById(R.id.passwordField)).setText(Ougi.getInstance().getPassword());
    }

    public void loginPressed(View v) {
        String username = ((EditText) findViewById(R.id.usernameField)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();

        Ougi.getInstance().setCredentials(username, password);

        if (verifyCredentials()) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Ougi.getInstance().getMainActivity());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.commit();
            showToast(String.format("Credentials saved successfully, welcome %s!", username));
        } else {
            showToast("Those credentials were invalid.");
            Ougi.getInstance().setCredentials(
                    PreferenceManager.getDefaultSharedPreferences(this).getString("username", "user"),
                    PreferenceManager.getDefaultSharedPreferences(this).getString("password", "pass"));
        }
    }

    private boolean verifyCredentials() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.api_url_base))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WaifuAPI waifuAPI = retrofit.create(WaifuAPI.class);
        Call<Token> call = waifuAPI.getToken(Ougi.getInstance().getAuth());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                handleLoginResponse(response.body().getToken());
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                handleLoginResponse(null);
            }
        });

        return status;
    }

    private void handleLoginResponse(String token) {
        status = (!((token == null) || (token.isEmpty())));
    }

    public void signUpPressed(View v) {

    }
}
