package fr.do_f.rssfeedify.main.settings.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.do_f.rssfeedify.R;
import fr.do_f.rssfeedify.Utils;
import fr.do_f.rssfeedify.api.json.users.UsersReponse.*;

/**
 * Created by do_f on 23/04/16.
 */
public class AdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdminAdapter";

    private setOnClickListener  setOnClickListener;
    private int                 lastPosition = -1;
    private List<User>          users;
    private Context             context;

    public AdminAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.main_settings_adapter_admin, parent, false);
        return new CellAdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CellAdminViewHolder) holder).bindView(users.get(position), position);
        setAnimation(((CellAdminViewHolder) holder).container, position);
    }

//    @Override
//    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//    }

    @Override
    public int getItemCount() {
        int size = (users == null) ? 0 : users.size();
        return size;
    }

    public synchronized void refreshAdapter(List<User> newUsers) {
        if (users != null) {
            users.clear();
            users.addAll(newUsers);
        } else {
            users = newUsers;
        }
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void setSetOnClickListener(setOnClickListener setOnClickListener) {
        this.setOnClickListener = setOnClickListener;
    }

    public interface setOnClickListener {
        void onUserClick();
    }

    public class CellAdminViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.admin_circle)
        ImageView circle;

        @Bind(R.id.admin_circle_text)
        TextView circle_text;

        @Bind(R.id.admin_title)
        TextView            title;

        @Bind(R.id.mContainer)
        LinearLayout        container;

        private View v;

        private Boolean alreadySet;

        public CellAdminViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            alreadySet = false;
            v = view;
        }

        public void bindView(final User user, final int position) {
            if (!alreadySet) {
                GradientDrawable bgShape = (GradientDrawable)circle.getBackground();
                bgShape.setColor(v.getResources().getColor(Utils.colors[random(0, Utils.colors.length-1)]));
                alreadySet = true;
            }
            circle_text.setText(user.getUsername().substring(0, 1).toUpperCase());
            title.setText(user.getUsername());

//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onItemClickListener != null)
//                        onItemClickListener.onItemClick(position);
//                }
//            });
        }

        public int random(int min, int max)
        {
            int range = (max - min) + 1;
            return (int)(Math.random() * range) + min;
        }
    }
}
