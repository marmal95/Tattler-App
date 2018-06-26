package tattler.pro.tattler.login;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;

import tattler.pro.tattler.security.FingerprintAuthenticator;
import tattler.pro.tattler.security.FingerprintSensorCallback;

public class FingerprintLoginPresenter extends MvpBasePresenter<FingerprintLoginView> implements FingerprintSensorCallback {
    private FingerprintAuthenticator authenticator;

    FingerprintLoginPresenter(FingerprintAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onAuthenticationSucceeded() {
        if (isViewAttached()) {
            getView().showAuthSucceedToast();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onAuthenticationFailed() {
        if (isViewAttached()) {
            getView().showAuthFailToast();
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onAuthenticationError(String errorMessage) {
        if (isViewAttached()) {
            getView().showAuthErrorToast(errorMessage);
        }
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onAuthenticationHelp(String helpMessage) {
        if (isViewAttached()) {
            getView().showAuthHelpToast(helpMessage);
        }
    }

    public void startAuthentication() {
        if (authenticator != null) {
            Logger.d("Starting authentication.");
            authenticator.startAuthentication(this);
        } else {
            Logger.d("FingerprintAuthenticator is null and won't be started.");
        }
    }

    public void stopAuthentication() {
        if (authenticator != null) {
            Logger.d("Stopping authentication.");
            authenticator.stopAuthentication();
        } else {
            Logger.d("FingerprintAuthenticator is null and won't be stopped.");
        }
    }
}
