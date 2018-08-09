package tattler.pro.tattler.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.authentication.PhoneAuthenticator;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.custom_ui.MaterialToast;
import tattler.pro.tattler.login.FingerprintLoginActivity;

public class RegisterActivity extends MvpActivity<RegisterView, RegisterPresenter> implements RegisterView {
    private static final int NUM_PAGES = 2;
    private static final int PHONE_AUTH_FRAGMENT_IDX = 0;
    private static final int CODE_VERIFY_FRAGMENT_IDX = 1;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter(
                new PhoneAuthenticator(this),
                AppPreferences.getInstance(this));
    }

    @Override
    public void startPhoneAuthenticationFragment() {
        runOnUiThread(() -> viewPager.setCurrentItem(PHONE_AUTH_FRAGMENT_IDX));
    }

    @Override
    public void startCodeVerificationFragment() {
        runOnUiThread(() -> viewPager.setCurrentItem(CODE_VERIFY_FRAGMENT_IDX));
    }

    @Override
    public void startFingerAuthActivity() {
        Intent intent = new Intent(this, FingerprintLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPhoneAuthenticationCompleted() {
        runOnUiThread(() -> MaterialToast.makeText(
                RegisterActivity.this, "Phone Authentication Completed", Toast.LENGTH_LONG, MaterialToast.TYPE_SUCCESS).show());
    }

    @Override
    public void onPhoneAuthenticationFailed() {
        runOnUiThread(() -> MaterialToast.makeText(
                RegisterActivity.this, "Phone Authentication Failed", Toast.LENGTH_LONG, MaterialToast.TYPE_ERROR).show());
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                default:
                    return new PhoneAuthenticationFragment();
                case 1:
                    return new CodeVerificationFragment();

            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}