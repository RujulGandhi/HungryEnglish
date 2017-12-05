package app.com.HungryEnglish.View;

import java.util.ArrayList;

import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;
import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;

/**
 * Created by rujul on 4/12/17.
 */

public interface AdminAddInfoTeacherView extends MvpView {

    void getErrorInAddText(String string);

    void addData(AdminAddInfoResponse basicResponse);

    void showSliderData(ArrayList<AdminAddInfoDetail> sliderArray);

    void showLinkData(ArrayList<AdminAddInfoDetail> linkArray);
}
