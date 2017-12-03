package app.com.HungryEnglish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import app.com.HungryEnglish.Activity.TimePickerActivity;
import app.com.HungryEnglish.databinding.ActivityMain2Binding;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class Main2Activity extends AppCompatActivity {
    private static String file_url = "https://d3fildg3jlcvty.cloudfront.net/0b32c08c472b56d27189347345b320a05bfb25d2/graphics/App-Store-Icon_200x200.png";
    Button btnShowProgress;
    private ProgressDialog pDialog;
    ImageView my_image;
    public static final int progress_bar_type = 0;
    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);
    }


    public void startTimePicker(View view) {

// Check if GPS is available
        if (SmartLocation.with(this).location().state().isGpsAvailable()) {
            SmartLocation.with(this).location()
                    .oneFix()
                    .start(new OnLocationUpdatedListener() {

                        @Override
                        public void onLocationUpdated(Location location) {
                            binding.btnProgressBar.setText("Location = " + location.getLatitude() + " : " + location.getLongitude());
                        }
                    });
        } else {
            binding.btnProgressBar.setText("Gps is not availble");
        }

//        Intent in = new Intent(this, TimePickerActivity.class);
//        startActivityForResult(in, 100);

    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100) {
//            binding.btnProgressBar.setText(data.getExtras().getString("availabletime"));
//            binding.btnProgressBar.setTag(data.getExtras().getString("availabletime"));
//        }
//    }
}
