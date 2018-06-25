package tattler.pro.tattler.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import tattler.pro.tattler.R;
import tattler.pro.tattler.security.FingerprintAuthenticator;
import tattler.pro.tattler.util.MaterialToast;

public class FingerprintLoginActivity extends MvpActivity<FingerprintLoginView, FingerprintLoginPresenter>
        implements FingerprintLoginView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_login);
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
    public void showAuthSucceedToast() {
        MaterialToast.makeText(
                this, getString(R.string.authenticationSuccess), Toast.LENGTH_SHORT, MaterialToast.TYPE_SUCCESS).show();
    }

    @Override
    public void showAuthFailToast() {
        MaterialToast.makeText(
                this, getString(R.string.authenticationFail), Toast.LENGTH_SHORT, MaterialToast.TYPE_WARNING).show();
    }

    @Override
    public void showAuthErrorToast(String message) {
        MaterialToast.makeText(
                this, getString(R.string.authenticationError, message), Toast.LENGTH_SHORT, MaterialToast.TYPE_ERROR).show();
    }

    @Override
    public void showAuthHelpToast(String message) {
        MaterialToast.makeText(
                this, getString(R.string.authenticationHelp, message), Toast.LENGTH_SHORT, MaterialToast.TYPE_INFO).show();
    }
}
