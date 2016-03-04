package com.learn.featureapp.dashboard;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.learn.featureapp.R;
import com.learn.featureapp.googlepay.activities.GooglePayActivity;
import com.learn.featureapp.navdrawer.NavigationDrawerActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<Demo> demos = Arrays.asList(
                new Demo(this, NavigationDrawerActivity.class, R.string.navigation_drawer),
                new Demo(this, GooglePayActivity.class, R.string.google_pay));
        ArrayAdapter<Demo> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                demos);
        getListView().setAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Demo demo = demos.get(position);
                startActivity(new Intent(MainActivity.this, demo.activityClass));
            }
        });
    }

    public static class Demo {
        public final Class<?> activityClass;
        public final String title;

        public Demo(Context context, Class<?> activityClass, int titleId) {
            this.activityClass = activityClass;
            this.title = context.getString(titleId);
        }

        @Override
        public String toString() {
            return this.title;
        }
    }
}