package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import tattler.pro.tattler.authentication.PhoneAuthenticator;

public class RegisterPresenter extends MvpBasePresenter<RegisterView> implements PhoneAuthCallback {
    private PhoneAuthenticator phoneAuthenticator;

    public RegisterPresenter(PhoneAuthenticator phoneAuthenticator) {
        this.phoneAuthenticator = phoneAuthenticator;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onVerificationCompleted(String phoneNumber) {
        //TODO: Save in shared preferences
        if (isViewAttached()) {
            getView().onPhoneAuthenticationCompleted();
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
            getView().launchCodeVerificationFragment();
        }
    }

    public void verifyPhoneNumber(String phoneNumber) {
        phoneAuthenticator.startPhoneNumberVerification(phoneNumber, this);
    }

    public void verifyAuthCode(String code) {
        phoneAuthenticator.verifyPhoneNumberWithCode(code);
    }
}
