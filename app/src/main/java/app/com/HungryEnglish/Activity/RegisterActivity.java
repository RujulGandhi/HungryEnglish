package app.com.HungryEnglish.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Model.register.RegisterMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityRegisterBinding;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Util.RestConstant.ROLE_STUDENT;
import static app.com.HungryEnglish.Util.RestConstant.ROLE_TEACHER;

// TODO: 31/12/17 Rujul
public class RegisterActivity extends BaseActivity {

    private boolean isTeacher;
    private ActivityRegisterBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.signup));

    }

    // CALL REGISTER API HERE
    private void callRegisterApi() {

        if (!Utils.checkNetwork(RegisterActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), RegisterActivity.this);
            return;
        }


        ApiHandler.getApiService().getRegister(getRegisterDetail(), new retrofit.Callback<RegisterMainResponse>() {
            @Override
            public void success(RegisterMainResponse registerMainResponse, Response response) {
                if (registerMainResponse == null) {
                    toast(getString(R.string.something_wrong));

                    return;

                }
                if (registerMainResponse.getStatus() == null) {
                    toast(getString(R.string.something_wrong));

                    return;
                }
                if (registerMainResponse.getStatus().equals("false")) {
                    toast(registerMainResponse.getMsg());

                    return;
                }
                if (registerMainResponse.getStatus().equals("true")) {
                    toast("" + registerMainResponse.getMsg());
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                error.getMessage();
                toast(getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getRegisterDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("username", getText(binding.activityRegisterUsername));
        map.put("email", getText(binding.activityRegisterEmail));
        map.put("password", getText(binding.activityRegisterPassword));
        map.put("mob_no", getText(binding.activityRegisterMobileNumber));
        map.put("fullName", getText(binding.activityRegisterFullname));
        if (isTeacher) {
            map.put("role", ROLE_TEACHER);
        } else {
            map.put("role", ROLE_STUDENT);
        }
        return map;
    }

    public void onSignUp(View view) {

        if (getText(binding.activityRegisterFullname).equals("")) {
            binding.textInputFullname.setErrorEnabled(true);
            binding.textInputFullname.setError(getApplicationContext().getString(R.string.full_name_validation));
            return;
        }

        if (getText(binding.activityRegisterUsername).equals("")) {
            binding.textInputUsername.setErrorEnabled(true);
            binding.textInputUsername.setError(getApplicationContext().getString(R.string.user_name_validation));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(
                getText(binding.activityRegisterEmail)).matches()) {
            binding.textInputEmail.setErrorEnabled(true);
            binding.textInputEmail.setError(getString(R.string.email_validation));
            return;
        }

        if (getText(binding.activityRegisterPassword).equals("")) {
            binding.textInputPassword.setErrorEnabled(true);
            binding.textInputPassword.setError(getString(R.string.password_validation));
            return;
        }


        if (getText(binding.activityRegisterMobileNumber).equals("")) {
            binding.textInputMobile.setErrorEnabled(true);
            binding.textInputMobile.setError(getString(R.string.enter_mobile_or_wechat));
            return;
        }


        isTeacher = binding.activityRegisterIsTeacher.isChecked();
        // CALL REGISTER API
        callRegisterApi();
    }
}
