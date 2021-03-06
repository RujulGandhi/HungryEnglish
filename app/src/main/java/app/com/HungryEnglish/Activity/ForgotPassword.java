package app.com.HungryEnglish.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import app.com.HungryEnglish.Model.ForgotPassord.ForgotPasswordModel;
import app.com.HungryEnglish.Model.RemoveTeacher.BasicResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bhadresh Chavada on 26-07-2017.
 */

public class ForgotPassword extends BaseActivity implements View.OnClickListener {
    EditText edtEmail, edtOtp, edtPassword, edtReEnterPassword;
    Button btnsubmitEmail, btnSubmitOtp, btnSubmitPassword;
    int randomNumber;
    LinearLayout llResetPassword, llEmail, llOTP;
    TextView txtResendOtp;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail = (EditText) findViewById(R.id.activity_forgot_password_email);
        btnsubmitEmail = (Button) findViewById(R.id.activity_forgot_password_submit_btn);
        edtOtp = (EditText) findViewById(R.id.activity_forgot_password_otp);
        btnSubmitOtp = (Button) findViewById(R.id.activity_forgot_password_otp_submit_btn);
        llResetPassword = (LinearLayout) findViewById(R.id.password_reset_layout);
        edtPassword = (EditText) findViewById(R.id.activity_forgot_password_password);
        edtReEnterPassword = (EditText) findViewById(R.id.activity_forgot_password_re_enter_password);
        btnSubmitPassword = (Button) findViewById(R.id.activity_forgot_password_submit_password_btn);
        txtResendOtp = (TextView) findViewById(R.id.text_resend_otp);
        llEmail = (LinearLayout) findViewById(R.id.ll_enter_email);
        llOTP = (LinearLayout) findViewById(R.id.ll_enter_otp);

        btnsubmitEmail.setOnClickListener(this);
        btnSubmitOtp.setOnClickListener(this);
        btnSubmitPassword.setOnClickListener(this);
        txtResendOtp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.activity_forgot_password_submit_btn) {
            if (Utils.emailValidator(edtEmail.getText().toString())) {
                checkEmail();
            } else {
                edtEmail.setError(getString(R.string.email_validation));
                edtEmail.requestFocus();
            }
        } else if (v.getId() == R.id.activity_forgot_password_otp_submit_btn) {
            if (edtOtp.getText().toString().trim().equals(String.valueOf(randomNumber).trim())) {
                llResetPassword.setVisibility(View.VISIBLE);

                edtOtp.setVisibility(View.GONE);
                btnSubmitOtp.setVisibility(View.GONE);
                txtResendOtp.setVisibility(View.GONE);
                llOTP.setVisibility(View.GONE);

            } else {
                edtOtp.setError(getString(R.string.otp_validation));
                edtOtp.requestFocus();
            }
        } else if (v.getId() == R.id.activity_forgot_password_submit_password_btn) {
            if (edtPassword.getText().toString().equals(edtReEnterPassword.getText().toString()) && edtPassword.getText().toString().length() > 5) {
                ResetPassword();
            } else if (edtPassword.getText().length() > 5) {
                edtPassword.setError(getString(R.string.password_validation));
                edtPassword.requestFocus();
            } else {

                edtPassword.setError(getString(R.string.password_validation));
                edtPassword.requestFocus();
            }

        } else if (v.getId() == txtResendOtp.getId()) {
            sendEmail();
            toast(getString(R.string.email_send_successfully));
        }
    }

    private void checkEmail() {
        if (!Utils.checkNetwork(getApplicationContext())) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }

        Utils.showDialog(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", edtEmail.getText().toString());

        ApiHandler.getApiService().checkUser(map, new retrofit.Callback<ForgotPasswordModel>() {
            @Override
            public void success(ForgotPasswordModel forgotPasswordModel, Response response) {
                Utils.dismissDialog();
                if (forgotPasswordModel.getStatus().toString().equalsIgnoreCase("true")) {
                    email = edtEmail.getText().toString();
                    sendEmail();
                    Toast.makeText(getApplicationContext(), forgotPasswordModel.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), forgotPasswordModel.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ResetPassword() {
        if (!Utils.checkNetwork(getApplicationContext())) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }

        Utils.showDialog(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", edtPassword.getText().toString());

        ApiHandler.getApiService().resetPassword(map, new retrofit.Callback<ForgotPasswordModel>() {
            @Override
            public void success(ForgotPasswordModel forgotPasswordModel, Response response) {

                Utils.dismissDialog();
                Toast.makeText(getApplicationContext(), forgotPasswordModel.getMsg(), Toast.LENGTH_SHORT).show();

                if (forgotPasswordModel.getStatus().toString().equalsIgnoreCase("true")) {
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), forgotPasswordModel.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendEmail() {
        int digits = 6;
        randomNumber = nDigitRandomNo(digits);

        String message = "Welcome to Hungry English Club " + "\n" + "To Reset the Password enter below OTP in ypur application" + "\n" + randomNumber;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("subject", "Hungry English");
        hashMap.put("message", String.valueOf(message));
        hashMap.put("email", email);

        ApiHandler.getApiService().sendMail(hashMap, new Callback<BasicResponse>() {
            @Override
            public void success(BasicResponse basicResponse, Response response) {
                if (basicResponse.getStatus().equalsIgnoreCase("true")) {
                    toast(basicResponse.getMsg());
                    llEmail.setVisibility(View.GONE);
                    edtEmail.setVisibility(View.GONE);
                    btnsubmitEmail.setVisibility(View.GONE);
                    llOTP.setVisibility(View.VISIBLE);
                    edtOtp.setVisibility(View.VISIBLE);
                    btnSubmitOtp.setVisibility(View.VISIBLE);
                    txtResendOtp.setVisibility(View.VISIBLE);
                    btnSubmitPassword.setVisibility(View.VISIBLE);
                } else {
                    toast(basicResponse.getMsg());
                }
                Utils.dismissDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
            }
        });

    }

    private int nDigitRandomNo(int digits) {
        int max = (int) Math.pow(10, (digits)) - 1; //for digits =7, max will be 9999999
        int min = (int) Math.pow(10, digits - 1); //for digits = 7, min will be 1000000
        int range = max - min; //This is 8999999
        Random r = new Random();
        int x = r.nextInt(range);// This will generate random integers in range 0 - 8999999
        int nDigitRandomNo = x + min; //Our random rumber will be any random number x + min
        return nDigitRandomNo;
    }

}
