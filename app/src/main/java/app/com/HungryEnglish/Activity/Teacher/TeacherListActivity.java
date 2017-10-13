package app.com.HungryEnglish.Activity.Teacher;

import android.app.Dialog;
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
import app.com.HungryEnglish.Activity.LoginActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Adapter.TeacherListAdapter;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityTeacherListBinding;
import app.com.HungryEnglish.databinding.DialogFilterBinding;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rujul on 7/1/2017.
 */

public class TeacherListActivity extends BaseActivity {

    private TeacherListAdapter teacherListAdapter;
    private List<TeacherListResponse> teacherList, searchTeacherList;
    private ActivityTeacherListBinding binding;
    private DialogFilterBinding dialogBinding;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_list);
        if (read(Constant.SHARED_PREFS.KEY_USER_ROLE).equalsIgnoreCase("student")) {
            callTeacherListApi();
        }
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
        teacherListAdapter = new TeacherListAdapter(getApplicationContext(), teacherList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerTearcherList.setLayoutManager(mLayoutManager);
        binding.recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerTearcherList.setAdapter(teacherListAdapter);
    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("role", "teacher");
        map.put("status", "1");
        map.put("optional_status", "2");
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
        String role = Utils.ReadSharePrefrence(getApplicationContext(), Constant.SHARED_PREFS.KEY_USER_ROLE);
        switch (item.getItemId()) {
            case R.id.logout:
                Utils.ClearSharePrefrence(getApplicationContext());
                startActivity(LoginActivity.class, true);
                finish();
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
                startActivity(Contactus.class);
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
    }

    public void onSubmitFilter(View view) {
        if (dialogBinding != null) {
            String filterString = dialogBinding.skillEdt.getText().toString();
            searchTeacherList = new ArrayList<>();
            for (TeacherListResponse details : teacherList) {
                if (details.getTeacherInfo() != null && details.getTeacherInfo().getSkills().contains(filterString)) {
                    searchTeacherList.add(details);
                }
            }

            if (searchTeacherList.size() > 0) {
                binding.emptyView.setVisibility(View.GONE);
                binding.recyclerTearcherList.setVisibility(View.VISIBLE);
            } else {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.recyclerTearcherList.setVisibility(View.GONE);
            }
            teacherListAdapter = new TeacherListAdapter(getApplicationContext(), searchTeacherList);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            binding.recyclerTearcherList.setLayoutManager(mLayoutManager);
            binding.recyclerTearcherList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerTearcherList.setAdapter(teacherListAdapter);

            dialog.dismiss();
        }
    }

    public void onClearFilter(View view) {
        setTeacherList(teacherList);
        dialog.dismiss();
    }
}
