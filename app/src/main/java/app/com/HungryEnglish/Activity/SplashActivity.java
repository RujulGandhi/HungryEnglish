package app.com.HungryEnglish.Activity;

import android.os.Bundle;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;

import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Activity.Teacher.MainActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Constant;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Vnnovate on 6/29/2017.
 */

public class SplashActivity extends BaseActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
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
                    startActivity(TeacherListActivity.class);
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
}
