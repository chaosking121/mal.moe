package moe.mal.waifus.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Waifu;

public abstract class ListActivity extends SidebarActivity {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.swipeToRefresh) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.list_fab) FloatingActionButton list_fab;

    protected List<Waifu> waifus = new ArrayList<>();
    protected WaifuAdapter adapter;

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
        adapter = new WaifuAdapter(a, waifus);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        list_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchPrompt();
            }
        });
        refreshWaifus();
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
     * @return the icon to be used for the button on this entry
     */
    protected abstract Drawable getActionDrawable();

}
