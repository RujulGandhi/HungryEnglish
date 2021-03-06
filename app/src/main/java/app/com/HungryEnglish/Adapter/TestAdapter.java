package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import app.com.HungryEnglish.Activity.ImageActivity;
import app.com.HungryEnglish.Interface.OnItemClick;
import app.com.HungryEnglish.Model.Teacher.TeacherInfo;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.databinding.TeacherGridAdapterBinding;

/**
 * Created by Rujul on 10/2/2017.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {

    private Context context;
    private List<TeacherListResponse> teacherInfo;
    private OnItemClick onItemClick;

    public TestAdapter(Context context, List<TeacherListResponse> data) {
        this.context = context;
        this.teacherInfo = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(viewGroup.getContext());
        TeacherGridAdapterBinding itemBinding =
                TeacherGridAdapterBinding.inflate(layoutInflater, viewGroup, false);
        return new MyViewHolder(itemBinding, onItemClick);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        TeacherInfo info = teacherInfo.get(i).getTeacherInfo();
        myViewHolder.binding.teacherName.setText(teacherInfo.get(i).getFullName());
        if (info != null) {
            final String imageURL = RestConstant.BASEURL + info.getProfileImage();
            if (info.getProfileImage().length() > 1) {
                Glide.with(context).load(imageURL)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default)).into(myViewHolder.binding.ivTeacherProfilePic);
            } else {
                Glide.with(context).load(R.drawable.ic_user_default).into(myViewHolder.binding.ivTeacherProfilePic);
            }
            myViewHolder.binding.ivTeacherProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context, ImageActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("url", imageURL);
                    context.startActivity(in);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return teacherInfo.size();
    }

    public void setListenr(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TeacherGridAdapterBinding binding;

        public MyViewHolder(TeacherGridAdapterBinding itemView, OnItemClick onItemClick) {
            super(itemView.getRoot());
            binding = itemView;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onItemClick(teacherInfo.get(getAdapterPosition()), getAdapterPosition());
        }
    }

}
