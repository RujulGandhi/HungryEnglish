package app.com.HungryEnglish.Activity.Admin;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Adapter.RatingAdapter;
import app.com.HungryEnglish.Model.Rate.RateBasicResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityRateListBinding;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by good on 10/2/2017.
 */

public class RatingActivity extends BaseActivity {
    ActivityRateListBinding binding;
    RatingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rate_list);
        getRatingList();
        setTitle("Rating List");
    }


    // CALL LOGIN API HERE
    private void getRatingList() {
        if (!Utils.checkNetwork(this)) {
            Utils.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }

        Utils.showDialog(this);
        ApiHandler.getApiService().getAllRating(getLoginDetail(), new retrofit.Callback<RateBasicResponse>() {
            @Override
            public void success(RateBasicResponse rateData, Response response) {
                Utils.dismissDialog();

                if (rateData.getData() != null) {
                    adapter = new RatingAdapter(RatingActivity.this, rateData.getData());
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(RatingActivity.this);
                    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.recyclerRatelist.setLayoutManager(mLayoutManager);
                    binding.recyclerRatelist.setItemAnimator(new DefaultItemAnimator());
                    binding.recyclerRatelist.setAdapter(adapter);
                    binding.recyclerRatelist.setVisibility(View.VISIBLE);
                    binding.emptyView.setVisibility(View.GONE);
                } else {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerRatelist.setVisibility(View.GONE);
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
        map.put("action", "get_all_rate");
        return map;
    }
}
