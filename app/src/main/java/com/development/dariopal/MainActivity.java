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
import com.neura.standalonesdk.util.SDKUtils;

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
            /**
             * {@link com.neura.standalonesdk.service.NeuraApiClient#registerPushServerApiKey(Activity, String)}
             * isn't mandatory, only if you want Neura to handle events and let you know when
             * an event occurs. Register with your project_id.
             * Further more, please see explanation in
             * {@link FragmentSubscribe#subscribeToFromEvent(String, boolean)}
             */
            getMainActivity().getClient().registerPushServerApiKey(getMainActivity(), getString(R.string.google_api_project_number));
            Log.i(getClass().getSimpleName(), "Successfully logged in with accessToken : "
                    + SDKUtils.extractToken(data));
        } else {
            loadProgress(false);
            mRequestPermissions.setEnabled(true);
        }
    }




}
