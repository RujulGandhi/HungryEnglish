package app.com.HungryEnglish.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.databinding.AdapterImageBinding;

import static app.com.HungryEnglish.Util.RestConstant.BASEURL;

/**
 * Created by Rujul on 10/7/2017.
 */

public class TeacherImageAdapter extends PagerAdapter {
    private Context mContext;
    ArrayList<AdminAddInfoDetail> array;
    AdapterImageBinding binding;
    Picasso picasso;

    public TeacherImageAdapter(Context context, ArrayList<AdminAddInfoDetail> array, Picasso picasso) {
        mContext = context;
        this.array = array;
        this.picasso = picasso;
    }

//   @Override
//    public Fragment getItem(int i) {
//        return new ImageAdpaterFragment(imagesArray, linkArray, i);
//    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.adapter_image, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_adapter);
//        imageView.setImageResource(mImageResources[position]);
        final AdminAddInfoDetail info = array.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String url = info.getLink();
                    if (!url.startsWith("http") && !url.startsWith("https")) {
                        url = "http://" + url;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext, "No Browser", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String url = BASEURL+array.get(position).getImages();
        picasso.load(url).placeholder(R.drawable.ic_add_img).error(R.drawable.ic_add_img).into(imageView);
        container.addView(itemView);
        return itemView;
    }


    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


}
