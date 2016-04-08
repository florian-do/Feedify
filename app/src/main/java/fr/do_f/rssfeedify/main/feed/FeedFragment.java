package fr.do_f.rssfeedify.main.feed;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.main.feed.adapter.FeedAdapter;
import fr.do_f.rssfeedify.main.feed.listener.EndlessRecyclerViewScrollListener;

public class FeedFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, FeedAdapter.onItemClickListener {

    private static final String     TAG = "FeedFragment";

    @Bind(R.id.rvFeed)
    RecyclerView rvFeed;

    @Bind(R.id.feed_swipe)
    SwipeRefreshLayout swipe;

    private FeedAdapter mAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.main_feed_fragment_feed, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
        setupFeed();
    }

    public void setupFeed() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rvFeed.setHasFixedSize(true);
        rvFeed.setLayoutManager(lm);
        mAdapter = new FeedAdapter(10);
        mAdapter.setOnItemClickListener(this);
        rvFeed.setAdapter(mAdapter);
        rvFeed.addOnScrollListener(new EndlessRecyclerViewScrollListener(lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO: REFRESH API CALL
                Log.d(TAG, "onLoadMore");
                new PrefetchData().execute();
            }
        });
    }

    @Override
    public void onRefresh() {
        mAdapter.refreshAdapter(mAdapter.getItemCount());
        swipe.setRefreshing(false);
    }


    // do_f Interface Recycler View onClick
    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "POSITION : "+position);
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipe.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mAdapter.refreshAdapter(mAdapter.getItemCount() + 10);
            swipe.setRefreshing(false);
        }
    }
}
