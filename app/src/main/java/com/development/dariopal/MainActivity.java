package com.development.dariopal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.development.dariopal.database.DBManagerInterface;
import com.development.dariopal.neura_manager.Constants;
import com.development.dariopal.neura_manager.NeuraManager;
import com.development.dariopal.otto.BusManager;
import com.development.dariopal.otto.Const;
import com.development.dariopal.otto.events.BaseEvent;
import com.development.dariopal.service.DarioPalService;
import com.neura.sdk.object.Permission;
import com.neura.sdk.service.SubscriptionRequestCallbacks;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Long originTime;
    Context context;
    Button btnLoginNeura;
    Intent darioServiceIntent ;
    private DBManagerInterface dbManagerInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        darioServiceIntent = new Intent(getApplicationContext(), DarioPalService.class);

        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
        context = this;
        NeuraManager.getInstance().initNeuraConnection(this);
        context.startService(darioServiceIntent);
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

    @Subscribe
    public void pushArrived(BaseEvent baseEvent){
        switch (baseEvent.getEventType()){
            case Const.DB_EVENT:
                break;
            case Const.NEURA_PUSH_EVENT: {
                Toast.makeText(MainActivity.this, "Push received", Toast.LENGTH_SHORT).show();
                break;
            }


        }
    }

    private void setListeners() {
        btnLoginNeura.setOnClickListener(this);
    }

    private void findViews() {
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

        context.stopService(darioServiceIntent);
    }


}
