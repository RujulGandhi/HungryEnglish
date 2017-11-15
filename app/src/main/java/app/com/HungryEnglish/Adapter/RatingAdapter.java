package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.suke.widget.SwitchButton;

import java.util.HashMap;
import java.util.List;

import app.com.HungryEnglish.Model.Rate.RateInfo;
import app.com.HungryEnglish.Model.RemoveTeacher.BasicResponse;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.databinding.AdapterAdminRateBinding;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by good on 10/2/2017.
 */

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {

    private Context context;
    private List<RateInfo> rateInfo;

    public RatingAdapter(Context context, List<RateInfo> data) {
        this.context = context;
        this.rateInfo = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(viewGroup.getContext());
        AdapterAdminRateBinding itemBinding =
                AdapterAdminRateBinding.inflate(layoutInflater, viewGroup, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final RateInfo info = rateInfo.get(i);
        String msg = info.getStuUsername() + " gave rate to " + info.getTeaUsername();
        myViewHolder.binding.msgTv.setText(msg);
        if (info.getRate() != "") {
            int count = (int) Float.parseFloat(info.getRate());
            myViewHolder.binding.rateBar.setCount(count);
        } else
            myViewHolder.binding.rateBar.setCount(0);
        myViewHolder.binding.switchButton.setChecked(info.getIs_approved().equals("1"));

        myViewHolder.binding.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                String rateStatus;
                if (isChecked) {
                    rateStatus = "1";
                } else {
                    rateStatus = "0";
                }
                hashMap.put("rate_status", rateStatus);
                hashMap.put("action", "update_rate");
                hashMap.put("rate_id", info.getRateId());
                hashMap.put("teacher_id", info.getTeaId());
                callUpdateRate(hashMap, i, rateStatus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rateInfo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final AdapterAdminRateBinding binding;

        public MyViewHolder(AdapterAdminRateBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    // CALL LOGIN API HERE
    private void callUpdateRate(HashMap<String, String> hashMap, final int i, final String rateStatus) {
        ApiHandler.getApiService().updateRate(hashMap, new retrofit.Callback<BasicResponse>() {
            @Override
            public void success(BasicResponse responseObject, Response response) {
                if (responseObject.getStatus().equalsIgnoreCase("true")) {
                    RateInfo info = rateInfo.get(i);
                    info.setIs_approved(rateStatus);
                    rateInfo.set(i, info);
                } else {
                    RateInfo info = rateInfo.get(i);
                    rateInfo.set(i, info);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                error.getMessage();
            }
        });

    }

}
