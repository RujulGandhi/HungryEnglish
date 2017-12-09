package app.com.HungryEnglish.Presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import app.com.HungryEnglish.Model.StudentList.InfoWithTeacherList;
import app.com.HungryEnglish.Model.Teacher.InfoResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.RequestParams;
import app.com.HungryEnglish.View.StudentHomeView;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rujul on 11/22/2017.
 */

public class StudentHomePresenter extends BasePresenter<StudentHomeView> {
    private ArrayList<String> imageArray;
    private ArrayList<String> linkArray;
    private InfoResponse infoList;
    private String imageURL1, imageURL2, imageURL3;
    private String imageClickURL1, imageClickURL2, imageClickURL3;


    @Override
    public void onDestroyed() {

    }

    public void getInfoWithTeacherList() {
        getMvpView().showProgressDialog();
        ApiHandler.getApiService().getInfoWithTeacherInfo(getInfo(), new retrofit.Callback<InfoWithTeacherList>() {
            @Override
            public void success(InfoWithTeacherList infoMainResponse, Response response) {
                imageArray = new ArrayList<String>();
                linkArray = new ArrayList<String>();
                if (infoMainResponse == null) {
                    getMvpView().showErrorMsg(R.string.something_wrong);
                    return;
                }
                if (infoMainResponse.getStatus() == null) {
                    getMvpView().showErrorMsg(R.string.something_wrong);
                    return;
                }
                if (infoMainResponse.getStatus().equals("false")) {
                    getMvpView().showErrorMsg(infoMainResponse.getMsg());
                    return;
                }
                if (infoMainResponse.getStatus().equals("true")) {
                    infoList = new InfoResponse();
                    infoList = infoMainResponse.getInfo();
                    imageURL1 = RestConstant.BASEURL + infoList.getImage1();
                    imageURL2 = RestConstant.BASEURL + infoList.getImage2();
                    imageURL3 = RestConstant.BASEURL + infoList.getImage3();

                    imageArray.add(imageURL1);
                    imageArray.add(imageURL2);
                    imageArray.add(imageURL3);

                    if (!infoList.getLink1().equalsIgnoreCase("")) {
                        String[] link1 = infoList.getLink1().split("--");
                        imageClickURL1 = link1[2];
                        linkArray.add(imageClickURL1);
                    }

                    if (!infoList.getLink2().equalsIgnoreCase("")) {
                        String[] link1 = infoList.getLink2().split("--");
                        imageClickURL2 = link1[2];
                        linkArray.add(imageClickURL2);
                    }

                    if (!infoList.getLink3().equalsIgnoreCase("")) {
                        String[] link1 = infoList.getLink3().split("--");
                        imageClickURL3 = link1[2];
                        linkArray.add(imageClickURL3);
                    }
//                    Log.d("data",)
                    getMvpView().showSlider(imageArray, linkArray);

                    getMvpView().showTeacherList(infoMainResponse.getData());

                }
                getMvpView().hideProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                getMvpView().hideProgressDialog();
                if (error != null)
                    getMvpView().showErrorMsg(error.getMessage());
            }
        });
    }

    public HashMap<String, String> getInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(RequestParams.ActionName.getValue(), "abc");
        hashMap.put(RequestParams.Role.getValue(), "teacher");
        hashMap.put(RequestParams.Status.getValue(), "2");
//        http://smartsquad.pe.hu/HungryEnglish/api/get_info.php?action=abc&role=teacher&status=2
        return hashMap;
    }

    public void onItemClick(TeacherListResponse item) {
        Log.d("item", item.getId());
        getMvpView().showTeacherProfile(item.getId());
    }
}
