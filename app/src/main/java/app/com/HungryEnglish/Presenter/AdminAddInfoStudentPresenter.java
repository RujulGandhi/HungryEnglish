package app.com.HungryEnglish.Presenter;

import android.content.Context;
import android.text.Editable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Interface.AdminAddInfoStudentView;
import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.Model.admin.UserListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RequestParams;
import app.com.HungryEnglish.View.AdminMapView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by rujul on 3/12/17.
 */

public class AdminAddInfoStudentPresenter extends BasePresenter<AdminAddInfoStudentView> {
    private Context context;

    public AdminAddInfoStudentPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onDestroyed() {

    }

    public void addLink(String title, String link) {
        if (title.length() > 0 && link.length() > 0) {
            getMvpView().showProgressDialog();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(RequestParams.Role.getValue(), RequestParams.Student.getValue());
            hashMap.put(RequestParams.Title.getValue(), title);
            hashMap.put(RequestParams.Link.getValue(), link);

            ApiHandler.getApiService().addTitleLinkInfo(hashMap, new Callback<AdminAddInfoResponse>() {
                @Override
                public void success(AdminAddInfoResponse basicResponse, Response response) {
                    getMvpView().hideProgressDialog();
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
            hashMap.put(RequestParams.Role.getValue(), RequestParams.Student.getValue());
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
}
