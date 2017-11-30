package app.com.HungryEnglish.View;

import app.com.HungryEnglish.Model.Address;

/**
 * Created by Rujul on 11/14/2017.
 */

public interface MapView extends MvpView {
    void onLocationFound();

    void showLocationEnable();

    void showAddressDialog(Address address, String lat, String lng);

    void hideProgressDialog();

    void showProgressDialog();

    void showErrorMsg(String msg);
}
