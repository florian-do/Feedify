package fr.do_f.rssfeedify.main.settings.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import fr.do_f.rssfeedify.main.settings.adapter.AdminAdapter;

/**
 * Created by do_f on 24/04/16.
 */
public class RecyclerViewSwipedCallback extends ItemTouchHelper.Callback {

    private final AdminAdapter mAdapter;

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    public RecyclerViewSwipedCallback(AdminAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d("TEST", "onSwiped + "+direction);
        if (direction == 32) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
