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
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse.*;

/**
 * Created by do_f on 17/04/16.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MenuAdapter";

    private onItemClickListener onItemClickListener;
    private List<Feed>          feed;

    public MenuAdapter(List<Feed> feed) {
        this.feed = feed;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_adapter, parent, false);
        CellMenuViewHolder cellMenuViewHolder = new CellMenuViewHolder(v);
        return cellMenuViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CellMenuViewHolder) holder).bindView(feed.get(position));
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(int feedid);
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

        private int[] colors = {
                R.color.md_red_500,
                R.color.md_red_A200,
                R.color.md_pink_500,
                R.color.md_pink_A200,
                R.color.md_purple_500,
                R.color.md_deep_purple_500,
                R.color.md_deep_purple_A200,
                R.color.md_indigo_500,
                R.color.md_indigo_A200,
                R.color.md_blue_500,
                R.color.md_blue_A200,
                R.color.md_light_blue_500,
                R.color.md_light_blue_A200,
                R.color.md_cyan_500,
                R.color.md_teal_500,
                R.color.md_green_500,
                R.color.md_green_A200,
                R.color.md_light_green_500,
                R.color.md_lime_500,
                R.color.md_amber_500,
                R.color.md_amber_A200,
                R.color.md_orange_500,
                R.color.md_orange_A200,
                R.color.md_deep_orange_500,
                R.color.md_deep_orange_A200
        };

        public CellMenuViewHolder(View v) {
            super(v);
            this.alreadySet = false;
            ButterKnife.bind(this, v);
            this.v = v;
        }

        public void bindView(final Feed feed) {
            if (!alreadySet) {
                GradientDrawable bgShape = (GradientDrawable)circle.getBackground();
                bgShape.setColor(v.getResources().getColor(colors[random(0, colors.length-1)]));
                alreadySet = true;
            }
            circle_text.setText(feed.getName().substring(0, 1));
            title.setText(feed.getName());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(feed.getId());
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
