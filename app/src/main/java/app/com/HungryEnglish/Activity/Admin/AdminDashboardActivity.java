package app.com.HungryEnglish.Activity.Admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.Student.StudentListActivity;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.Model.admin.CountListMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.Utils;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by R'jul on 7/14/2017.
 */

public class AdminDashboardActivity extends BaseActivity {

    RelativeLayout llStudentList, llTeacherList, llAddImageOrLink, llreport;

    AlphaAnimation click;
    int teacherCount = 0, studentCount = 0;
    private TextView tvTeacherCountAdmin, tvStudentCountAdmin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        click = new AlphaAnimation(1F, 0.5F);
        setContentView(R.layout.admin_dashboard_activity);
        idMapping();

        setOnClick();

        callGetCountApi();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String role = Utils.ReadSharePrefrence(AdminDashboardActivity.this, RestConstant.SHARED_PREFS.KEY_USER_ROLE);
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
        }
        return true;
    }

    private void idMapping() {
        llStudentList = (RelativeLayout) findViewById(R.id.llStudentList);
        llTeacherList = (RelativeLayout) findViewById(R.id.llTeacherList);
        llAddImageOrLink = (RelativeLayout) findViewById(R.id.llAddImageOrLink);
        llreport = (RelativeLayout) findViewById(R.id.llreport);
        tvTeacherCountAdmin = (TextView) findViewById(R.id.tvTeacherCountAdmin);
        tvStudentCountAdmin = (TextView) findViewById(R.id.tvStudentCountAdmin);
    }

    private void setOnClick() {
        llStudentList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(StudentListActivity.class);
            }
        });
        llTeacherList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AdminTeacherList.class);
            }
        });
        llAddImageOrLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AdminAddInfo.class);
            }
        });
        llreport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(ReportActivity.class);
            }
        });

    }


    // CALL GET COUNT API HERE
    private void callGetCountApi() {
        if (!Utils.checkNetwork(AdminDashboardActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), AdminDashboardActivity.this);
            return;
        } else {
            Utils.showDialog(AdminDashboardActivity.this);
            ApiHandler.getApiService().getCountList(getTeacherDetail(), new retrofit.Callback<CountListMainResponse>() {
                @Override
                public void success(CountListMainResponse countListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (countListMainResponse == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (countListMainResponse.getStatus() == null) {
                        toast("Something Wrong");
                        return;
                    }
                    if (countListMainResponse.getStatus().equals("false")) {
                        toast(countListMainResponse.getMsg());
                        return;
                    }
                    if (countListMainResponse.getStatus().equals("true")) {
                        teacherCount = Integer.parseInt(countListMainResponse.getTeacherCount());
                        studentCount = Integer.parseInt(countListMainResponse.getStudentCount());
                        tvTeacherCountAdmin.setText(String.valueOf(teacherCount));
                        tvStudentCountAdmin.setText(String.valueOf(studentCount));
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Utils.dismissDialog();
                    Toast.makeText(AdminDashboardActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private Map<String, String> getTeacherDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }


    public void onMap(View view) {
        AdminMapActivity.start(this);
    }

    public void onRate(View view) {
        RatingActivity.start(this);
    }
}
