package com.learn.featureapp.recycleview;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.learn.featureapp.R;

public class RecycleViewActivity extends Activity {
    private static final int LIST_VERTICAL_SPACING = 4;

    private CustomRecycleViewAdapter adapter;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new CustomRecycleViewAdapter(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecorator(dpToPx(LIST_VERTICAL_SPACING)));
        recyclerView.setAdapter(adapter);


        /**
         * to update the list call;
         * adapter.setItemList();
         **/
    }
}
