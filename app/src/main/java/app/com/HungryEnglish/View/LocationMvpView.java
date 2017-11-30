package app.com.HungryEnglish.View;

import android.location.Location;

/**
 * Created by Rujul on 11/19/2017.
 */

public interface LocationMvpView extends MvpView {

    void showGPSDialog();

    void updateLocatino(Location location);

    void noPermission();
}
