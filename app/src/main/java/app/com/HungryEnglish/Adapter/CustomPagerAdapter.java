package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.HungryEnglish.R;
import app.com.HungryEnglish.databinding.AdapterImageBinding;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    ArrayList<String> imagesArray;
    ArrayList<String> linkArray;
    AdapterImageBinding binding;

    public CustomPagerAdapter(Context context, ArrayList<String> imagesArray, ArrayList<String> linkArray) {
        mContext = context;
        this.imagesArray = imagesArray;
        this.linkArray = linkArray;
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = AdapterImageBinding.inflate(inflater);
        Picasso.with(mContext).load(imagesArray.get(position)).placeholder(R.drawable.ic_add_img).into(binding.imageAdapter);
        collection.addView(binding.imageAdapter);
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return imagesArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    /**
     *
     Context context;
     int images[];
     LayoutInflater layoutInflater;

     public MyCustomPagerAdapter(Context context, int images[]) {
     this.context = context;
     this.images = images;
     layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     }

     @Override
     public int getCount() {
     return images.length;
     }

     @Override
     public boolean isViewFromObject(View view, Object object) {
     return view == ((LinearLayout) object);
     }

     @Override
     public Object instantiateItem(ViewGroup container, final int position) {
     View itemView = layoutInflater.inflate(R.layout.item, container, false);

     ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
     imageView.setImageResource(images[position]);

     container.addView(itemView);

     //listening to image click
     imageView.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
     Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
     }
     });

     return itemView;
     }

     @Override
     public void destroyItem(ViewGroup container, int position, Object object) {
     container.removeView((LinearLayout) object);
     }
     */


}