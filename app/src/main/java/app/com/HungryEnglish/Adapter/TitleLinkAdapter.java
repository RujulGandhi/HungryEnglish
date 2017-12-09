package app.com.HungryEnglish.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;
import app.com.HungryEnglish.databinding.AdapterAdminRateBinding;
import app.com.HungryEnglish.databinding.AdapterTitleLinkBinding;

/**
 * Created by Rujul on 10/2/2017.
 */

public class TitleLinkAdapter extends RecyclerView.Adapter<TitleLinkAdapter.MyViewHolder> {

    private Context context;
    private List<AdminAddInfoDetail> teacherArray;

    public TitleLinkAdapter(Context context, List<AdminAddInfoDetail> data) {
        this.context = context;
        this.teacherArray = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(viewGroup.getContext());
        AdapterTitleLinkBinding itemBinding =
                AdapterTitleLinkBinding.inflate(layoutInflater, viewGroup, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final AdminAddInfoDetail info = teacherArray.get(i);
        myViewHolder.binding.linkTitle.setText(info.getTitle());
        myViewHolder.binding.link.setText(info.getLink());
        myViewHolder.binding.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String url = info.getLink();
                    if (!url.startsWith("http") && !url.startsWith("https")) {
                        url = "http://" + url;
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No Browser", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final AdapterTitleLinkBinding binding;

        public MyViewHolder(AdapterTitleLinkBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
