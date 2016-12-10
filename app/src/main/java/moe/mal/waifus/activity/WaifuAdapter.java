package moe.mal.waifus.activity;

import android.app.Activity;
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

public class WaifuAdapter extends ArrayAdapter<Waifu> {

    private final List<Waifu> list;
    private final Activity context;
    private LayoutInflater inflater;

    static class ViewHolder {
        @BindView(R.id.name) TextView name;
        @BindView(R.id.count) TextView count;
        @BindView(R.id.action) ImageButton action;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public WaifuAdapter(Activity context, List<Waifu> list) {
        super(context, R.layout.waifu_entry, list);
        this.context = context;
        this.list = list;
        inflater = context.getLayoutInflater();
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.waifu_entry, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

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

        return view;
    }

}
