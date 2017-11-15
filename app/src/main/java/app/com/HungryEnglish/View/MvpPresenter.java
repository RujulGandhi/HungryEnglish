package app.com.HungryEnglish.View;

public interface MvpPresenter<V extends MvpView> {
    void attachView(V mvpView);

    void detachView();

    void onDestroyed();
}