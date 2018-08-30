package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;

import tattler.pro.tattler.common.Util;

public class PhoneAuthenticationPresenter extends MvpBasePresenter<PhoneAuthenticationView> {
    private RegisterPresenter registerPresenter;

    PhoneAuthenticationPresenter(RegisterPresenter registerPresenter) {
        this.registerPresenter = registerPresenter;
    }

    public void verifyPhoneNumber(String phoneNumber, String userName) {
        if (!isDataFilledCorrectly(phoneNumber, userName))
            return;

        rememberUserData(phoneNumber, userName);
        tryVerifyUserPhoneNumber(phoneNumber);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isDataFilledCorrectly(String phoneNumber, String userName) {
        if (Util.isDataNotFilled(phoneNumber, userName)) {
            if (isViewAttached()) {
                getView().showEmptyDataError();
            }
            return false;
        }
        return true;
    }

    private void rememberUserData(String phoneNumber, String userName) {
        registerPresenter.rememberUserData(new UserRegisterData(userName, phoneNumber));
    }

    @SuppressWarnings("ConstantConditions")
    private void tryVerifyUserPhoneNumber(String phoneNumber) {
        try {
            registerPresenter.verifyPhoneNumber(phoneNumber);
        } catch (IllegalArgumentException e) {
            Logger.w("Exception occurred while verifying phone number: " + e.getMessage());
            getView().showPhoneNumberInvalidError();
        }
    }
}
