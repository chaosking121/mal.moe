package moe.mal.waifus.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.mal.waifus.R;
import moe.mal.waifus.model.Waifu;

/**
 * Custom adapter for populating listviews with waifu objects.
 * Created by Arshad on 10/12/2016.
 */

public class WaifuAdapter extends RecyclerView.Adapter<WaifuAdapter.ViewHolder> {

    private final List<Waifu> list;
    private final Activity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.count) TextView count;
        @BindView(R.id.action) ImageButton action;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.waifu_entry, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WaifuAdapter.ViewHolder holder, int position) {
        final String name = list.get(position).getName();

        holder.name.setText(name);
        holder.count.setText(String.valueOf(list.get(position).getCount()));
        holder.action.setImageDrawable(((ListActivity) context).getActionDrawable());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListActivity) context).displayWaifu(name);
            }
        });

        holder.name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((ListActivity) context).handleLongPress(name);
                return true;
            }
        });

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListActivity) context).handleLongPress(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
