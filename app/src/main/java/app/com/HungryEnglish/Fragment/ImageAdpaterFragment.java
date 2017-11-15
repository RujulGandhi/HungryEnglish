package app.com.HungryEnglish.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.AdapterImageBinding;

/**
 * Created by good on 10/8/2017.
 */
@SuppressLint("ValidFragment")
public class ImageAdpaterFragment extends Fragment {
    private AdapterImageBinding binding;
    ArrayList<String> imageslink, link;
    int pos;

    public ImageAdpaterFragment(ArrayList<String> imageslink, ArrayList<String> link, int pos) {
        this.imageslink = imageslink;
        this.link = link;
        this.pos = pos;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = AdapterImageBinding.inflate(inflater);
        Picasso.with(getContext()).load(imageslink.get(pos)).placeholder(R.drawable.ic_add_img).error(R.drawable.ic_add_img).into(binding.imageAdapter);
        binding.imageAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Utils.makePerfectURL(link.get(pos));
                if (url != null && url.length() > 0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });
        return binding.getRoot();
    }
}
