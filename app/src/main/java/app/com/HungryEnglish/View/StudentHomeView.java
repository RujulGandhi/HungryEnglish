package app.com.HungryEnglish.View;

import java.util.ArrayList;
import java.util.List;

import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;

/**
 * Created by Rujul on 11/22/2017.
 */

public interface StudentHomeView extends MvpView {

    void showErrorMsg(int something_wrong);

    void showSlider(ArrayList<String> imageArray, ArrayList<String> linkArray);

    void showTeacherList(List<TeacherListResponse> data);

    void showTeacherProfile(String id);
}
