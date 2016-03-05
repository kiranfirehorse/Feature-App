package com.learn.featureapp.basicactivityflow;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.learn.featureapp.R;

public class FlowDiagram extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_diagram);
        showToast("In onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        showToast("In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showToast("In onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToast("In onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showToast("In onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showToast("In onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showToast("In onDestroy()");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
