package app.com.HungryEnglish.Activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Activity.Admin.AdminDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentDashboardActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Activity.Teacher.MainActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.Interface.OnLanguageChange;
import app.com.HungryEnglish.Model.login.LoginMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityLoginBinding;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Util.Utils.WriteSharePrefrence;

public class LoginActivity extends BaseActivity {

    private Context context;
    private Utils utils;
    private TextView forgotPasswordTxt;
    private String Token = "ABC";
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = this;
        utils = new Utils(context);
        setTitle(getString(R.string.sign_in));
    }

    // CALL LOGIN API HERE
    private void callLoginApi() {
        if (!Utils.checkNetwork(context)) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }

        Utils.showDialog(context);
        ApiHandler.getApiService().getLogin(getLoginDetail(), new retrofit.Callback<LoginMainResponse>() {
            @Override
            public void success(LoginMainResponse loginUser, Response response) {
                Utils.dismissDialog();
                if (loginUser == null) {
                    toast(getString(R.string.something_wrong));
                    return;
                }
                if (loginUser.getStatus() == null) {
                    toast(getString(R.string.something_wrong));
                    return;
                }
                if (loginUser.getStatus().equals("false")) {
                    toast(loginUser.getMsg());
                    return;
                }
                if (loginUser.getStatus().equals("true")) {

                    String role = loginUser.getData().getRole();
                    String isActiveStatue = loginUser.getData().getIsActive();

                    WriteSharePrefrence(context, RestConstant.SHARED_PREFS.KEY_USER_ID, loginUser.getData().getId());
                    WriteSharePrefrence(context, RestConstant.SHARED_PREFS.KEY_USER_ROLE, role);
                    WriteSharePrefrence(context, RestConstant.SHARED_PREFS.KEY_USER_NAME, loginUser.getData().getUsername());
                    WriteSharePrefrence(context, RestConstant.SHARED_PREFS.KEY_IS_ACTIVE, isActiveStatue);

                    if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("0")) {
                        startActivity(StudentProfileActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    } else if (role.equalsIgnoreCase("student") && isActiveStatue.equalsIgnoreCase("1")) {
                        startActivity(StudentDashboardActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("0")) {

                        toast(getString(R.string.admin_approve_request));

                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("1")) {
                        startActivity(TeacherProfileActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    } else if (role.equalsIgnoreCase("teacher") && isActiveStatue.equalsIgnoreCase("2")) {
                        startActivity(MainActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    } else if (role.equalsIgnoreCase("admin")) {
                        startActivity(AdminDashboardActivity.class);
                        toast(loginUser.getMsg());
                        finish();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                toast(getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getLoginDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("u_pass", "" + binding.edtPassword.getText().toString());
        map.put("u_name", "" + binding.edtUsername.getText());
        map.put("device_id", Token);
        return map;
    }

    public void onLogin(View view) {
        if (binding.edtUsername.getText().toString().equalsIgnoreCase(getString(R.string.null_value))) {
            binding.txtInputUsername.setErrorEnabled(true);
            binding.txtInputUsername.setError(getString(R.string.email_validation));
            return;
        } else {
            binding.txtInputUsername.setErrorEnabled(false);
        }
        if (binding.edtPassword.getText().toString().equalsIgnoreCase(getString(R.string.null_value))) {
            binding.txtInputPasssword.setErrorEnabled(true);
            binding.txtInputPasssword.setError(getString(R.string.password_validation));
            return;
        } else {
            binding.txtInputPasssword.setErrorEnabled(false);
        }
        callLoginApi();
    }

    public void onRegister(View view) {
        startActivity(RegisterActivity.class);
    }

    public void onForgetPassword(View view) {
        startActivity(ForgotPassword.class);
    }

    public void onLanguageChange(View view) {
        utils.alertChangeLanguage(this, new OnLanguageChange() {
            @Override
            public void onPositivePressed(String languageCode) {
                utils.changeLanguage(getApplicationContext(), languageCode);
                recreate();
            }

            @Override
            public void onNegativePressed() {

            }
        });


    }
}
