package tattler.pro.tattler.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface FingerprintLoginView extends MvpView {
    void indicateFingerprintAuthFail();
    void indicateFingerprintAuthSuccess();
    void indicateFingerprintAuthHelp(String message);
    void indicateFingerprintAuthError(String message);
}
