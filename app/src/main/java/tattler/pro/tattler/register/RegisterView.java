package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface RegisterView extends MvpView {
    void startPhoneAuthenticationFragment();
    void startCodeVerificationFragment();
    void startFingerAuthActivity();
    void onPhoneAuthenticationCompleted();
    void onPhoneAuthenticationFailed();
}
