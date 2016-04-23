package fr.do_f.rssfeedify.main.menu.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse.*;

/**
 * Created by do_f on 17/04/16.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MenuAdapter";

    private onItemClickListener onItemClickListener;
    private List<Feed>          feed;

    public MenuAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_adapter, parent, false);
        CellMenuViewHolder cellMenuViewHolder = new CellMenuViewHolder(v);
        return cellMenuViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CellMenuViewHolder) holder).bindView(feed.get(position), position);
    }

    @Override
    public int getItemCount() {
        int size = (feed == null) ? 0 : feed.size();
        return size;
    }

    public synchronized void refreshAdapter(List<Feed> newFeed) {
        if (feed != null) {
            feed.clear();
            feed.addAll(newFeed);
        } else {
            feed = newFeed;
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    class CellMenuViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.menu_circle)
        ImageView           circle;

        @Bind(R.id.menu_circle_text)
        TextView            circle_text;

        @Bind(R.id.menu_title)
        TextView            title;

        private View v;

        private Boolean alreadySet;

        public CellMenuViewHolder(View v) {
            super(v);
            this.alreadySet = false;
            ButterKnife.bind(this, v);
            this.v = v;
        }

        public void bindView(final Feed feed, final int position) {
            if (!alreadySet) {
                GradientDrawable bgShape = (GradientDrawable)circle.getBackground();
                bgShape.setColor(v.getResources().getColor(Utils.colors[random(0, Utils.colors.length-1)]));
                alreadySet = true;
            }
            circle_text.setText(feed.getName().substring(0, 1));
            title.setText(feed.getName());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(position);
                }
            });
        }

        public int random(int min, int max)
        {
            int range = (max - min) + 1;
            return (int)(Math.random() * range) + min;
        }
    }
}
