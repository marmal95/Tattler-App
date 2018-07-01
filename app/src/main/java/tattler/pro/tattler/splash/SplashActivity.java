package tattler.pro.tattler.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.login.FingerprintLoginActivity;
import tattler.pro.tattler.register.RegisterActivity;


public class SplashActivity extends MvpActivity<SplashView, SplashPresenter> implements SplashView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter(new UserLoginStatusChecker(this));
    }

    @Override
    public void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void startFingerAuthActivity() {
        Intent intent = new Intent(this, FingerprintLoginActivity.class);
        startActivity(intent);
    }
}