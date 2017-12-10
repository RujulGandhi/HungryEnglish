package app.com.HungryEnglish.Activity.Student;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.Adapter.TeacherImageAdapter;
import app.com.HungryEnglish.Adapter.TestAdapter;
import app.com.HungryEnglish.Interface.OnItemClick;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;
import app.com.HungryEnglish.Presenter.StudentHomePresenter;
import app.com.HungryEnglish.R;
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
    public void showSlider(ArrayList<AdminAddInfoDetail> array) {
        Picasso pic = Picasso.with(getApplicationContext());
        binding.viewPager.setAdapter(new TeacherImageAdapter(this, array, pic));
        binding.tablayout.setupWithViewPager(binding.viewPager);
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
}
