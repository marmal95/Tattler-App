package tattler.pro.tattler.login;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.authentication.FingerprintAuthenticator;

public class FingerprintLoginActivity extends MvpActivity<FingerprintLoginView, FingerprintLoginPresenter>
        implements FingerprintLoginView {

    @BindView(R.id.fingerprintIcon)
    ImageView fingerprintIcon;

    @BindView(R.id.infoLabel)
    TextView infoLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_login);
        ButterKnife.bind(this);
    }

    @NonNull
    @Override
    public FingerprintLoginPresenter createPresenter() {
        return new FingerprintLoginPresenter(new FingerprintAuthenticator(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().startAuthentication();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().stopAuthentication();
    }

    @Override
    public void indicateFingerprintAuthFail() {
        fingerprintIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorTextError));
        infoLabel.setTextColor(ContextCompat.getColor(this, R.color.colorTextError));
        infoLabel.setText(getString(R.string.authenticationFail));
        indicateToNormalWithDelay();
    }

    @Override
    public void indicateFingerprintAuthSuccess() {
        fingerprintIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorTextSuccess));
        infoLabel.setTextColor(ContextCompat.getColor(this, R.color.colorTextSuccess));
        infoLabel.setText(getString(R.string.authenticationSuccess));
    }

    @Override
    public void indicateFingerprintAuthHelp(String message) {
        infoLabel.setText(message);
        infoLabel.setTextColor(ContextCompat.getColor(this, R.color.colorTextHelp));
        indicateToNormalWithDelay();
    }

    @Override
    public void indicateFingerprintAuthError(String message) {
        fingerprintIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorTextError));
        infoLabel.setTextColor(ContextCompat.getColor(this, R.color.colorTextError));
        infoLabel.setText(message);
    }

    private void indicateToNormalWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            fingerprintIcon.setColorFilter(ContextCompat.getColor(
                    FingerprintLoginActivity.this, R.color.colorPrimaryText));
            infoLabel.setText(getString(R.string.touchFingerSensorToAppLogin));
            infoLabel.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryText));
        }, 3000);
    }
}
