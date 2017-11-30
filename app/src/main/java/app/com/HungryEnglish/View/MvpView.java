package app.com.HungryEnglish.View;

public interface MvpView {
    void showProgressDialog();
    void hideProgressDialog();
    void showErrorMsg(String msg);
}