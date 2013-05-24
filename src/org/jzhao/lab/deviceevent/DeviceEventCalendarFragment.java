// Author: Jing Zhao (me@jingzhao.org)

package org.jzhao.lab.deviceevent;

import java.util.Date;
import java.util.HashSet;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class DeviceEventCalendarFragment extends Fragment implements OnItemClickListener
{

    // UI component
    private ListView mCalendarEventsListView = null;
    private SimpleCursorAdapter mCalendarEventsListViewAdapter = null;
    private View mFragmentView;

    @Override public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
    {
        // String[] links = getResources().getStringArray(R.array.tut_links);
        //
        // String content = links[position];
        // eventCategorySelectedListener.onEventCategorySelected(-1);
    }

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mFragmentView = inflater.inflate(R.layout.device_event_list_fragment, container, false);
        setupUI();
        return mFragmentView;
    }

    @Override public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // try {
        // eventCategorySelectedListener = (OnEventCategorySelectedListener) activity;
        // } catch (ClassCastException e) {
        // throw new ClassCastException(activity.toString()
        // + " must implement OnEventCategorySelectedListener");
        // }
    }

    public static void readCalendar(Context context)
    {

        ContentResolver contentResolver = context.getContentResolver();

        // Fetch a list of all calendars synced with the device, their display names and whether the
        // user has them selected for display.

        final Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar"), (new String[] { "_id", "displayName", "selected" }), null, null, null);
        // For a full list of available columns see http://tinyurl.com/yfbg76w

        HashSet<String> calendarIds = new HashSet<String>();

        while (cursor.moveToNext()) {

            final String _id = cursor.getString(0);
            final String displayName = cursor.getString(1);
            final Boolean selected = !cursor.getString(2).equals("0");

            System.out.println("Id: " + _id + " Display Name: " + displayName + " Selected: " + selected);
            calendarIds.add(_id);
        }

        // For each calendar, display all the events from the previous week to the end of next week.
        for (String id : calendarIds) {
            Uri.Builder builder = Uri.parse("content://calendar/instances/when").buildUpon();
            long now = new Date().getTime();
            ContentUris.appendId(builder, now - DateUtils.WEEK_IN_MILLIS);
            ContentUris.appendId(builder, now + DateUtils.WEEK_IN_MILLIS);

            Cursor eventCursor = contentResolver.query(builder.build(), new String[] { "title", "begin", "end", "allDay" }, "Calendars._id=" + id, null, "startDay ASC, startMinute ASC");
            // For a full list of available columns see http://tinyurl.com/yfbg76w

            while (eventCursor.moveToNext()) {
                final String title = eventCursor.getString(0);
                final Date begin = new Date(eventCursor.getLong(1));
                final Date end = new Date(eventCursor.getLong(2));
                final Boolean allDay = !eventCursor.getString(3).equals("0");

                System.out.println("Title: " + title + " Begin: " + begin + " End: " + end + " All Day: " + allDay);
            }
        }
    }

    private void setupUI()
    {
        

        ContentResolver contentResolver = getActivity().getContentResolver();

        // Fetch a list of all calendars synced with the device, their display names and whether the
        // user has them selected for display.

        final Cursor cursor = contentResolver.query(Uri.parse("content://com.android.calendar/calendars"), (new String[] { "_id" }), null, null, null);
        HashSet<String> calendarIds = new HashSet<String>();

        while (cursor.moveToNext()) {

            final String _id = cursor.getString(0);

            System.out.println("Id: " + _id);
            calendarIds.add(_id);
        }
        
        // For each calendar, display all the events from the previous week to the end of next week.
        if (calendarIds.size() > 0) {
            String id = (String) calendarIds.toArray()[calendarIds.size()-1];
            Uri.Builder builder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
            long now = new Date().getTime();
            ContentUris.appendId(builder, now - DateUtils.WEEK_IN_MILLIS);
            ContentUris.appendId(builder, now + DateUtils.WEEK_IN_MILLIS);

            Cursor managedCursor = contentResolver.query(builder.build(), null, null, null, "startDay ASC, startMinute ASC");
            // For a full list of available columns see http://tinyurl.com/yfbg76w
            getActivity().startManagingCursor(managedCursor);

            // the desired columns to be bound
            String[] columns = new String[] { "title", "begin", "end" };
            // the XML defined views which the data will be bound to
            int[] to = new int[] { R.id.event_title, R.id.event_time_start, R.id.event_time_end };

            // create the adapter using the cursor pointing to the desired data as well as the layout information
            mCalendarEventsListViewAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_row_calendar_event, managedCursor, columns, to);
            mCalendarEventsListViewAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder()
            {
                @Override public boolean setViewValue(View view, Cursor cursor, int column)
                {
                    int viewId = view.getId();
                    String eventTitle = cursor.getString(cursor.getColumnIndex("title"));
                    String eventStart = cursor.getString(cursor.getColumnIndex("begin"));
                    String eventEnd = cursor.getString(cursor.getColumnIndex("end"));

                    if (viewId == R.id.event_title) {
                        TextView tvTitle = (TextView) view;
                        tvTitle.setText(eventTitle);
                        return true;
                    } else if (viewId == R.id.event_time_start) {
                        TextView tvStart = (TextView) view;
                        Date time = new Date(Long.valueOf(eventStart));
                        tvStart.setText(new java.text.SimpleDateFormat("h:mm a").format(time));

                        return true;
                    } else if (viewId == R.id.event_time_end) {
                        TextView tvEnd = (TextView) view;
                        Date time = new Date(Long.valueOf(eventEnd));
                        tvEnd.setText(new java.text.SimpleDateFormat("h:mm a, EEE, MM/dd/yyyy").format(time));
                        return true;
                    }
                    return false;
                }
            });

            // set this adapter as your ListActivity's adapter
            mCalendarEventsListView = (ListView) mFragmentView.findViewById(R.id.call_list);
            mCalendarEventsListView.setAdapter(mCalendarEventsListViewAdapter);
            mCalendarEventsListView.setOnItemClickListener(this);
        }
        
    }

}
