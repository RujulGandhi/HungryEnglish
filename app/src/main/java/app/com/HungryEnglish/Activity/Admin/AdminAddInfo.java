package app.com.HungryEnglish.Activity.Admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import app.com.HungryEnglish.Adapter.ViewPagerAdapter;
import app.com.HungryEnglish.Fragment.AdminAddInfoStudent;
import app.com.HungryEnglish.Fragment.AdminAddInfoTeacher;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.databinding.ActivityAdminAddInfoBinding;

public class AdminAddInfo extends AppCompatActivity {
    private ActivityAdminAddInfoBinding binding;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_add_info);
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(AdminAddInfoTeacher.newInstance(true), "Teacher");
        adapter.addFragment(AdminAddInfoStudent.newInstance(), "Student");
        viewPager.setAdapter(adapter);
    }
}
