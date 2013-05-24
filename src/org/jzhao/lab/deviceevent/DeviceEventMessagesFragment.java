// Author: Jing Zhao (me@jingzhao.org)

package org.jzhao.lab.deviceevent;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceEventMessagesFragment extends Fragment implements OnItemClickListener{
        
    // UI component
    private ListView mMessagesListView = null;
    private SimpleCursorAdapter mMessagesListViewAdapter = null;
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
    
    
    private void setupUI()
    {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor managedCursor = getActivity().managedQuery(uri, null, null, null, null);
        getActivity().startManagingCursor(managedCursor);

        // the desired columns to be bound
        String[] columns = new String[] { "body", "address"};
        // the XML defined views which the data will be bound to
        int[] to = new int[] { R.id.message_body, R.id.sender_address };

        // create the adapter using the cursor pointing to the desired data as well as the layout information
        mMessagesListViewAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_row_message, managedCursor, columns, to);
        mMessagesListViewAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder()
        {
            @Override public boolean setViewValue(View view, Cursor cursor, int column)
            {
                int viewId = view.getId();
                String phNumber = cursor.getString(cursor.getColumnIndex("address"));
                String messageBody = cursor.getString(cursor.getColumnIndex("body"));
                
                if (viewId == R.id.message_body) {
                    TextView tvBody = (TextView) view;
                    tvBody.setText(messageBody);
                    return true;
                } else if (viewId == R.id.phone_number) {
                    TextView tvDescription = (TextView) view;
                    tvDescription.setText(phNumber);
                    return true;
                }
                return false;
            } 
        });

        // set this adapter as your ListActivity's adapter
        mMessagesListView = (ListView) mFragmentView.findViewById(R.id.call_list);
        mMessagesListView.setAdapter(mMessagesListViewAdapter);
        mMessagesListView.setOnItemClickListener(this);
    }

}
