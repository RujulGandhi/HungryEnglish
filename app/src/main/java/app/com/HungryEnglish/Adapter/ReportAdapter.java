package app.com.HungryEnglish.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.com.HungryEnglish.Fragment.TeacherApprovedListFragment;
import app.com.HungryEnglish.Model.Report.ReportInfo;
import app.com.HungryEnglish.R;

/**
 * Created by Bhadresh Chavada on 01-08-2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    private List<ReportInfo> reportList;
    private Context mContext;
    TeacherApprovedListFragment activity;
    //    private OnRemoveTeacherClickListener mListener;
    private int pos;

    public ReportAdapter(Context mContext, List<ReportInfo> reportList) {
        this.mContext = mContext;
        this.reportList = reportList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRequestString;


        public MyViewHolder(View view) {
            super(view);
            tvRequestString = (TextView) view.findViewById(R.id.report_text);


        }
    }

    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report, parent, false);

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext,ReportDetails.class);
//                intent.putParcelableArrayListExtra("ReportDetails",reportList.get(pos));
//                mContext.startActivity(intent);
//
//            }
//        });

        return new ReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        pos = position;
        holder.tvRequestString.setText(reportList.get(position).getStuFullName() + "  Request to  " + reportList.get(position).getTeacherEmail());


    }


    @Override
    public int getItemCount() {
        Log.e("COUNT", "@@ " + reportList.size());
        return reportList.size();
    }

}
