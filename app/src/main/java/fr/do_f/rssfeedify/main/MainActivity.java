package fr.do_f.rssfeedify.main;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse.*;
import fr.do_f.rssfeedify.broadcast.NetworkReceiver;
import fr.do_f.rssfeedify.main.feed.fragment.FeedFragment;
import fr.do_f.rssfeedify.main.feed.activity.AddFeedActivity;
import fr.do_f.rssfeedify.main.menu.adapter.MenuAdapter;

import fr.do_f.rssfeedify.main.settings.activity.AdminActivity;
import fr.do_f.rssfeedify.main.settings.activity.SettingsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MenuAdapter.onItemClickListener, NetworkReceiver.onNetworkStateChanged {

    private static final String     TAG = "MainActivity";

    @Bind(R.id.rvFeed)
    RecyclerView            feed;

    @Bind(R.id.fab)
    FloatingActionButton    fab;

    @Bind(R.id.menu_home)
    LinearLayout            home;

    @Bind(R.id.toolbar)
    Toolbar                 toolbar;

    private String          token;
    private List<Feed>      feedInfo;
    private NetworkReceiver network;
    private MenuAdapter     adapter;
    private int             networkState;


    public static void newActivity(Activity activity)
    {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        token = getSharedPreferences(Utils.SP, Context.MODE_PRIVATE).getString(Utils.TOKEN, "null");
        setSupportActionBar(toolbar);

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ganjify)));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        network = new NetworkReceiver();
        this.registerReceiver(network, filter);


        network = new NetworkReceiver();
        network.setOnNetworkStateChanged(this);
        networkState = network.singleCheck(this);

        initFeed();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.HOME, null))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_CODE && resultCode == RESULT_OK) {
            refreshRecycler();
        }
    }

    // init Drawer Menu List
    public void initFeed() {
        setupFeed();
        if (networkState == NetworkReceiver.STATE_OFF)
        {
            Type listType = new TypeToken<List<Feed>>() {}.getType();
            feedInfo = Utils.read(this, Utils.FILE_MENU, listType);
            Log.d(TAG, "INIT MENU NETWORK OFF");
            if (feedInfo == null) {
                //Snackbar.make(getView(), "Error, can't retreive the feed", Snackbar.LENGTH_SHORT).show();
                return ;
            } else {
                adapter.refreshAdapter(feedInfo);
            }
        }
        else {
            refreshRecycler();
        }
    }

    public void setupFeed() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        feed.setLayoutManager(lm);
        feed.setHasFixedSize(true);
        adapter = new MenuAdapter();
        adapter.setOnItemClickListener(this);
        feed.setAdapter(adapter);
    }

    public void refreshRecycler() {
        Call<GetFeedResponse> call = RestClient.get(token).getFeed();
        call.enqueue(new Callback<GetFeedResponse>() {
            @Override
            public void onResponse(Call<GetFeedResponse> call, Response<GetFeedResponse> response) {
                if (response.body() != null) {
                    feedInfo = response.body().getFeed();
                    Utils.write(getApplicationContext(), feedInfo, Utils.FILE_MENU);
                    adapter.refreshAdapter(response.body().getFeed());
                }
                else {
                    Log.d(TAG, "error 500");
                }
            }

            @Override
            public void onFailure(Call<GetFeedResponse> call, Throwable t) {
                Log.d(TAG, "onFailure : "+t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick()
    {
        int[] startingLocation = new int[2];
        fab.getLocationOnScreen(startingLocation);
        startingLocation[0] += fab.getWidth() / 2;
        AddFeedActivity.newActivity(startingLocation, this);
        overridePendingTransition(0, 0);
    }


    // On Drawer Menu Item Click
    @Override
    public void onItemClick(int position) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.FEEDBYID, feedInfo.get(position)))
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.menu_home)
    public void onClickHome() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.HOME, null))
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    // do_f Interface on Network Change
    @Override
    public void onStateChange(int state) {
        if (state == NetworkReceiver.STATE_ON) {
            //onRefresh();
            networkState = state;
        } else {
            networkState = state;
        }
    }






    // USELESS



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            Intent i = new Intent(this, SettingsActivity.class);
//            startActivity(i);
            int[] startingLocation = new int[2];
            toolbar.getLocationOnScreen(startingLocation);
            startingLocation[0] += toolbar.getWidth() / 2;
            AdminActivity.newActivity(startingLocation, this);
            overridePendingTransition(0, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
