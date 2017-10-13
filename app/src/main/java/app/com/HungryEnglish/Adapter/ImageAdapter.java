package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import app.com.HungryEnglish.Fragment.ImageAdpaterFragment;
import app.com.HungryEnglish.databinding.AdapterImageBinding;

/**
 * Created by good on 10/7/2017.
 */

public class ImageAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    ArrayList<String> imagesArray;
    ArrayList<String> linkArray;
    AdapterImageBinding binding;

    public ImageAdapter(FragmentManager fm, Context context, ArrayList<String> imagesArray, ArrayList<String> linkArray) {
        super(fm);
        mContext = context;
        this.imagesArray = imagesArray;
        this.linkArray = linkArray;
    }

    @Override
    public Fragment getItem(int i) {
        return new ImageAdpaterFragment(imagesArray, linkArray, i);
    }

    @Override
    public int getCount() {
        return imagesArray.size();
    }



}
