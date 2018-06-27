package tattler.pro.tattler.register;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;

public class RegisterActivity extends FragmentActivity {
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

    public void launchPhoneAuthenticationFragment() {
        mPager.setCurrentItem(PHONE_AUTH_FRAGMENT_IDX);
    }

    public void launchCodeVerificationFragment() {
        mPager.setCurrentItem(CODE_VERIFY_FRAGMENT_IDX);
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