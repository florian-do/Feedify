package fr.do_f.rssfeedify.api;

import fr.do_f.rssfeedify.api.json.login.LoginResponse;
import fr.do_f.rssfeedify.api.json.login.LoginResponse.LoginPost;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse;
import fr.do_f.rssfeedify.api.json.login.RegisterResponse.RegisterPost;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by do_f on 03/04/16.
 */
public interface ApiService {

    @POST("register")
    Call<RegisterResponse>  register(@Body RegisterPost user);

    @POST("login")
    Call<LoginResponse>     login(@Body LoginPost user);
}
