package app.com.HungryEnglish.View;

import java.util.ArrayList;
import java.util.List;

import app.com.HungryEnglish.Activity.Admin.AdminAddInfo;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;

/**
 * Created by Rujul on 11/22/2017.
 */

public interface StudentHomeView extends MvpView {

    void showErrorMsg(int something_wrong);

    void showSlider(ArrayList<AdminAddInfoDetail> imageArray);

    void showTeacherList(ArrayList<TeacherListResponse> data);

    void showTeacherProfile(String id);
}
