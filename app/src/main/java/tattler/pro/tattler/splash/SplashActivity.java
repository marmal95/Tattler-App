package tattler.pro.tattler.splash;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import tattler.pro.tattler.R;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.NotificationBuilder;
import tattler.pro.tattler.login.FingerprintLoginActivity;
import tattler.pro.tattler.register.RegisterActivity;


public class SplashActivity extends MvpActivity<SplashView, SplashPresenter> implements SplashView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());

//        AppPreferences.getInstance(this).clear();
//        AppPreferences.getInstance(this).put(AppPreferences.Key.USER_NAME, "Stefan");
//        AppPreferences.getInstance(this).put(AppPreferences.Key.USER_PHONE_NUMBER, "+48518834914");
//        AppPreferences.getInstance(this).put(AppPreferences.Key.USER_NUMBER, 6286375);

        createNotificationChannel();
    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return new SplashPresenter(new UserLoginStatusChecker(this));
    }

    @Override
    public void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void startFingerAuthActivity() {
        Intent intent = new Intent(this, FingerprintLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void createNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(
                NotificationBuilder.CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);
    }
}