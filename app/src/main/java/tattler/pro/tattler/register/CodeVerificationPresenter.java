package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class CodeVerificationPresenter extends MvpBasePresenter<CodeVerificationView> {
    private RegisterPresenter registerPresenter;

    CodeVerificationPresenter(RegisterPresenter registerPresenter) {
        this.registerPresenter = registerPresenter;
    }

    public void checkVerificationCode(String code) {
        if (code == null || code.isEmpty()) {
            showCodeInvalidOrEmptyError();
        } else {
            registerPresenter.verifyAuthCode(code);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showCodeInvalidOrEmptyError() {
        if (isViewAttached()) {
            getView().showCodeInvalidOrEmptyError();
        }
    }
}
