package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface PhoneAuthenticationView extends MvpView {
    void showEmptyDataError();
    void showPhoneNumberInvalidError();
}
