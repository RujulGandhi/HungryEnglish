package app.com.HungryEnglish.Activity.Teacher;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.FeedbackActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Adapter.TeacherListAdapter;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityTeacherListBinding;
import app.com.HungryEnglish.databinding.DialogFilterBinding;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rujul on 7/1/2017.
 */

public class TeacherListActivity extends BaseActivity {
    private String filterString;
    private TeacherListAdapter teacherListAdapter;
    private List<TeacherListResponse> teacherList, searchTeacherList;
    private ActivityTeacherListBinding binding;
    private DialogFilterBinding dialogBinding;
    private Dialog dialog;
    private ArrayList<String> daysFilter;

    public static void start(Context context) {
        Intent intent = new Intent(context, TeacherListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (read(RestConstant.SHARED_PREFS.KEY_USER_ROLE).equalsIgnoreCase("student")) {
            callTeacherListApi();
        }
        daysFilter = new ArrayList<>();
    }


    // CALL TEACHER LIST API HERE
    private void callTeacherListApi() {
        if (!Utils.checkNetwork(getApplicationContext())) {
            Utils.showCustomDialog(getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        } else {
            Utils.showDialog(this);
            ApiHandler.getApiService().getTeacherList(getTeacherDetail(), new retrofit.Callback<TeacherListMainResponse>() {
                @Override
                public void success(TeacherListMainResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherListMainResponse.getStatus() == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("false")) {
                        toast(teacherListMainResponse.getMessage());
                        return;
                    }
                    if (teacherListMainResponse.getStatus().equals("true")) {
                        setTeacherList(teacherListMainResponse.getData());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    toast(getString(R.string.something_wrong));
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerTearcherList.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setTeacherList(List<TeacherListResponse> teacherListMainResponse) {
        teacherList = new ArrayList<TeacherListResponse>();
        teacherList = teacherListMainResponse;

        if (teacherList.size() > 0) {
            binding.emptyView.setVisibility(View.GONE);
            binding.recyclerTearcherList.setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.recyclerTearcherList.setVisibility(View.GONE);
        }
        teacherListAdapter = new TeacherListAdapter(TeacherListActivity.this, teacherList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(TeacherListActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerTearcherList.setLayoutManager(mLayoutManager);
        binding.recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerTearcherList.setAdapter(teacherListAdapter);
    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "teacher");
        map.put("status", "2");
        return map;
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

    public void filterPopup(View view) {
        dialog = new Dialog(this);
        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_filter, null, false);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(true);
        dialog.show();

        dialogBinding.skillEdt.setText(filterString);
        if (daysFilter.contains("Mon")) {
            dialogBinding.dayMon.setSelected(true);
        }
        if (daysFilter.contains("Tue")) {
            dialogBinding.dayTue.setSelected(true);
        }
        if (daysFilter.contains("Wed")) {
            dialogBinding.dayWen.setSelected(true);
        }
        if (daysFilter.contains("Thu")) {
            dialogBinding.dayThu.setSelected(true);
        }
        if (daysFilter.contains("Fri")) {
            dialogBinding.dayFri.setSelected(true);
        }
        if (daysFilter.contains("Sat")) {
            dialogBinding.daySat.setSelected(true);
        }
        if (daysFilter.contains("Sun")) {
            dialogBinding.daySun.setSelected(true);
        }
    }

    public void onSubmitFilter(View view) {
        if (dialogBinding != null) {
            filterString = dialogBinding.skillEdt.getText().toString();
            searchTeacherList = new ArrayList<>();
            if (filterString.length() > 0) {
                searchTeacherList = getSearchBySkill(teacherList, filterString);
                if (daysFilter.size() > 0)
                    searchTeacherList = getSearchByDays(searchTeacherList, daysFilter);
            } else {
                if (daysFilter.size() > 0)
                    searchTeacherList = getSearchByDays(teacherList, daysFilter);
                else
                    searchTeacherList = teacherList;
            }

            if (searchTeacherList.size() > 0) {
                binding.emptyView.setVisibility(View.GONE);
                binding.recyclerTearcherList.setVisibility(View.VISIBLE);
            } else {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.recyclerTearcherList.setVisibility(View.GONE);
            }

            teacherListAdapter = new TeacherListAdapter(TeacherListActivity.this, searchTeacherList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            binding.recyclerTearcherList.setLayoutManager(mLayoutManager);
            binding.recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerTearcherList.setAdapter(teacherListAdapter);

            dialog.dismiss();
        }
    }

    public void onClearFilter(View view) {
        daysFilter.clear();
        filterString = "";
        setTeacherList(teacherList);
        dialog.dismiss();
    }

    public void onSelect(View view) {
        if (view.isSelected()) {
            view.setSelected(false);
            int index = daysFilter.indexOf(view.getTag().toString());
            daysFilter.remove(index);
        } else {
            view.setSelected(true);
            if (!daysFilter.contains(view.getTag().toString()))
                daysFilter.add(view.getTag().toString());
        }
    }


    public ArrayList<TeacherListResponse> getSearchBySkill(List<TeacherListResponse> array, String skill) {
        ArrayList<TeacherListResponse> searchArray = new ArrayList<>();
        for (TeacherListResponse details : array) {
            if (details.getTeacherInfo() != null &&
                    details.getTeacherInfo().getSkills() != null &&
                    details.getTeacherInfo().getSkills().toLowerCase().contains(filterString.toLowerCase())) {
                searchArray.add(details);
            }
        }
        return searchArray;
    }


    public ArrayList<TeacherListResponse> getSearchByDays(List<TeacherListResponse> array, ArrayList<String> daysFilter) {
        ArrayList<TeacherListResponse> searchArray = new ArrayList<>();
        for (TeacherListResponse details : array) {
            if (this.daysFilter.size() != 0) {
                for (String days : this.daysFilter) {
                    if (details.getTeacherInfo().getAvailableTime().contains(days)) {
                        searchArray.add(details);
                    }
                }
            } else {
                searchArray.add(details);
            }
        }
        return searchArray;
    }
}
