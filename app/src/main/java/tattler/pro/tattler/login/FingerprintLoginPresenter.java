package tattler.pro.tattler.login;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import tattler.pro.tattler.authentication.FingerprintAuthenticator;
import tattler.pro.tattler.authentication.FingerprintSensorCallback;

public class FingerprintLoginPresenter extends MvpBasePresenter<FingerprintLoginView> implements FingerprintSensorCallback {
    private FingerprintAuthenticator authenticator;

    FingerprintLoginPresenter(FingerprintAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onAuthenticationSucceeded() {
        if (isViewAttached()) {
            getView().indicateFingerprintAuthSuccess();
            getView().startMainActivity();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onAuthenticationFailed() {
        if (isViewAttached()) {
            getView().indicateFingerprintAuthFail();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onAuthenticationError(String errorMessage) {
        if (isViewAttached()) {
            getView().indicateFingerprintAuthError(errorMessage);
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onAuthenticationHelp(String helpMessage) {
        if (isViewAttached()) {
            getView().indicateFingerprintAuthHelp(helpMessage);
        }
    }

    public void startAuthentication() {
        if (authenticator != null) {
            authenticator.startAuthentication(this);
        }
    }

    public void stopAuthentication() {
        if (authenticator != null) {
            authenticator.stopAuthentication();
        }
    }
}
