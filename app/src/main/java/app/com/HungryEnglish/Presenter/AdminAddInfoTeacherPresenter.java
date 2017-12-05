package app.com.HungryEnglish.Presenter;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import app.com.HungryEnglish.Activity.Admin.AdminAddInfo;
import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;
import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.Model.admin.TeacherDashboardInfoMain;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RequestParams;
import app.com.HungryEnglish.View.AdminAddInfoTeacherView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by rujul on 4/12/17.
 */

public class AdminAddInfoTeacherPresenter extends BasePresenter<AdminAddInfoTeacherView> {
    private Context context;


    public AdminAddInfoTeacherPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onDestroyed() {

    }

    public void addLink(String title, String link) {
        if (title.length() > 0 && link.length() > 0) {
            getMvpView().showProgressDialog();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(RequestParams.Role.getValue(), RequestParams.Teacher.getValue());
            hashMap.put(RequestParams.Title.getValue(), title);
            hashMap.put(RequestParams.Link.getValue(), link);

            ApiHandler.getApiService().addTitleLinkInfo(hashMap, new Callback<AdminAddInfoResponse>() {
                @Override
                public void success(AdminAddInfoResponse basicResponse, Response response) {
                    getMvpView().hideProgressDialog();
                    if (basicResponse.getStatus().equalsIgnoreCase("true")) {
                        getMvpView().addData(basicResponse);
                    } else {
                        getMvpView().showErrorMsg(basicResponse.getMsg());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    getMvpView().hideProgressDialog();
                }
            });
        } else {
            getMvpView().getErrorInAddText(context.getString(R.string.error_data));
        }
    }

    public void addImage(File pickedFile, String link) {
        if (pickedFile != null && link.length() > 0) {
            getMvpView().showProgressDialog();
            // file pick
            Map<String, TypedFile> files = new HashMap<String, TypedFile>();
            if (pickedFile != null) {
                TypedFile proImage = new TypedFile("multipart/form-data", pickedFile);
                files.put("img", proImage);
            }

            // Add Link
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(RequestParams.Role.getValue(), RequestParams.Teacher.getValue());
            hashMap.put(RequestParams.Link.getValue(), link);

            ApiHandler.getApiService().addImageLinkInfo(files, hashMap, new Callback<AdminAddInfoResponse>() {
                @Override
                public void success(AdminAddInfoResponse basicResponse, Response response) {
                    getMvpView().hideProgressDialog();
                    if (basicResponse.getStatus().equalsIgnoreCase("true")) {
                        getMvpView().addData(basicResponse);
                    } else {
                        getMvpView().showErrorMsg(basicResponse.getMsg());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    getMvpView().hideProgressDialog();
                }
            });
        } else {
            getMvpView().getErrorInAddText(context.getString(R.string.error_data));
        }
    }

    public void getTeacherData() {
        getMvpView().showProgressDialog();
        // Add Link
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(RequestParams.Role.getValue(), RequestParams.Teacher.getValue());
        ApiHandler.getApiService().getTeacherDashBoard(hashMap, new Callback<TeacherDashboardInfoMain>() {
            @Override
            public void success(TeacherDashboardInfoMain basicResponse, Response response) {
                getMvpView().hideProgressDialog();
                ArrayList<AdminAddInfoDetail> sliderArray = new ArrayList<>();
                ArrayList<AdminAddInfoDetail> linkArray = new ArrayList<>();
                for (AdminAddInfoDetail infoDetail : basicResponse.getData()) {
                    if (infoDetail.getImages().length() > 0) {
                        sliderArray.add(infoDetail);
                    } else {
                        linkArray.add(infoDetail);
                    }
                }
                getMvpView().showLinkData(linkArray);
                getMvpView().showSliderData(sliderArray);
            }

            @Override
            public void failure(RetrofitError error) {
                getMvpView().hideProgressDialog();
            }
        });
    }
}
