// Author: Jing Zhao (me@jingzhao.org)

package org.jzhao.lab.deviceevent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class DeviceEventPhotosFragment extends Fragment implements OnItemSelectedListener
{
    // UI component
    private View mFragmentView;
    
    List<String> paths;

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mFragmentView = inflater.inflate(R.layout.device_event_photo_fragment, container, false);
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

    private void setupUI()
    {

        Gallery g = (Gallery) mFragmentView.findViewById(R.id.gallery);
        paths = ReadSDCard();
        g.setAdapter(new ImageAdapter(getActivity()));
        g.setOnItemSelectedListener(this);
    }

    @Override public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {
        // mSwitcher.setImageResource(paths[position]);
    }

    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    public View makeView()
    {
        ImageView i = new ImageView(getActivity());
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return i;
    }

    public class ImageAdapter extends BaseAdapter
    {

        public ImageAdapter(Context c)
        {
            mContext = c;
        }

        public int getCount()
        {
            return paths.size();
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new Gallery.LayoutParams(320, 320));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            Bitmap bm = decodeSampledBitmapFromUri(paths.get(position), 320, 320);

            imageView.setImageBitmap(bm);
            return imageView;
        }

        private Context mContext;

    }

    private List<String> ReadSDCard()
    {
        List<String> tFileList = new ArrayList<String>();

        // It have to be matched with the directory in SDCard
        File rootsd = Environment.getExternalStorageDirectory();
        File dcim = new File(rootsd.getAbsolutePath() + "/DCIM/Camera");

        File[] files = dcim.listFiles();

        if( files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                /* It's assumed that all file in the path are in supported type */
                tFileList.add(file.getPath());
            }
        }
        return tFileList;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight)
    {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

    BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

}
