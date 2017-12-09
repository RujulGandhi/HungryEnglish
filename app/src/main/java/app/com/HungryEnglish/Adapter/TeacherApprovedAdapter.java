package app.com.HungryEnglish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import app.com.HungryEnglish.Activity.Teacher.TeacherProfileActivity;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.Model.RemoveTeacher.BasicResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.Views.CircleImageView;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Fragment.TeacherApprovedListFragment.callRemoveTeacherFromListApi;
import static app.com.HungryEnglish.Util.Utils.checkNetwork;
import static app.com.HungryEnglish.Util.Utils.showDialog;

/**
 * Created by Vnnovate on 7/19/2017.
 */

public class TeacherApprovedAdapter extends RecyclerView.Adapter<TeacherApprovedAdapter.MyViewHolder> {
    private List<TeacherListResponse> teacherList;
    private Context mContext;
    AdapterView.OnItemClickListener mOnItemClickLister;
    private TeacherPendingAdapter.OnRemoveTeacherClickListener mListener;


    public interface OnRemoveTeacherClickListener {
        public void onItemClick(View view, int position);
    }

    public TeacherApprovedAdapter(Context activity, List<TeacherListResponse> teacherList) {
        this.mContext = activity;
        this.teacherList = teacherList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTeacherName, tvEmail, tvMobileNo, tvTeacherAvaibility;
        public CircleImageView ivProfilePic;
        public RelativeLayout ivRemove, ivEdit, inactive;
        public LinearLayout llEditDel;


        public MyViewHolder(View view) {
            super(view);
            tvTeacherName = (TextView) view.findViewById(R.id.tvTeacherName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            tvTeacherAvaibility = (TextView) view.findViewById(R.id.tvTeacherAvaibility);
            ivProfilePic = (CircleImageView) view.findViewById(R.id.ivTeacherProfilePic);
            ivRemove = (RelativeLayout) view.findViewById(R.id.approvedTeacherRemove);
            ivEdit = (RelativeLayout) view.findViewById(R.id.approvedTeacherEdit);
            llEditDel = (LinearLayout) view.findViewById(R.id.llEditDel);
            inactive = (RelativeLayout) view.findViewById(R.id.inactiveRel);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_approved_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int pos = position;
        //        Movie movie = teacherList.get(position);
        holder.tvTeacherName.setText(teacherList.get(pos).getUsername());

        holder.tvEmail.setText("Email : " + teacherList.get(pos).getEmail());
        if (teacherList.get(pos).getTeacherInfo() != null && teacherList.get(pos).getTeacherInfo().getAvailableTime() != null) {
            holder.tvTeacherAvaibility.setText("Avaibility : " + Utils.getDisplayString(teacherList.get(pos).getTeacherInfo().getAvailableTime()));
            String imageUrl = RestConstant.BASEURL + teacherList.get(pos).getTeacherInfo().getProfileImage();
            Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default).into(holder.ivProfilePic);
        }
        holder.tvMobileNo.setText("Mobile No : " + teacherList.get(pos).getMobNo());
        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRemoveTeacherFromListApi(pos, teacherList.get(pos).getId(), teacherList.get(pos).getRole());
            }
        });

        holder.ivEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TeacherProfileActivity.class);
                intent.putExtra("id", teacherList.get(pos).getId());
                intent.putExtra("role", teacherList.get(pos).getRole());
                intent.putExtra("callFrom", "Admin");
                mContext.startActivity(intent);
            }
        });

        holder.inactive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.alert(mContext, mContext.getString(R.string.inactive_user), mContext.getString(R.string.confirm_inacitve), mContext.getString(R.string.yes), mContext.getString(R.string.no), new OnDialogEvent() {
                    @Override
                    public void onPositivePressed() {
                        inactiveTeacher(teacherList.get(pos).getId(),pos);
                    }

                    @Override
                    public void onNegativePressed() {

                    }
                });
            }
        });

//        holder.tvGender.setText("Gender : " + teacherList.get(position).get("gender"));
//        holder.tvExperience.setText("Experience : " + teacherList.get(position).get("experience"));
//        holder.tvTeacherAvaibility.setText("Avaibility : " + teacherList.get(position).get("avaibility"));
    }


    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    // CALL TEACHER LIST API HERE
    public void inactiveTeacher(String id,final int pos) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", id);
        hashMap.put("is_active", "0");
        if (!checkNetwork(mContext)) {
            Utils.showCustomDialog("Internet Connection !", mContext.getString(R.string.internet_connection_error), (Activity) mContext);
            return;
        } else {
            showDialog(mContext);
            ApiHandler.getApiService().inactiveUser(hashMap, new retrofit.Callback<BasicResponse>() {
                @Override
                public void success(BasicResponse teacherListMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherListMainResponse.getStatus().equalsIgnoreCase("true")) {
                        Toast.makeText(mContext, teacherListMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        teacherList.remove(pos);
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(mContext, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
