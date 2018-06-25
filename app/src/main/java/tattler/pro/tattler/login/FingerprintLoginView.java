package tattler.pro.tattler.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface FingerprintLoginView extends MvpView {
    void showAuthSucceedToast();
    void showAuthFailToast();
    void showAuthErrorToast(String message);
    void showAuthHelpToast(String message);
}
