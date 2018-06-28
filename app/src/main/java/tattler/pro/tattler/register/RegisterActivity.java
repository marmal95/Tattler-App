package tattler.pro.tattler.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import tattler.pro.tattler.R;
import tattler.pro.tattler.authentication.PhoneAuthenticator;
import tattler.pro.tattler.custom_ui.MaterialToast;

public class RegisterActivity extends MvpActivity<RegisterView, RegisterPresenter> implements RegisterView {
    private static final int NUM_PAGES = 2;
    private static final int PHONE_AUTH_FRAGMENT_IDX = 0;
    private static final int CODE_VERIFY_FRAGMENT_IDX = 1;

    @BindView(R.id.pager)
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        ButterKnife.bind(this);

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter(new PhoneAuthenticator(this));
    }

    @Override
    public void launchPhoneAuthenticationFragment() {
        runOnUiThread(() -> mPager.setCurrentItem(PHONE_AUTH_FRAGMENT_IDX));
    }

    @Override
    public void launchCodeVerificationFragment() {
        runOnUiThread(() -> mPager.setCurrentItem(CODE_VERIFY_FRAGMENT_IDX));
    }

    @Override
    public void onPhoneAuthenticationCompleted() {
        MaterialToast.makeText(this, "Phone Authentication Completed", Toast.LENGTH_LONG, MaterialToast.TYPE_SUCCESS).show();
    }

    @Override
    public void onPhoneAuthenticationFailed() {
        MaterialToast.makeText(this, "Phone Authentication Failed", Toast.LENGTH_LONG, MaterialToast.TYPE_ERROR).show();
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