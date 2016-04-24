package fr.do_f.rssfeedify.main.feed.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse.*;

public class DetailsActivity extends AppCompatActivity implements Html.ImageGetter {

    private static final String     TAG = "DetailsActivity";
    private static final String     ARG_ARTICLE = "article";

    @Bind(R.id.feed_details_image)
    SimpleDraweeView    image;

    @Bind(R.id.feed_title)
    TextView            title;

    @Bind(R.id.feed_details_content)
    TextView            content;

    private Articles    articles;

    public static void newActivity(Activity activity, View v, Articles articles) {
        Intent i = new Intent(activity, DetailsActivity.class);
        i.putExtra(ARG_ARTICLE, articles);
        //activity.startActivity(i);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,

                /*new Pair<View, String>(v.findViewById(R.id.feed_image),
                        activity.getString(R.string.transition_feed_image)),*/
                new Pair<View, String>(v.findViewById(R.id.feed_date),
                        activity.getString(R.string.transition_feed_date)),
                new Pair<View, String>(v.findViewById(R.id.feed_time),
                        activity.getString(R.string.transition_feed_date2)),
                new Pair<View, String>(v.findViewById(R.id.feed_title),
                        activity.getString(R.string.transition_feed_title))
        );

        ActivityCompat.startActivity(activity, i, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_feed_activity_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        articles = (Articles) getIntent().getSerializableExtra(ARG_ARTICLE);
        title.setText(articles.getTitle());
        content.setText(Html.fromHtml(articles.getFull(), this, null));
        content.setMovementMethod(LinkMovementMethod.getInstance());


        //getWindow().setEnterTransition(TransitionUtils.makeEnterTransition());
        //getWindow().setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition(this));
        //setEnterSharedElementCallback(new EnterSharedElementCallback(this));

        if (articles.getUrl() != null) {
            image.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(articles.getUrl());
            image.setImageURI(uri);
        } else {
            image.setVisibility(View.GONE);
        }
    }

    @Override
    public Drawable getDrawable(String source) {
        Log.d(TAG, "getDrawable : "+source);
        Drawable d = getResources().getDrawable(R.drawable.drawable_background_box);
        if (d != null) {
            d.setBounds(0, 0, 0, 0);
        }
        return d;
    }
}
