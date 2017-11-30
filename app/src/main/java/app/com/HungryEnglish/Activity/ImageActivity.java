package app.com.HungryEnglish.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import app.com.HungryEnglish.R;
import app.com.HungryEnglish.databinding.ActivityImageBinding;

/**
 * Created by good on 11/28/2017.
 */

public class ImageActivity extends BaseActivity {
    private ActivityImageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (intent.hasExtra("bitmap")) {
            Bitmap bm = (Bitmap) intent.getParcelableExtra("bitmap");
            binding.image.setImageBitmap(bm);
        } else {
            binding.image.setImageResource(R.drawable.ic_add_img);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
