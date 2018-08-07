package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface CodeVerificationView extends MvpView {
    void showCodeInvalidOrEmptyError();
}
