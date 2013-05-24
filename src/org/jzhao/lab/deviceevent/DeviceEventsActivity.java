// Author: Jing Zhao (me@jingzhao.org)

package org.jzhao.lab.deviceevent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class DeviceEventsActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_event_list_activity);

        String category = getIntent().getStringExtra("category");
        if( "Calls".equals(category) ) {
            Fragment callLogFragment = new DeviceEventCallLogFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, callLogFragment);
            // transaction.addToBackStack(null);
            transaction.commit();
        } else if ( "Messages".equals(category)) {
            Fragment messageFragment = new DeviceEventMessagesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, messageFragment);
            // transaction.addToBackStack(null);
            transaction.commit();
        } else if ( "Photos".equals(category)) {
            Fragment photoFragment = new DeviceEventPhotosFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, photoFragment);
            // transaction.addToBackStack(null);
            transaction.commit();
        } else if ( "Calendar".equals(category)) {
            Fragment calendarFragment = new DeviceEventCalendarFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, calendarFragment);
            // transaction.addToBackStack(null);
            transaction.commit();
        }
//        String content = launchingIntent.getData().toString();

//        DeviceEventCallLogFragment viewer = (DeviceEventCallLogFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.device_event_list_fragment);

        //viewer.updateUrl(content);
    }

}
