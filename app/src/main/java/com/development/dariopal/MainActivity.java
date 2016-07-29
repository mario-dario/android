package com.development.dariopal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.development.dariopal.neura_manager.Constants;
import com.development.dariopal.neura_manager.NeuraManager;
import com.development.dariopal.otto.BusManager;
import com.development.dariopal.service.DarioPalService;
import com.neura.sdk.object.Permission;
import com.neura.sdk.service.SubscriptionRequestCallbacks;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.btnStartTrackingService)
    Button btnStartTrackingService;

    @BindView(R.id.btnStopTrackingService)
    Button btnStopTrackingService;

    private Intent darioServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        NeuraManager.getInstance().initNeuraConnection(this);
        updateVisibility();
//        dbManagerInterface = new DBManager(this);
//        DarioDataReceiver.getDarioDataReceiver().start(this, new IDarioDataHandler() {
//            @Override
//            public void onSuccess(ExportDarioLogEntryDataSerializable exportDarioLogEntryDataSerializable) {
//                dbManagerInterface.onSaveToDB(exportDarioLogEntryDataSerializable);
//                originTime = exportDarioLogEntryDataSerializable.getTimeOfEvent();
//            }
//        });
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (dbManagerInterface != null){
//                    List<EventRecord> events =   dbManagerInterface.onGetFromDB(originTime, originTime);
//                    for (EventRecord eventRecord: events){
//                        Toast.makeText(MainActivity.this, eventRecord.getType(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }, 2000);
    }

    private void updateVisibility() {
        if (DarioPalService.isInstanceCreated(this)){
            showStopScreen();
        } else {
            showStartScreen();
        }
    }

    private void showStartScreen() {
        btnStartTrackingService.setVisibility(View.VISIBLE);
        btnStopTrackingService.setVisibility(View.INVISIBLE);
    }

    private void showStopScreen() {
        btnStartTrackingService.setVisibility(View.INVISIBLE);
        btnStopTrackingService.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusManager.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        BusManager.getInstance().unregister(this);
        super.onPause();
    }

//    @Subscribe
//    public void pushArrived(BaseEvent baseEvent){
//        switch (baseEvent.getEventType()){
//            case Const.DB_EVENT:
//                break;
//            case Const.NEURA_PUSH_EVENT: {
//                Toast.makeText(MainActivity.this, "Push received", Toast.LENGTH_SHORT).show();
//                break;
//            }
//
//
//        }
//    }



    @OnClick ({R.id.btnStartTrackingService, R.id.btnStopTrackingService})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartTrackingService:
                manageStartTrackButton();
                break;
            case R.id.btnStopTrackingService:
                manageStopButton();
                break;
        }
    }

    private void manageStopButton() {
        stopService();
        NeuraManager.getInstance().logoutFromNeura();
        updateVisibility();
    }

    private void manageStartTrackButton() {
        if (!NeuraManager.getInstance().isConnected()){
            NeuraManager.getInstance().loginToNeura();
        } else {
            startService();
            showStopScreen();
        }
    }

    public void startService(){
        if (!DarioPalService.isInstanceCreated(this)){
            darioServiceIntent = new Intent(getApplicationContext(), DarioPalService.class);
            this.startService(darioServiceIntent);
        }
    }

    public void stopService(){
        if (DarioPalService.isInstanceCreated(this)){
            this.stopService(darioServiceIntent);
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
            startService();
            updateVisibility();
        } else {
            Toast.makeText(this, "Login Failed, Try Again", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        if (NeuraManager.getInstance().getClient() != null)
            NeuraManager.getInstance().getClient().disconnect();
        super.onDestroy();
    }


}
