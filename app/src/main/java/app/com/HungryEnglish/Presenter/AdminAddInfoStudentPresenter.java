package app.com.HungryEnglish.Presenter;

import android.content.Context;
import android.text.Editable;

import java.util.HashMap;

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

            ApiHandler.getApiService().addStudentInfo(hashMap, new Callback<AdminAddInfoResponse>() {
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
}
