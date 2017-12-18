package app.com.HungryEnglish.Activity.Student;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.FeedbackActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.Adapter.TeacherImageAdapter;
import app.com.HungryEnglish.Adapter.TestAdapter;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.Interface.OnItemClick;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;
import app.com.HungryEnglish.Presenter.StudentHomePresenter;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.View.StudentHomeView;
import app.com.HungryEnglish.databinding.ActivityStudentHomeBinding;

/**
 * Created by Rujul on 11/22/2017.
 */

public class StudentDashboardActivity extends BaseActivity implements StudentHomeView, OnItemClick {
    private ActivityStudentHomeBinding binding;
    private StudentHomePresenter presenter;
    private TestAdapter adapter;
    private Handler handler;
    private int countOfImages;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int curIndex = binding.viewPager.getCurrentItem() + 1;
            if (curIndex == countOfImages) {
                binding.viewPager.setCurrentItem(0);
            } else {
                binding.viewPager.setCurrentItem(curIndex);
            }
            handler.postDelayed(this, 4000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_home);
        presenter = new StudentHomePresenter();
        presenter.attachView(this);
        initUi();
    }

    private void initUi() {
        presenter.getInfoWithTeacherList();
    }


    @Override
    public void showProgressDialog() {
        Utils.showDialog(this);
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
    public void showErrorMsg(int msgId) {
        toast(getString(msgId));
    }

    @Override
    public void showSlider(ArrayList<AdminAddInfoDetail> data) {
        binding.viewPager.setAdapter(new TeacherImageAdapter(this, data));
        binding.tablayout.setupWithViewPager(binding.viewPager);
        countOfImages = data.size();
        handler = new Handler();
        handler.postDelayed(runnable, 4000);
    }

    @Override
    public void showTeacherList(ArrayList<TeacherListResponse> data) {
        adapter = new TestAdapter(getApplicationContext(), data);
        adapter.setListenr(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    public void onMoreTeachers(View view) {
        TeacherListActivity.start(this);
    }

    @Override
    public void showTeacherProfile(String id) {
        TeacherProfileActivity.start(this, id);
    }

    @Override
    public void onItemClick(TeacherListResponse response, int pos) {
        presenter.onItemClick(response);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.contact);
        item.setVisible(true);
        // return true so that the menu pop up is opened
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
                switch (role) {
                    case "student":
                        startActivity(StudentProfileActivity.class);
                        break;
                    case "teacher":
                        startActivity(TeacherProfileActivity.class);
                        break;
                }
                break;
            case R.id.contact:
                FeedbackActivity.start(this);
                break;
        }
        return true;
    }
}
