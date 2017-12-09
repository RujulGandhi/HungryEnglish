package app.com.HungryEnglish.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import app.com.HungryEnglish.Model.RemoveTeacher.BasicResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.WebActivity;
import app.com.HungryEnglish.databinding.ActivityFeedbackBinding;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FeedbackActivity extends BaseActivity {
    private ActivityFeedbackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onFeedBack(View view) {
        if (binding.feedbackMsg.length() > 0) {
            Utils.showDialog(this);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("subject", "Hungry English");
            hashMap.put("message", String.valueOf(binding.feedbackMsg.getText()));
//            hashMap.put("email", "Rujul.co@gmail.com");
            hashMap.put("email", "idigi@live.com");


            ApiHandler.getApiService().sendMail(hashMap, new Callback<BasicResponse>() {
                @Override
                public void success(BasicResponse basicResponse, Response response) {
                    if (basicResponse.getStatus().equalsIgnoreCase("true")) {
                        toast(basicResponse.getMsg());
                        finish();
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

        } else {
            binding.txtInputFeedback.setErrorEnabled(true);
            binding.txtInputFeedback.setError(getString(R.string.err_feedback));
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onPrivacyClick(View view) {
        WebActivity.start(this);
    }
}