package app.com.HungryEnglish.Presenter;

import android.content.Context;

import java.util.HashMap;

import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RequestParams;
import app.com.HungryEnglish.View.AdminAddInfoTeacherView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

            ApiHandler.getApiService().addStudentInfo(hashMap, new Callback<AdminAddInfoResponse>() {
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
