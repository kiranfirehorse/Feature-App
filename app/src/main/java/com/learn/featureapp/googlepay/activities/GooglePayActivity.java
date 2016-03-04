package com.learn.featureapp.googlepay.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.learn.featureapp.R;
import com.learn.featureapp.googlepay.util.IabBroadcastReceiver;
import com.learn.featureapp.googlepay.util.IabHelper;
import com.learn.featureapp.googlepay.util.IabHelper.OnIabPurchaseFinishedListener;
import com.learn.featureapp.googlepay.util.IabHelper.QueryInventoryFinishedListener;
import com.learn.featureapp.googlepay.util.IabResult;
import com.learn.featureapp.googlepay.util.Inventory;
import com.learn.featureapp.googlepay.util.Purchase;

import static com.learn.featureapp.googlepay.util.IabBroadcastReceiver.IabBroadcastListener;

public class GooglePayActivity extends Activity implements IabBroadcastListener {

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;
    private static final String TAG = "Google Pay";
    private EditText purchaseItemTextView;
    private String purchasedSku = "android.test.purchased";
    private String nonPurchasedSku = "android.test.item_unavailable";
    private String cancelledSku = "android.test.item_unavailable";
    private String refundedSku = "android.test.item_unavailable";


    private IabHelper mHelper;
    private IabBroadcastReceiver mBroadcastReceiver;
    private boolean isPurchased = false;

    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) {
                return;
            }
            if (result.isSuccess()) {
                notifyUser(TAG, "Consumption successful. Provisioning.");
            } else {
                notifyUser(TAG, "Error while consuming: " + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };

    private QueryInventoryFinishedListener mGotInventoryListener = new QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (mHelper == null) {
                notifyUser(TAG, "we been disposed of in the meantime?");
                return;
            }
            if (result.isFailure()) {
                notifyUser(TAG, "Failed to query inventory: " + result);
                return;
            }
            notifyUser(TAG, "Query inventory was successful.");
            //Checking for subscriptions
            Purchase samplePurchase = inventory.getPurchase(purchasedSku);
            isPurchased = (samplePurchase != null && verifyDeveloperPayload(samplePurchase));
            Toast.makeText(getApplicationContext(), (isPurchased ? "you have purchased" : "You haven't purchased"), Toast.LENGTH_SHORT).show();
        }
    };
    private OnIabPurchaseFinishedListener mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            notifyUser(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                showAlert("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                showAlert("Error purchasing. Authenticity verification failed.");
                return;
            }
            notifyUser(TAG, "Purchase successful.");

            if (purchase.getSku().equals(purchasedSku)) {
                Log.d(TAG, "Purchase is done. Starting consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };

    private void notifyUser(String tag, String message) {
        Log.d(tag, message);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean verifyDeveloperPayload(Purchase purchase) {
        String payload = purchase.getDeveloperPayload();
        /*TODO Add code to compare the payload to compare the object*/
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_pay);
        purchaseItemTextView = (EditText) findViewById(R.id.product_id_field);

        String base64EncodedPublicKey = getString(R.string.google_api_key);
        initializeHelperClass(base64EncodedPublicKey);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeHelperClass(String publicKey) {
        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, publicKey);
        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);
        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                if (!result.isSuccess()) {
                    showAlert("Problem setting up in-app billing: " + result);
                    return;
                }
                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) {
                    notifyUser(TAG, "we been disposed of in the meantime?");
                    return;
                }
                mBroadcastReceiver = new IabBroadcastReceiver(GooglePayActivity.this);
            }
        });
    }

    private void showAlert(String s) {
        Log.e(TAG, "**** Google Pay Error: " + s);
        alert("Error: " + s);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        mHelper.queryInventoryAsync(mGotInventoryListener);
    }

    public void onClickSubmit(View view) {
        String itemSku = purchaseItemTextView.getText().toString();
        if (!isPurchased) {
            String payload = "PAYLOAD";
            mHelper.launchPurchaseFlow(this, itemSku, RC_REQUEST, mPurchaseFinishedListener, payload);
        } else {
            notifyUser(TAG, "You already have purchased");
        }
    }
    

    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }
}
