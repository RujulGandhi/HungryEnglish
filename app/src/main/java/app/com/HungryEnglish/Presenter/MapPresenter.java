package app.com.HungryEnglish.Presenter;

import android.content.Context;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.HashMap;

import app.com.HungryEnglish.Model.Address;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RequestParams;
import app.com.HungryEnglish.View.MapView;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by good on 11/14/2017.
 */

public class MapPresenter extends BasePresenter<MapView> implements OnMapReadyCallback {
    private Context context;
    private MapboxMap mapboxMap;

    public MapPresenter(Context context) {
        this.context = context;
    }

    public void getLocation() {
//        GPSTracker gpsTracker = new GPSTracker(context);
//        if (gpsTracker.canGetLocation()) {
//            Location location = gpsTracker.getLocation();
//            if ((location != null)) {
//                Double lat = location.getLatitude();
//                Double lng = location.getLongitude();
//                LatLngBounds latLngBounds = new LatLngBounds.Builder()
//                        .include(new LatLng(lat, lng)).build();
//                mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 2000);
//            } else {
//                Double lat = 23.000000;
//                Double lng = 72.000000;
//                LatLngBounds latLngBounds = new LatLngBounds.Builder()
//                        .include(new LatLng(lat, lng)).build();
//                mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 50), 2000);
//            }
//        } else {
//            getMvpView().showLocationEnable();
//        }
    }

    @Override
    public void attachView(MapView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void onDestroyed() {
        detachView();
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
    }

    public void getCenterLatLng() {
        if (mapboxMap != null) {
            String lat = String.valueOf(mapboxMap.getCameraPosition().target.getLatitude());
            String lng = String.valueOf(mapboxMap.getCameraPosition().target.getLongitude());
            getAddress(lat, lng);
        }
    }

    private void getAddress(final String lat, final String lng) {
        getMvpView().showProgressDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put(RequestParams.ActionName.getValue(), RequestParams.ActionAddress.getValue());
        map.put(RequestParams.Lat.getValue(), String.valueOf(lat));
        map.put(RequestParams.Lng.getValue(), String.valueOf(lng));
        ApiHandler.getApiService().getAddress(map, new retrofit.Callback<Address>() {
            @Override
            public void success(final Address address, Response response) {
                if (address != null) {
                    if (!address.getData().equalsIgnoreCase("false")) {
                        getMvpView().showAddressDialog(address, lat, lng);
                    }
                }
                getMvpView().hideProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                getMvpView().hideProgressDialog();
                getMvpView().showErrorMsg(context.getString(R.string.try_again));
            }
        });
    }
}
