package com.development.dariopal;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.development.dariopal.neura_manager.NeuraEventConstants;
import com.development.dariopal.neura_manager.NeuraManager;
import com.development.dariopal.otto.BusManager;
import com.development.dariopal.service.DarioPalService;
import com.neura.sdk.config.NeuraConsts;
import com.neura.sdk.object.Permission;
import com.neura.sdk.service.SubscriptionRequestCallbacks;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnStartTrackingService;
    Button btnStopTrackingService;
    ImageView imgMain;
    ImageView imgBotomRight;
    ImageView imgBottomLeft;

    private Intent darioServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setListeners();
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

    private void setListeners() {
        btnStartTrackingService.setOnClickListener(this);
        btnStopTrackingService.setOnClickListener(this);
    }

    private void bindViews() {
        btnStartTrackingService = (Button)findViewById(R.id.btnStartTrackingService);
        btnStopTrackingService = (Button)findViewById(R.id.btnStopTrackingService);
        imgMain = (ImageView)findViewById(R.id.main_image);
        imgBotomRight = (ImageView)findViewById(R.id.right_bottom_image);
        imgBottomLeft = (ImageView)findViewById(R.id.left_bottom_image);
    }

    private void updateVisibility() {
        if (isServiceRunning()) {
            showStopScreen();
        } else {
            showStartScreen();
        }
    }

    private void showStartScreen() {
        imgMain.setImageResource(R.drawable.off);
        btnStartTrackingService.setVisibility(View.VISIBLE);
        btnStopTrackingService.setVisibility(View.INVISIBLE);
    }

    private void showStopScreen() {
        imgMain.setImageResource(R.drawable.on);
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
        NeuraManager.getInstance().logoutFromNeura(new NeuraManager.NeuraManagerCallbacks() {
            @Override
            public void onLogoutSuccess(boolean isSuccess) {
                updateVisibility();
            }
        });
    }

    private void manageStartTrackButton() {
        if (!NeuraManager.getInstance().isConnected()) {
            NeuraManager.getInstance().loginToNeura();
        } else {
            startService();
            showStopScreen();
        }
    }

    public void startService() {
        if (!isServiceRunning()) {
            darioServiceIntent = new Intent(getApplicationContext(), DarioPalService.class);
            this.startService(darioServiceIntent);
        }
    }

    public void stopService() {
        if (isServiceRunning()) {
            if (darioServiceIntent == null){
                darioServiceIntent = new Intent(getApplicationContext(), DarioPalService.class);
            }
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
        if (requestCode == NeuraEventConstants.NEURA_AUTHENTICATION_REQUEST_CODE && resultCode == FragmentActivity.RESULT_OK) {
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

    public boolean isServiceRunning() {

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            Log.d("Check Running Services", runningServiceInfo.service.getClassName());
            if (runningServiceInfo.service.getClassName().equals(DarioPalService.class.getName())) {
                Log.d("Check Running Services", runningServiceInfo.getClass().getSimpleName());
                return true;
            }
        }
        return false;
    }


}
