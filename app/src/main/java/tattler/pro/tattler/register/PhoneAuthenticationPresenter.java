package tattler.pro.tattler.register;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class PhoneAuthenticationPresenter extends MvpBasePresenter<PhoneAuthenticationView> {
    private RegisterPresenter registerPresenter;

    PhoneAuthenticationPresenter(RegisterPresenter registerPresenter) {
        this.registerPresenter = registerPresenter;
    }

    public void verifyPhoneNumber(String phoneNumber) {
        registerPresenter.verifyPhoneNumber(phoneNumber);
    }

    public void rememberUserData(String phoneNumber, String userName) {
        registerPresenter.rememberUserData(new UserRegisterData(userName, phoneNumber));
    }
}
