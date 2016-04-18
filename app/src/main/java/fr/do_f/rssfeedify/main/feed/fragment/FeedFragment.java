package fr.do_f.rssfeedify.main.feed.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse.*;
import fr.do_f.rssfeedify.main.feed.activity.DetailsActivity;
import fr.do_f.rssfeedify.main.feed.adapter.FeedAdapter;
import fr.do_f.rssfeedify.main.feed.listener.EndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, FeedAdapter.onItemClickListener {

    private static final String     TAG = "FeedFragment";
    private static final String     ARG_TYPE = "type";
    private static final String     ARG_ID = "id";

    @Bind(R.id.rvFeed)
    RecyclerView            rvFeed;

    @Bind(R.id.feed_swipe)
    SwipeRefreshLayout      swipe;

    private List<Articles>  articles;
    private FeedAdapter     mAdapter;
    private String          token;

    private String          type;
    private int             feedid;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance(String type, int feedid) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putInt(ARG_ID, feedid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
            if (type.equals(Utils.FEEDBYID))
                feedid = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_feed_fragment_feed, container, false);
        ButterKnife.bind(this, v);
        token = getActivity()
                .getSharedPreferences(Utils.SP, Context.MODE_PRIVATE)
                .getString(Utils.TOKEN, "null");
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

        swipe.setRefreshing(true);

        initFeed();
    }

    public void initFeed() {
        Call<FeedResponse> call;
        if (type.equals(Utils.HOME)) {
            call = RestClient.get(token).getAllFeed(1);
        } else {
            call = RestClient.get(token).getAllFeedById(feedid, 1);
        }

        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "setupfeed FIN "+response.body().getArticles().size());
                    articles = response.body().getArticles();
                    setupFeed();
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                Log.d(TAG, "onFailure");

            }
        });
    }

    public void setupFeed() {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rvFeed.setHasFixedSize(true);
        rvFeed.setLayoutManager(lm);
        mAdapter = new FeedAdapter(articles);
        mAdapter.setOnItemClickListener(this);
        rvFeed.setAdapter(mAdapter);
        rvFeed.addOnScrollListener(new EndlessRecyclerViewScrollListener(lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO: REFRESH API CALL
                if (page == 0) {

                } else {
                    addPage(page+1);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        Call<FeedResponse> call;
        if (type.equals(Utils.HOME)) {
            call = RestClient.get(token).getAllFeed(1);
        } else {
            call = RestClient.get(token).getAllFeedById(feedid, 1);
        }

        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "refresh FIN "+response.body().getArticles().size());
                    mAdapter.refreshAdapter(response.body().getArticles(), true);
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                Log.d(TAG, "onFailure");

            }
        });
    }

    public void addPage(final int page) {
        swipe.setRefreshing(true);
        Call<FeedResponse> call;
        if (type.equals(Utils.HOME)) {
            call = RestClient.get(token).getAllFeed(page);
        } else {
            call = RestClient.get(token).getAllFeedById(feedid, page);
        }
        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                Log.d(TAG, "ADD PAGE "+ page);
                if (response.body() != null) {
                    mAdapter.refreshAdapter(response.body().getArticles(), false);
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {

            }
        });
    }

    // do_f Interface Recycler View onClick
    @Override
    public void onItemClick(Articles articles, View v) {
        DetailsActivity.newActivity(getActivity(), v, articles);
    }
}
