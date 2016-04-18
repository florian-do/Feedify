package fr.do_f.rssfeedify.api;

import fr.do_f.rssfeedify.api.json.feeds.AddFeedResponse;
import fr.do_f.rssfeedify.api.json.feeds.AddFeedResponse.AddFeedPost;
import fr.do_f.rssfeedify.api.json.feeds.FeedResponse;
import fr.do_f.rssfeedify.api.json.login.LoginResponse;
import fr.do_f.rssfeedify.api.json.login.LoginResponse.LoginPost;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse.RegisterPost;
import fr.do_f.rssfeedify.api.json.menu.GetFeedResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by do_f on 03/04/16.
 */
public interface ApiService {

    @POST("register")
    Call<RegisterResponse>  register(@Body RegisterPost post);

    @POST("login")
    Call<LoginResponse>     login(@Body LoginPost post);

    @POST("feed")
    Call<AddFeedResponse>   addFeed(@Body AddFeedPost post);

    @GET("feeds")
    Call<GetFeedResponse>   getFeed();

    @GET("feeds/articles/{id}")
    Call<FeedResponse>      getAllFeed(@Path("id") int page);

    @GET("feed/{feedid}/articles/{page}")
    Call<FeedResponse>      getAllFeedById(@Path("feedid") int id, @Path("page") int page);
}
