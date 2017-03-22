package moe.mal.waifus.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import moe.mal.waifus.Ougi;
import moe.mal.waifus.R;
import moe.mal.waifus.model.AuthLevel;
import moe.mal.waifus.model.Waifu;

/**
 * Custom adapter for populating listviews with waifu objects.
 * Created by Arshad on 10/12/2016.
 */

public class WaifuAdapter extends RecyclerView.Adapter<WaifuAdapter.ViewHolder> {

    private static Map<String, Integer> listEntryOverflowMenuLookup;

    private final List<Waifu> list;
    private final Activity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.count) TextView count;
        @BindView(R.id.action) ImageButton action;
        @BindDrawable(R.drawable.ic_more_vert_black_24dp) Drawable moreIcon;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            listEntryOverflowMenuLookup = new HashMap<>();
            listEntryOverflowMenuLookup.put("Fave Action", 0);
            listEntryOverflowMenuLookup.put("Scrape", 1);
            listEntryOverflowMenuLookup.put("Delete", 2);
        }
    }

    private static class WaifuMenu implements View.OnClickListener, View.OnLongClickListener {

        Waifu waifu;
        private Context c;
        private WaifuAdapter.ViewHolder holder;
        boolean isAll;

        WaifuMenu(Waifu w, Context c, WaifuAdapter.ViewHolder holder, boolean isAll) {
            this.waifu = w;
            this.c = c;
            this.holder = holder;
            this.isAll = isAll;
        }

        @Override
        public void onClick(View v) {
            showMenu(v);
        }

        @Override
        public boolean onLongClick(View v) {
            showMenu(v);
            return true;
        }

        private void showMenu(View v) {
            PopupMenu popup = new PopupMenu(c, holder.action);
            popup.inflate(R.menu.waifu_menu);

            if (Ougi.getInstance().getUser().getAuthLevel() < AuthLevel.WEEB.getValue()) {
                // Hide the list button button for non-mods
                popup.getMenu().getItem(listEntryOverflowMenuLookup.get("Fave Action")).setVisible(false);
            } else {
                popup.getMenu().getItem(0).setTitle(((ListActivity) c).getListActionTitle());
            }

            if (Ougi.getInstance().getUser().getAuthLevel() < AuthLevel.MOD.getValue()) {
                // Hide the scrape button for non-mods
                popup.getMenu().getItem(listEntryOverflowMenuLookup.get("Scrape")).setVisible(false);
            }

            if (Ougi.getInstance().getUser().getAuthLevel() < AuthLevel.ADMIN.getValue()) {
                // Hide the delete button for non-admins
                popup.getMenu().getItem(listEntryOverflowMenuLookup.get("Delete")).setVisible(false);
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.waifu_list_action:
                            ((ListActivity) c).handleListAction(waifu.getName());
                            break;
                        case R.id.scrape_waifu:
                            ((ListActivity) c).showScreen(ScrapeActivity.class, "waifu", waifu.getName());
                            break;
                        case R.id.delete_waifu:
                            ((GenericActivity) c).showToast("Coming soon.");
                            break;
                    }
                    return false;
                }
            });
            popup.show();
        }
    }

    public WaifuAdapter(Activity context, List<Waifu> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public WaifuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.waifu_entry, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WaifuAdapter.ViewHolder holder, int position) {
        final String name = list.get(position).getName();

        holder.name.setText(name);
        holder.count.setText(String.valueOf(list.get(position).getCount()));
        holder.action.setImageDrawable(context.getDrawable(R.drawable.ic_more_vert_black_24dp));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListActivity) context).displayWaifu(name);
            }
        });

        WaifuMenu wm = new WaifuMenu(list.get(position), context, holder,
                ((ListActivity) context).isAll());

        holder.count.setOnClickListener(wm);
        holder.name.setOnLongClickListener(wm);
        holder.action.setOnClickListener(wm);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
