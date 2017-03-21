package moe.mal.waifus.activity;

import android.os.Bundle;
import android.view.View;

import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;

public class SadActivity extends SidebarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sad);
        super.setUpSidebar();
    }

    /**
     * Handles when the lily button is pressed
     * @param v the view
     */
    public void lilySelected(View v) {
        displayWaifu("lily");
    }

    /**
     * Handles when the sad button is pressed
     * @param v the view
     */
    public void sadSelected(View v) {
        displayWaifu("sad");
    }
}
