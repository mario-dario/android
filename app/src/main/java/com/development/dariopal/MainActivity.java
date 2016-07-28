package com.development.dariopal;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.development.dariopal.neura_manager.Constants;
import com.development.dariopal.neura_manager.NeuraManager;
import com.neura.sdk.object.AuthenticationRequest;
import com.neura.sdk.object.Permission;
import com.neura.sdk.service.SubscriptionRequestCallbacks;
import com.neura.standalonesdk.util.SDKUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLoginNeura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findVies();
        setListeners();
        NeuraManager.getInstance().initNeuraConnection(this);
    }

    private void setListeners() {
        btnLoginNeura.setOnClickListener(this);
    }

    private void findVies() {
        btnLoginNeura = (Button)findViewById(R.id.btnLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                NeuraManager.getInstance().loginToNeura();
                break;
        }
    }

    /**
     * When calling getMainActivity().getClient().authenticate(NEURA_AUTHENTICATION_REQUEST_CODE, mAuthenticateRequest);
     * the response is received here.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.NEURA_AUTHENTICATION_REQUEST_CODE && resultCode == FragmentActivity.RESULT_OK) {
            ArrayList<Permission> permissions = NeuraManager.getInstance().getPermissions();
            Log.i(getClass().getSimpleName(), "user successfully authenticated with Neura");
            NeuraManager.getInstance().getClient().registerPushServerApiKey(this, getString(R.string.google_api_project_number));
            for (int i = 0; i < permissions.size(); i++) {
                NeuraManager.getInstance().getClient().subscribeToEvent(permissions.get(i).getName(),
                        "YourEventIdentifier_" + permissions.get(i).getName(), true,
                        new SubscriptionRequestCallbacks() {
                            @Override
                            public void onSuccess(String eventName, Bundle bundle, String s1) {
                                Log.i(getClass().getSimpleName(), "Successfully subscribed to event " + eventName);
                            }

                            @Override
                            public void onFailure(String eventName, Bundle bundle, int i) {
                                Log.e(getClass().getSimpleName(), "Failed to subscribe to event " + eventName);
                            }
                        });
            }
        } else {
            Log.e(getClass().getSimpleName(), "user failed to authenticate with Neura");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (NeuraManager.getInstance().getClient() != null)
            NeuraManager.getInstance().getClient().disconnect();
    }




}
