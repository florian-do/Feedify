package fr.do_f.rssfeedify.main.settings.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.json.users.UpdateUserResponse.*;

public class DetailsUserActivity extends AppCompatActivity {

    private static final String     ARG_USER = "user";

    @Bind(R.id.updateuser_username)
    EditText            username;

    @Bind(R.id.updateuser_password)
    EditText            password;

    @Bind(R.id.updateuser_admin)
    Switch              isAdmin;

    private User        user;

    public static void newActivity(User user, Activity activity) {
        Intent i = new Intent(activity, AddUserActivity.class);
        i.putExtra(ARG_USER, user);
        activity.startActivityForResult(i, Utils.REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_settings_activity_detailsuser);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(ARG_USER);
    }

    @OnClick(R.id.updateuser_submit)
    public void onSubmit(View v) {

        if (username.getText().length() < 6
                || password.getText().length() < 6)
        {
            return ;
        }

        User u = new User(
                username.getText().toString(),
                password.getText().toString(),
                "true"
        );
    }
}
