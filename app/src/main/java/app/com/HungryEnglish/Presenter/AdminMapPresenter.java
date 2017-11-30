package app.com.HungryEnglish.Presenter;

import android.content.Context;
import android.location.Location;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import app.com.HungryEnglish.Model.admin.UserBasicInfo;
import app.com.HungryEnglish.Model.admin.UserListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.View.AdminMapView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rujul on 11/16/2017.
 */

public class AdminMapPresenter extends BasePresenter<AdminMapView> implements OnMapReadyCallback {
    private Context context;
    private MapboxMap mapboxMap;
    private boolean isStudentSwitchOn = true, isTeacherSwitchOn = true;
    public UserListResponse res;

    public AdminMapPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.getUiSettings().setAttributionEnabled(false);
        getList();
    }

    public void getList() {
        getMvpView().showProgressDialog();

        ApiHandler.getApiService().getUserList(new Callback<UserListResponse>() {
            @Override
            public void success(UserListResponse basicResponse, Response response) {
                res = basicResponse;
                addMakerToMap();
                getMvpView().hideProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                getMvpView().hideProgressDialog();
            }
        });

    }

    private void addMakerToMap() {
        //Creating a list of markers
        if (mapboxMap != null) {
            mapboxMap.removeAnnotations();
            if (res.getData().size() > 0) {
                for (UserBasicInfo info : res.getData()) {
                    if (info.getRole().equalsIgnoreCase("student")) {
                        if (isStudentSwitchOn) {
                            setMarker(info);
                        }
                    }
                    if (info.getRole().equalsIgnoreCase("teacher")) {
                        if (isTeacherSwitchOn) {
                            setMarker(info);
                        }
                    }
                }
            } else {
                getMvpView().noRecordFound();
            }
        }
    }

    private void setMarker(UserBasicInfo info) {
        if (info.getLatitude() != null && info.getLatitude().length() > 0) {
            LatLng latLng = new LatLng(Double.parseDouble(info.getLatitude()), Double.parseDouble(info.getLongitude()));
            //Filling up the list
            Icon icon;
            if (info.getRole().equalsIgnoreCase("student")) {
                icon = IconFactory.getInstance(context).fromResource(R.drawable.ic_map_blue_marker);
            } else {
                icon = IconFactory.getInstance(context).fromResource(R.drawable.ic_map_marker);
            }


            MarkerOptions option = new MarkerOptions()
                    .position(latLng)
                    .title(info.getFullName()).icon(icon)
                    .snippet(info.getRole());                //Adding all the markers to the map
            mapboxMap.addMarker(option);
        }
    }

    public void setStudent(boolean student) {
        this.isStudentSwitchOn = student;
        addMakerToMap();
    }

    public void setTeacher(boolean teacher) {
        this.isTeacherSwitchOn = teacher;
        addMakerToMap();
    }

    public void zoomOn(Location location) {
        if (mapboxMap != null) {

        }
    }
}
