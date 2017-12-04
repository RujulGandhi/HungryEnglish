package app.com.HungryEnglish.Interface;

import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.View.MvpView;

/**
 * Created by rujul on 3/12/17.
 */

public interface AdminAddInfoStudentView extends MvpView {

    public void getErrorInAddText(String message);

    void addData(AdminAddInfoResponse basicResponse);

}
