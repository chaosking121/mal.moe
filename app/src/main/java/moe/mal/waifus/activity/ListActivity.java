package moe.mal.waifus.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Waifu;

public abstract class ListActivity extends SidebarActivity {

    @BindView(R.id.listView) ListView listView;
    @BindView(R.id.swipeToRefresh) SwipeRefreshLayout swipeContainer;

    Drawable entryIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ButterKnife.bind(this);

        super.setUpSidebar();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWaifus();
            }
        });

        setWaifus();
    }

    /**
     * Handles when a waifu in a list is long pressed
     * @param waifu the waifu that long pressed
     */
    protected abstract void handleLongPress(String waifu);

    /**
     * Method that refreshes the waifus from the backend and updates the view
     */
    protected abstract void refreshWaifus();

    /**
     * Method that gets called to setup the initial view with a list of waifus.
     * Can be different from refreshWaifus
     */
    protected abstract void setWaifus();

    /**
     * @return the icon to be used for the button on this entry
     */
    protected abstract Drawable getActionDrawable();
}
