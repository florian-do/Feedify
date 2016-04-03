package fr.do_f.rssfeedify.login.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;

public class LoginFragment extends Fragment {

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment_login, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState)
    {
        super.onActivityCreated(saveInstanceState);
    }
}
