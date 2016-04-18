package fr.do_f.rssfeedify.main.feed.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse.*;


/**
 * Created by do_f on 06/04/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FeedAdapter";

    private onItemClickListener onItemClickListener;
    private List<Articles>      articles;

    public interface onItemClickListener {
        void onItemClick(Articles articles, View v);
    }

    public FeedAdapter(List<Articles> articles) {
        this.articles = articles;
        setImage();
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
        ((CellFeedViewHolder) holder).bindView(articles.get(position));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public synchronized void refreshAdapter(List<Articles> newArticles, boolean wipedata) {
        if (wipedata)
            this.articles.clear();
        this.articles.addAll(newArticles);
        setImage();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setImage() {
        for (Articles a : this.articles) {
            //Log.d(TAG, "setImage");
            Document doc = Jsoup.parse(a.getFull());

            for (Element e : doc.getElementsByTag("img")) {
                if (e.attr("src").length() != 0 && e.attr("width").length() != 0) {
                    a.setUrl(e.attr("src"));
                    break;
                }
            }
        }
    }

    public class CellFeedViewHolder extends RecyclerView.ViewHolder
    {

        @Bind(R.id.feed_title)
        TextView            title;

        @Bind(R.id.feed_image)
        SimpleDraweeView    image;

        private View v;

        public CellFeedViewHolder(View view) {
            super(view);
            v = view;
            ButterKnife.bind(this, view);
        }

        public void bindView(final Articles articles)
        {
            if (articles.getUrl() != null) {
                image.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(articles.getUrl());
                image.setImageURI(uri);
            } else {
                image.setVisibility(View.GONE);
            }

            title.setText(articles.getTitle());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(articles, v);
                }
            });
        }
    }
}
