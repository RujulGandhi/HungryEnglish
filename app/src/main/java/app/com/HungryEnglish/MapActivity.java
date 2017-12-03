package app.com.HungryEnglish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Model.Address;
import app.com.HungryEnglish.Presenter.MapPresenter;
import app.com.HungryEnglish.Util.RequestParams;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.View.MapView;
import app.com.HungryEnglish.databinding.ActivityMapBinding;

import static app.com.HungryEnglish.Util.Constant.MAP_BOX;

/**
 * Created by Rujul on 11/9/2017.
 */

public class MapActivity extends BaseActivity implements MapView {
    private ActivityMapBinding binding;
    private MapPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        Mapbox.getInstance(getApplicationContext(), MAP_BOX);
        binding.mapView.onCreate(savedInstanceState);
        presenter = new MapPresenter(getApplicationContext());
        presenter.attachView(this);

        //Center marker image adding
        addMarkerToCenter();

        presenter.getLocation();
        binding.mapView.getMapAsync(presenter);
    }

    private void addMarkerToCenter() {
        ImageView imageView = new ImageView(MapActivity.this);
        imageView.setImageResource(R.drawable.ic_map_marker);
        RelativeLayout.LayoutParams _params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _params.addRule(RelativeLayout.CENTER_VERTICAL);
        _params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(_params);
        binding.rel.addView(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.submit:
                presenter.getCenterLatLng();
//                getAdminAddInfoDetail();
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

//    public void getAdminAddInfoDetail() {
//        if (mapBox != null) {
//            String lat = String.valueOf(mapBox.getCameraPosition().target.getLatitude());
//            String lng = String.valueOf(mapBox.getCameraPosition().target.getLongitude());
//            getAddress(lat, lng);
//        }
//    }
//
//}

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onLocationFound() {
        Log.d("location", "location");
    }

    @Override
    public void showLocationEnable() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void showAddressDialog(final Address address, final String lat, final String lng) {
        final Dialog dialog = new Dialog(MapActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.address_confirm);
        dialog.setCancelable(false);
        TextView tvLogout = (TextView) dialog.findViewById(R.id.address_confirm_msg);
        tvLogout.setText("Do you want to set " + address.getData() + " address as Home location ?");
        TextView tvOkay = (TextView) dialog.findViewById(R.id.address_positive);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.address_negative);
        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("address", address.getData());
                resultIntent.putExtra(RequestParams.Lat.getValue(), String.valueOf(lat));
                resultIntent.putExtra(RequestParams.Lng.getValue(), String.valueOf(lng));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void hideProgressDialog() {
        Utils.dismissDialog();
    }

    @Override
    public void showProgressDialog() {
        Utils.showDialog(this);
    }

    @Override
    public void showErrorMsg(String msg) {
        toast(msg);
    }

    @Override
    public void onBackPressed() {

    }
}
