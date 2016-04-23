package fr.do_f.rssfeedify.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import fr.do_f.rssfeedify.R;

/**
 * Created by do_f on 21/04/16.
 */
public class NetworkReceiver extends BroadcastReceiver {

    public static final int STATE_OFF = 1;
    public static final int STATE_ON = 2;

    private onNetworkStateChanged onNetworkStateChanged;

    public interface onNetworkStateChanged {
        void onStateChange(int state);
    }

    public void setOnNetworkStateChanged(onNetworkStateChanged onNetworkStateChanged) {
        this.onNetworkStateChanged = onNetworkStateChanged;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (onNetworkStateChanged != null)
                onNetworkStateChanged.onStateChange(STATE_ON);
            Toast.makeText(context, "CO", Toast.LENGTH_SHORT).show();
        } else {
            if (onNetworkStateChanged != null)
                onNetworkStateChanged.onStateChange(STATE_OFF);
            Toast.makeText(context, "PAS CO", Toast.LENGTH_SHORT).show();
        }
    }



    public int singleCheck(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null) {
            return STATE_ON;
        } else {
            return STATE_OFF;
        }
    }

    public void checkNetworkState(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (onNetworkStateChanged != null)
                onNetworkStateChanged.onStateChange(STATE_ON);
            Toast.makeText(context, "CO", Toast.LENGTH_SHORT).show();
        } else {
            if (onNetworkStateChanged != null)
                onNetworkStateChanged.onStateChange(STATE_OFF);
            Toast.makeText(context, "PAS CO", Toast.LENGTH_SHORT).show();
        }
    }
}