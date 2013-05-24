// Author: Jing Zhao (me@jingzhao.org)

package org.jzhao.lab.deviceevent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements DeviceEventCategoryFragment.OnCategorySelectedListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_event_cat_activity);
        
        if( findViewById(R.id.fragment_container) != null) {
            Fragment callLogfragment = new DeviceEventCallLogFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, callLogfragment, "defaultRightFragment");
            fragmentTransaction.commit(); 
        }

    }

    @Override
    public void onCategorySelected (String category) {
        DeviceEventCallLogFragment viewer = (DeviceEventCallLogFragment) getSupportFragmentManager()
                .findFragmentByTag("defaultRightFragment");
    
        if (viewer == null || !viewer.isInLayout()) {
            Intent showContent = new Intent(getApplicationContext(),
                    DeviceEventsActivity.class);
            // Pass in category selected
            showContent.putExtra("category", category);
            startActivity(showContent);
        } else {
            // TODO: Compare with the current fragment and see if it is already showing
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
        }
    }
}