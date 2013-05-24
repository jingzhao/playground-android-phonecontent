// Author: Jing Zhao (me@jingzhao.org)

package org.jzhao.lab.deviceevent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DeviceEventCategoryFragment extends ListFragment {
    private OnCategorySelectedListener catSelectedListener;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String[] categories = getResources().getStringArray(R.array.event_category);
        String eventCategory = categories[position];
        catSelectedListener.onCategorySelected(eventCategory);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(ArrayAdapter.createFromResource(getActivity()
                .getApplicationContext(), R.array.event_category,
                R.layout.list_item));
    }

    public interface OnCategorySelectedListener {
        public void onCategorySelected(String tutUrl);
    }
    
    public interface OnEventCategorySelectedListener {
        public void onEventCategorySelected(int eventType);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            catSelectedListener = (OnCategorySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCategorySelectedListener");
        }
    }
}
