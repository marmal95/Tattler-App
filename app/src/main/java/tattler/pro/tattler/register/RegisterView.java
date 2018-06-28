package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface RegisterView extends MvpView {
    void launchPhoneAuthenticationFragment();
    void launchCodeVerificationFragment();
    void onPhoneAuthenticationCompleted();
    void onPhoneAuthenticationFailed();
}
