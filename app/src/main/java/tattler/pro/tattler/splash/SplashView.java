package tattler.pro.tattler.splash;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface SplashView extends MvpView {
    void startRegisterActivity();
    void startFingerAuthActivity();
}
