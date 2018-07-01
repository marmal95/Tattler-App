package tattler.pro.tattler.splash;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class SplashPresenter extends MvpBasePresenter<SplashView> {
    private UserLoginStatusChecker loginStatusChecker;

    SplashPresenter(UserLoginStatusChecker loginStatusChecker) {
        this.loginStatusChecker = loginStatusChecker;
    }

    @Override
    public void attachView(SplashView view) {
        super.attachView(view);
        handleUserLoginStatus();
    }

    private void handleUserLoginStatus() {
        SplashView view = getView();
        if (view == null) return;

        if (loginStatusChecker.isUserLoggedIn()) {
            view.startFingerAuthActivity();
        } else {
            view.startRegisterActivity();
        }
    }
}
