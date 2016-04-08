package fr.do_f.rssfeedify.main.feed.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;

/**
 * Created by do_f on 06/04/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // TODO: add LIST
    private int size;

    private onItemClickListener onItemClickListener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public FeedAdapter(int size)
    {
        this.size = size;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_feed_adapter_feed, parent, false);
        CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
        return cellFeedViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CellFeedViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public synchronized void refreshAdapter(int size) {
        // TODO: clear and update the list
        this.size = size;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class CellFeedViewHolder extends RecyclerView.ViewHolder
    {

        @Bind(R.id.feed_image)
        SimpleDraweeView    image;

        private View v;

        public CellFeedViewHolder(View view) {
            super(view);
            v = view;
            ButterKnife.bind(this, view);
        }

        public void bindView(final int position)
        {
            Uri uri;
            if (position % 10 == 0) {
                uri = Uri.parse("https://cdn2.vox-cdn.com/thumbor/k7NG0DDf5fLxvAuOZ2lU2KBkT7k=/50x0:2450x1600/1310x873/cdn0.vox-cdn.com/uploads/chorus_image/image/49252681/reddit-ios-app-screenshots.0.0.jpg");
            } else {
                uri = Uri.parse(v.getResources().getString(R.string.feed_image));
            }
            image.setImageURI(uri);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(position);
                }
            });
        }
    }
}
