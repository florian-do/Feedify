package fr.do_f.rssfeedify.main;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse.*;
import fr.do_f.rssfeedify.main.feed.fragment.FeedFragment;
import fr.do_f.rssfeedify.main.feed.activity.AddFeedActivity;
import fr.do_f.rssfeedify.main.menu.adapter.MenuAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MenuAdapter.onItemClickListener {

    private static final String     TAG = "MainActivity";

    @Bind(R.id.rvFeed)
    RecyclerView            feed;

    @Bind(R.id.fab)
    FloatingActionButton    fab;

    @Bind(R.id.menu_home)
    LinearLayout            home;

    private String          token;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ganjify)));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFeed();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.HOME, 0))
                .addToBackStack(null)
                .commit();
    }

    public void initFeed() {
        Call<GetFeedResponse> call = RestClient.get(token).getFeed();
        call.enqueue(new Callback<GetFeedResponse>() {
            @Override
            public void onResponse(Call<GetFeedResponse> call, Response<GetFeedResponse> response) {
                if (response.body() != null)
                    setupFeed(response.body().getFeed());
                else
                    Log.d(TAG, "error 500");

            }

            @Override
            public void onFailure(Call<GetFeedResponse> call, Throwable t) {
                Log.d(TAG, "onFailure : "+t.getMessage());
            }
        });
    }

    public void setupFeed(List<Feed> feed) {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        this.feed.setLayoutManager(lm);
        this.feed.setHasFixedSize(true);
        MenuAdapter adapter = new MenuAdapter(feed);
        adapter.setOnItemClickListener(this);
        this.feed.setAdapter(adapter);
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

    @OnClick(R.id.fab)
    public void onFabClick()
    {
        int[] startingLocation = new int[2];
        fab.getLocationOnScreen(startingLocation);
        startingLocation[0] += fab.getWidth() / 2;
        AddFeedActivity.newActivity(startingLocation, this);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemClick(int feedid) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.FEEDBYID, feedid))
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @OnClick(R.id.menu_home)
    public void onClickHome() {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, FeedFragment.newInstance(Utils.HOME, 0))
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
