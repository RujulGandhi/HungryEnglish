package app.com.HungryEnglish.Activity.Teacher;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.FeedbackActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Fragment.AdminAddInfoTeacher;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, AdminAddInfoTeacher.newInstance()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        // return true so that the menu pop up is opened

        MenuItem item = (MenuItem) menu.findItem(R.id.contact);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String role = Utils.ReadSharePrefrence(getApplicationContext(), RestConstant.SHARED_PREFS.KEY_USER_ROLE);
        switch (item.getItemId()) {
            case R.id.logout:

                Utils.alert(this, getString(R.string.logout), getString(R.string.logout_note), getString(R.string.logout), getString(R.string.cancel), new OnDialogEvent() {
                    @Override
                    public void onPositivePressed() {
                        clear();
                        Utils.logout(getApplicationContext());
                        finish();
                    }

                    @Override
                    public void onNegativePressed() {

                    }
                });
                break;
            case R.id.profile:
                if (role.equalsIgnoreCase("student"))
                    startActivity(StudentProfileActivity.class);

                else if (role.equalsIgnoreCase("teacher"))
                    startActivity(TeacherProfileActivity.class);
                break;

            case R.id.contact:
                FeedbackActivity.start(this);
                break;
        }
        return true;
    }

}

