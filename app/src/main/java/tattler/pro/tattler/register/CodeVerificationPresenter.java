package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;

import tattler.pro.tattler.common.Util;

public class CodeVerificationPresenter extends MvpBasePresenter<CodeVerificationView> {
    private RegisterPresenter registerPresenter;

    CodeVerificationPresenter(RegisterPresenter registerPresenter) {
        this.registerPresenter = registerPresenter;
    }

    public void checkVerificationCode(String code) {
        if (!isDataFilledCorrectly(code)) {
            return;
        }

        tryVerifyPhoneNumberByAuthenticationCode(code);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isDataFilledCorrectly(String code) {
        if (Util.isDataNotFilled(code)) {
            if (isViewAttached()) {
                getView().showEmptyDataError();
            }

            return false;
        }
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private void tryVerifyPhoneNumberByAuthenticationCode(String code) {
        try {
            registerPresenter.verifyAuthCode(code);
        } catch (IllegalArgumentException e) {
            Logger.w("Exception occurred while verifying authentication code: " + e.getMessage());
            getView().showCodeInvalidError();
        }
    }
}
