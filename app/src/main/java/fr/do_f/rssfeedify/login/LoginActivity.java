package fr.do_f.rssfeedify.login;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.os.Bundle;

import butterknife.OnClick;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.login.fragment.LoginFragment;
import fr.do_f.rssfeedify.login.fragment.MenuFragment;
import fr.do_f.rssfeedify.login.fragment.RegisterFragment;

public class LoginActivity extends AppCompatActivity
        implements MenuFragment.OnFragmentInteractionListener {

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, MenuFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() > 1)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void showLogin() {
        fm.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showRegister() {
        fm.beginTransaction()
                .replace(R.id.container, RegisterFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}
