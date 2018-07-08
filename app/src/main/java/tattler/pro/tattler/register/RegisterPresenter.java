package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.authentication.PhoneAuthenticator;
import tattler.pro.tattler.common.AppPreferences;

public class RegisterPresenter extends MvpBasePresenter<RegisterView> implements PhoneAuthCallback {
    private PhoneAuthenticator phoneAuthenticator;
    private AppPreferences appPreferences;
    private UserRegisterData userRegisterData;

    RegisterPresenter(PhoneAuthenticator phoneAuthenticator, AppPreferences appPreferences) {
        this.phoneAuthenticator = phoneAuthenticator;
        this.appPreferences = appPreferences;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onVerificationCompleted(String phoneNumber) {
        logWhenPhoneNumberNotEqual(phoneNumber);
        appPreferences.put(AppPreferences.Key.USER_PHONE_NUMBER, phoneNumber);
        appPreferences.put(AppPreferences.Key.USER_NAME, userRegisterData.userName);
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

    public void rememberUserData(UserRegisterData userData) {
        userRegisterData = userData;
    }

    private void logWhenPhoneNumberNotEqual(String phoneNumber) {
        if (!phoneNumber.equals(userRegisterData.phoneNumber)) {
            Logger.w("Verified phone number is different then remembered user phone number!");
        }
    }
}
