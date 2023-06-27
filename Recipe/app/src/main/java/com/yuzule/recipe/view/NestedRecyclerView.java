package com.yuzule.recipe.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

// 被嵌套的RecyclerView
public class NestedRecyclerView extends RecyclerView {

    public NestedRecyclerView(Context context) {
        super(context);
    }

    public NestedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int newHeightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, newHeightSpec);
    }
}