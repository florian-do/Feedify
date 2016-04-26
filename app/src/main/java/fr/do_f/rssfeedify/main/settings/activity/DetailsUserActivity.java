package fr.do_f.rssfeedify.main.settings.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.RestClient;
import fr.do_f.rssfeedify.api.json.users.UpdateUserResponse;
import fr.do_f.rssfeedify.api.json.users.UpdateUserResponse.*;
import fr.do_f.rssfeedify.api.json.users.UsersReponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsUserActivity extends AppCompatActivity {

    private static final String     ARG_USER = "user";
    private static final String     ARG_ADMIN = "type";
    private static final String     TAG = "DetailsUserActivity";

    @Bind(R.id.toolbar)
    Toolbar             toolbar;

    @Bind(R.id.updateuser_username)
    EditText            username;

    @Bind(R.id.updateuser_password)
    EditText            password;

    @Bind(R.id.updateuser_admin)
    Switch              isAdmin;

    private UsersReponse.User   user;
    private String              token;
    private Boolean             type;

    public static void newActivity(UsersReponse.User user, Boolean admin, Activity activity) {
        Intent i = new Intent(activity, DetailsUserActivity.class);
        i.putExtra(ARG_USER, user);
        i.putExtra(ARG_ADMIN, admin);
        activity.startActivityForResult(i, Utils.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_settings_activity_detailsuser);
        ButterKnife.bind(this);
        setupToolbar();
        user = (UsersReponse.User) getIntent().getSerializableExtra(ARG_USER);
        type = getIntent().getBooleanExtra(ARG_ADMIN, false);
        initView();
    }

    @OnClick(R.id.updateuser_submit)
    public void onSubmit(View v) {

        if (username.getText().length() < 6
                || password.getText().length() < 6)
        {
            return ;
        }

        String type = (isAdmin.isChecked())
                ? "admin"
                : "user";

        User u = new User(
                username.getText().toString(),
                password.getText().toString(),
                type
        );
        Call<UpdateUserResponse> call = RestClient.get(token).updateUser(user.getUsername(), u);
        call.enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                if (response.body() != null) {
                    setResult(RESULT_OK, null);
                    finish();
                } else {
                    Log.d(TAG, "body == null : "+response.code());
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                Log.d(TAG, "onFailure : "+t.getMessage());
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initView() {
        token = getSharedPreferences(Utils.SP, Context.MODE_PRIVATE).getString(Utils.TOKEN, "null");
        username.setText(user.getUsername());
        if (!type) {
            isAdmin.setVisibility(View.GONE);
        }
        isAdmin.setChecked((user.getType().equals("admin")));
    }
}
