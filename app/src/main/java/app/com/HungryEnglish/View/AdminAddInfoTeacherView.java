package app.com.HungryEnglish.View;

import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;

/**
 * Created by rujul on 4/12/17.
 */

public interface AdminAddInfoTeacherView extends MvpView {

    void getErrorInAddText(String string);

    void addData(AdminAddInfoResponse basicResponse);
}
