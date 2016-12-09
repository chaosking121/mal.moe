package moe.mal.waifus.activity;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                displayWaifu(((Waifu) parent.getItemAtPosition(position)).getName());
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                handleLongPress(((Waifu) parent.getItemAtPosition(position)).getName());
                return true;
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
}
