package com.crux.qxm.utils;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private final int left;
    private final int top;
    private final int right;
    private final int bottom;

    public RecyclerViewItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

//        outRect.set(left, top, right, bottom);

        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }

        if (itemPosition == 0) {
            outRect.top = top;
        }

        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;
    }
}