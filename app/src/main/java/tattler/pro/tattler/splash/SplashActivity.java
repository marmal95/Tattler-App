package tattler.pro.tattler.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.main.MainActivity;


public class SplashActivity extends MvpActivity<SplashView, SplashPresenter> implements SplashView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());

        // FIXME: Only for tests
        AppPreferences appPreferences = AppPreferences.getInstance(this);
        appPreferences.put(AppPreferences.Key.USER_PHONE_NUMBER, "111");
        appPreferences.put(AppPreferences.Key.USER_NAME, "$ONY");
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter(new UserLoginStatusChecker(this));
    }

    @Override
    public void startRegisterActivity() {
        // FIXME: Change to RegisterActivity after tests
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void startFingerAuthActivity() {
        // FIXME: Change to FingerAuthActivity after tests
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}