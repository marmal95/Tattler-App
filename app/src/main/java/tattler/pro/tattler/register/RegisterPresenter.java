package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import tattler.pro.tattler.authentication.PhoneAuthenticator;
import tattler.pro.tattler.util.AppPreferences;

public class RegisterPresenter extends MvpBasePresenter<RegisterView> implements PhoneAuthCallback {
    private PhoneAuthenticator phoneAuthenticator;
    private AppPreferences appPreferences;

    public RegisterPresenter(PhoneAuthenticator phoneAuthenticator, AppPreferences appPreferences) {
        this.phoneAuthenticator = phoneAuthenticator;
        this.appPreferences = appPreferences;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onVerificationCompleted(String phoneNumber) {
        appPreferences.put(AppPreferences.Key.USER_PHONE_NUMBER, phoneNumber);
        if (isViewAttached()) {
            getView().onPhoneAuthenticationCompleted();
            getView().startFingerAuthActivity();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onVerificationFailed(String failMessage) {
        if (isViewAttached()) {
            getView().onPhoneAuthenticationFailed();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onVerificationCodeSent() {
        if (isViewAttached()) {
            getView().startCodeVerificationFragment();
        }
    }

    public void verifyPhoneNumber(String phoneNumber) {
        phoneAuthenticator.startPhoneNumberVerification(phoneNumber, this);
    }

    public void verifyAuthCode(String code) {
        phoneAuthenticator.verifyPhoneNumberWithCode(code);
    }
}
