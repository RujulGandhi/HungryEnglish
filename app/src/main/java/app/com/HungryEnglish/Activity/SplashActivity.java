package app.com.HungryEnglish.Activity;

import android.os.Bundle;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;

import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Activity.Teacher.MainActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.GPSTracker;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Vnnovate on 6/29/2017.
 */

public class SplashActivity extends BaseActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    private GPSTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        synchronized (this) {
            tracker = new GPSTracker(getApplicationContext());
            if (tracker.canGetLocation()) {
                write("lat", String.valueOf(tracker.getLatitude()));
                write("lng", String.valueOf(tracker.getLongitude()));
            } else {
//            39.913818 , 116.363625
                write("lat", String.valueOf(39.913818));
                write("lng", String.valueOf(116.363625));
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String isActiveStatue = read(Constant.SHARED_PREFS.KEY_IS_ACTIVE);
                String role = read(Constant.SHARED_PREFS.KEY_USER_ROLE);

                if (role.equalsIgnoreCase("")) {
                    startActivity(LoginActivity.class);
                    finish();
                } else if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("0")) {
                    startActivity(StudentProfileActivity.class);
                    finish();
                } else if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("1")) {
                    startActivity(StudentDashboardActivity.class);
                    finish();
                } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("0")) {
                    startActivity(LoginActivity.class);
                    finish();
                } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("1")) {
                    startActivity(TeacherProfileActivity.class);
                    finish();
                } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("2")) {
                    startActivity(MainActivity.class);
                    finish();
                } else if (role.equalsIgnoreCase("admin")) {
                    startActivity(AdminDashboardActivity.class);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);


    }
//
//    @Override
//    public void showGPSDialog() {
//        toast("No GPS enable");
//    }
//
//    @Override
//    public void updateLocatino(Location location) {
//        toast(location.getLatitude() + " : " + location.getLongitude());
//    }
//
//    @Override
//    public void noPermission() {
//        toast("No permission");
//    }
}
