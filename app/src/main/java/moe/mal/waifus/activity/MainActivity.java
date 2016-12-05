package moe.mal.waifus.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;

public class MainActivity extends SidebarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        super.setUpSidebar();

        Ougi.getInstance().setMainActivity(this);

        Ougi.getInstance().setCredentials(
                PreferenceManager.getDefaultSharedPreferences(this).getString("username", "user"),
                PreferenceManager.getDefaultSharedPreferences(this).getString("password", "pass"));

        showScreen(SadActivity.class);
    }
}
