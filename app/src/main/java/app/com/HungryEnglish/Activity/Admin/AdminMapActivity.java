package app.com.HungryEnglish.Activity.Admin;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.Mapbox;
import com.suke.widget.SwitchButton;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.Presenter.AdminMapPresenter;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.MyGpsTracker;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.View.AdminMapView;
import app.com.HungryEnglish.View.LocationMvpView;
import app.com.HungryEnglish.databinding.ActivityAdminMapBinding;

import static app.com.HungryEnglish.Util.RestConstant.MAP_BOX;

/**
 * Created by Rujul on 11/16/2017.
 */

public class AdminMapActivity extends BaseActivity implements AdminMapView, SwitchButton.OnCheckedChangeListener, LocationMvpView {
    private ActivityAdminMapBinding binding;
    private AdminMapPresenter presenter;
    private MyGpsTracker gpsPresenter;

    public static void start(Context context) {
        Intent in = new Intent(context, AdminMapActivity.class);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_map);
        Mapbox.getInstance(getApplicationContext(), MAP_BOX);
        binding.mapView.onCreate(savedInstanceState);
        presenter = new AdminMapPresenter(this);
        gpsPresenter = new MyGpsTracker(this);
        presenter.attachView(this);
        gpsPresenter.attachView(this);
        initUi();
    }

    private void initUi() {
        binding.mapView.getMapAsync(presenter);

        // register switches
        binding.switchStudent.setOnCheckedChangeListener(this);
        binding.switchTeacher.setOnCheckedChangeListener(this);
        // get location
        gpsPresenter.getLocation();
    }

    @Override
    public void hideProgressDialog() {
        Utils.dismissDialog();
    }

    @Override
    public void showErrorMsg(String msg) {
        toast(msg);
    }

    @Override
    public void showProgressDialog() {
        Utils.showDialog(this);
    }

    @Override
    public void noRecordFound() {
        toast(R.string.no_data_found);
    }

    @Override
    public void onCheckedChanged(SwitchButton view, final boolean isChecked) {
        if (view.getId() == R.id.switch_teacher) {
            presenter.setTeacher(isChecked);
        } else if (view.getId() == R.id.switch_student) {
            presenter.setStudent(isChecked);
        }
    }

    @Override
    public void showGPSDialog() {
        Utils.alert(AdminMapActivity.this, getString(R.string.gps_enable), getString(R.string.gps_enable_des), getString(R.string.open_setting), getString(R.string.cancel), new OnDialogEvent() {
            @Override
            public void onPositivePressed() {
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }

            @Override
            public void onNegativePressed() {
            }
        });
    }

    @Override
    public void updateLocatino(Location location) {
        presenter.zoomOn(location);
    }

    @Override
    public void noPermission() {

    }
}
