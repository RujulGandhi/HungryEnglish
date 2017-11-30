package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Views.BaseBindingAdapter;
import app.com.HungryEnglish.Views.BaseBindingViewHolder;
import app.com.HungryEnglish.databinding.TeacherGridAdapterBinding;

/**
 * Created by good on 11/25/2017.
 */

public class TeacherGridAdapter extends BaseBindingAdapter<TeacherListResponse> {
    private Context context;

    public TeacherGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected ViewDataBinding bind(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return TeacherGridAdapterBinding.inflate(inflater, parent, false);
    }

    @Override
    public void onBindViewHolder(BaseBindingViewHolder holder, int position) {
        TeacherGridAdapterBinding binding = (TeacherGridAdapterBinding) holder.binding;
        TeacherListResponse obj = items.get(position);
        binding.teacherName.setText(obj.getFullName());
        if (obj.getTeacherInfo() != null && !obj.getTeacherInfo().getProfileImage().equalsIgnoreCase("")) {
            String imageUrl = Constant.BASEURL + obj.getTeacherInfo().getProfileImage();
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(binding.ivTeacherProfilePic);
        } else
            binding.ivTeacherProfilePic.setImageResource(R.drawable.ic_user_default);
    }
}
