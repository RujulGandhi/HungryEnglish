package app.com.HungryEnglish.Activity.Teacher;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.Student.StudentProfileActivity;
import app.com.HungryEnglish.Adapter.ImageAdapter;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.Model.Teacher.InfoMainResponse;
import app.com.HungryEnglish.Model.Teacher.InfoResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Constant;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityMainBinding;
import app.com.HungryEnglish.databinding.LinkViewBinding;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity {
    private int cnt = 0;
    private InfoResponse infoList;
    private String imageURL1, imageURL2, imageURL3;
    private String imageClickURL1, imageClickURL2, imageClickURL3;
    private ArrayList<String> imageArray, linkArray;
    private Handler handler = new Handler();
    private int temp = 0;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        callGetInfoApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        // return true so that the menu pop up is opened

        MenuItem item = (MenuItem) menu.findItem(R.id.contact);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String role = Utils.ReadSharePrefrence(getApplicationContext(), Constant.SHARED_PREFS.KEY_USER_ROLE);
        switch (item.getItemId()) {
            case R.id.logout:

                Utils.alert(this, getString(R.string.logout), getString(R.string.logout_note), getString(R.string.logout), getString(R.string.cancel), new OnDialogEvent() {
                    @Override
                    public void onPositivePressed() {
                        clear();
                        Utils.logout(getApplicationContext());
                        finish();
                    }

                    @Override
                    public void onNegativePressed() {

                    }
                });
                break;
            case R.id.profile:
                if (role.equalsIgnoreCase("student"))
                    startActivity(StudentProfileActivity.class);

                else if (role.equalsIgnoreCase("teacher"))
                    startActivity(TeacherProfileActivity.class);
                break;

            case R.id.contact:
                startActivity(Contactus.class);
                break;
        }
        return true;
    }

    private void callGetInfoApi() {
        if (!Utils.checkNetwork(getApplicationContext())) {
            Utils.showCustomDialog(getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        } else {
            ApiHandler.getApiService().getInfo(getInfo(), new retrofit.Callback<InfoMainResponse>() {
                @Override
                public void success(InfoMainResponse infoMainResponse, Response response) {

                    imageArray = new ArrayList<String>();
                    linkArray = new ArrayList<String>();
                    if (infoMainResponse == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (infoMainResponse.getStatus() == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (infoMainResponse.getStatus().equals("false")) {
                        toast(infoMainResponse.getMsg());
                        return;
                    }
                    if (infoMainResponse.getStatus().equals("true")) {

                        infoList = new InfoResponse();
                        infoList = infoMainResponse.getInfo();
                        imageURL1 = Constant.BASEURL + infoList.getImage1();
                        imageURL2 = Constant.BASEURL + infoList.getImage2();
                        imageURL3 = Constant.BASEURL + infoList.getImage3();

                        imageArray.add(imageURL1);
                        imageArray.add(imageURL2);
                        imageArray.add(imageURL3);

//                        SetImage1();
                        if (!infoList.getLink1().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink1().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                            addView(link1[0], link1[1]);
                            imageClickURL1 = link1[2];
                            linkArray.add(imageClickURL1);
                        }

                        if (!infoList.getLink2().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink2().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                            addView(link1[0], link1[1]);
                            imageClickURL2 = link1[2];
                            linkArray.add(imageClickURL2);
                        }

                        if (!infoList.getLink3().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink3().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                            addView(link1[0], link1[1]);
                            imageClickURL3 = link1[2];
                            linkArray.add(imageClickURL3);
                        }

                        if (!infoList.getLink4().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink4().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                            addView(link1[0], link1[1]);
                        }

                        if (!infoList.getLink5().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink5().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                            addView(link1[0], link1[1]);
                        }

                        if (!infoList.getLink6().equalsIgnoreCase("")) {
                            String[] link1 = infoList.getLink6().split("--");
//                            addDynamicContactText(link1[0]);
//                            addDynamicContactText(link1[1]);
                            addView(link1[0], link1[1]);
                        }


                    }
//                    binding.viewPager.setAdapter(new CustomPagerAdapter(MainActivity.this, imageArray, linkArray));
                    binding.viewPager.setAdapter(new ImageAdapter(getSupportFragmentManager(), MainActivity.this, imageArray, linkArray));
                    binding.tablayout.setupWithViewPager(binding.viewPager, true);
                    autoScroll();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    toast(getString(R.string.something_wrong));
                }
            });
        }

    }

    private void autoScroll() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = binding.viewPager.getCurrentItem();
                int nextItemCount = currentItem + 1;
                binding.viewPager.setCurrentItem(nextItemCount % 3);
                handler.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private Map<String, String> getInfo() {
        Map<String, String> map = new HashMap<>();
        return map;
    }


    private void addDynamicContactText(final String link1) {
        cnt = cnt + 1;
        TextView tvLabel = new TextView(getApplicationContext());
        tvLabel.setTextAppearance(getApplicationContext(), R.style.small_textview);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(Math.round(getResources().getDimension(R.dimen._5sdp)), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
        tvLabel.setLayoutParams(llp);
        tvLabel.setPadding(Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)), Math.round(getResources().getDimension(R.dimen._5sdp)));
        tvLabel.setText(link1);
        tvLabel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        tvLabel.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        tvLabel.setTextSize(12);
//        binding.llLinkList.addView(tvLabel);
        if (link1.startsWith("www.")) {
            tvLabel.setTextColor(Color.BLUE);
        }
        tvLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "";
                if (link1.startsWith("www.")) {
                    if (!link1.startsWith("http://") && !link1.startsWith("https://")) {
                        url = "http://" + link1;
                    } else {
                        url = link1;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }

            }

        });
    }

    private void addView(String linkTitle, final String link) {
        LinkViewBinding bindingLink = LinkViewBinding.inflate(LayoutInflater.from(getApplicationContext()));
        bindingLink.linkTitle.setText(linkTitle);
        bindingLink.link.setText(link);
        bindingLink.link.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "";
                if (link.startsWith("www.")) {
                    if (!link.startsWith("http://") && !link.startsWith("https://")) {
                        url = "http://" + link;
                    } else {
                        url = link;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }

            }

        });
        int padding = Math.round(getResources().getDimension(R.dimen._3sdp));
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(padding, padding, padding, padding);
        bindingLink.getRoot().setLayoutParams(llp);
        binding.llLinkList.addView(bindingLink.getRoot());
    }

}

