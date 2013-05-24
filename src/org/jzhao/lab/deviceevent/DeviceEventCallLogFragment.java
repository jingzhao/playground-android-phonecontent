// Author: Jing Zhao (me@jingzhao.org)

package org.jzhao.lab.deviceevent;

import java.util.Date;

import org.jzhao.lab.deviceevent.DeviceEventCategoryFragment.OnEventCategorySelectedListener;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceEventCallLogFragment extends Fragment implements OnItemClickListener{
    
    private OnEventCategorySelectedListener eventCategorySelectedListener;
    
    // UI component
    private ListView mCallHistoryListView = null;
    private SimpleCursorAdapter mCallHistoryListViewAdapter = null;
    private View mFragmentView; 
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
//        String[] links = getResources().getStringArray(R.array.tut_links);
//
//        String content = links[position];
//        eventCategorySelectedListener.onEventCategorySelected(-1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        mFragmentView = inflater.inflate(R.layout.device_event_list_fragment, container, false);
        setupUI();
        return mFragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            eventCategorySelectedListener = (OnEventCategorySelectedListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnEventCategorySelectedListener");
//        }
    }
    
    private String getCallLog()
    {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                dir = "OUTGOING";
                break;

            case CallLog.Calls.INCOMING_TYPE:
                dir = "INCOMING";
                break;

            case CallLog.Calls.MISSED_TYPE:
                dir = "MISSED";
                break;
            }
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
        }
        managedCursor.close();
        return sb.toString();
    }
    
    
    private void setupUI()
    {
        Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        getActivity().startManagingCursor(managedCursor);

        // the desired columns to be bound
        String[] columns = new String[] { CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.DURATION };
        // the XML defined views which the data will be bound to
        int[] to = new int[] { R.id.txt_name, R.id.txt_time, R.id.icon_call_status, R.id.txt_description };

        // create the adapter using the cursor pointing to the desired data as well as the layout information
        mCallHistoryListViewAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_row_callhistory, managedCursor, columns, to);
        mCallHistoryListViewAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder()
        {
            @Override public boolean setViewValue(View view, Cursor cursor, int column)
            {
                int viewId = view.getId();
                String phNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                
                // FIXME: potential performance issue can happen here. One db access for per view
                // The right way is probably get all CallContactRecord related to call history
                // when initializing the fragment (view) and cache it in memory. Then showing
                // each row just need the info already in memory, no db access
                if (viewId == R.id.txt_name) {
                    TextView tvName = (TextView) view;
                    tvName.setText(phNumber);
                    return true;
                } else if (viewId == R.id.txt_description) {
                    TextView tvDescription = (TextView) view;
                    tvDescription.setText(new java.text.SimpleDateFormat("EEE, MM/dd/yyyy, h:mm a").format(callDayTime));
                    return true;
                } else if (viewId == R.id.icon_call_status) {
//                    ImageView imgCallStatus = (ImageView) view;
//                    int callstatus = cursor.getInt(cursor.getColumnIndex("callStatus"));
//                    switch (callstatus) {
//                    case GroupCall.CALLSTATUS_CALLER_DIALED:
//                        imgCallStatus.setImageResource(R.drawable.ic_call_out);
//                        break;
//                    case GroupCall.CALLSTATUS_CALLER_CONNECTED:
//                        imgCallStatus.setImageResource(R.drawable.ic_call_out);
//                        break;
//                    }
                    return true;
                } else if (viewId == R.id.txt_time) { // the column 2 is the date
                    TextView tv = (TextView) view;
                    tv.setText(callDuration + " seconds");
                    return true;
                }
                return false;
            } 
        });

        // set this adapter as your ListActivity's adapter
        mCallHistoryListView = (ListView) mFragmentView.findViewById(R.id.call_list);
        mCallHistoryListView.setAdapter(mCallHistoryListViewAdapter);
        mCallHistoryListView.setOnItemClickListener(this);
    }

}
