package tattler.pro.tattler.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tattler.pro.tattler.R;
import tattler.pro.tattler.custom_ui.MaterialToast;

public class PhoneAuthenticationFragment extends MvpFragment<PhoneAuthenticationView, PhoneAuthenticationPresenter>
        implements PhoneAuthenticationView {
    @BindView(R.id.phoneNumberEditText)
    EditText phoneNumberArea;

    @BindView(R.id.userNameEditText)
    EditText userNameArea;

    @NonNull
    @Override
    public PhoneAuthenticationPresenter createPresenter() {
        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        assert registerActivity != null;
        return new PhoneAuthenticationPresenter(registerActivity.getPresenter());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_authentication, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.verifyPhoneButton)
    public void verifyPhoneNumber() {
        String phoneNumber = phoneNumberArea.getText().toString().trim();
        String userName = userNameArea.getText().toString().trim();
        getPresenter().verifyPhoneNumber(phoneNumber, userName);
    }

    @Override
    public void showEmptyDataError() {
        MaterialToast.makeText(getActivity(), getString(R.string.fillAllRequiredAreasInfo),
                Toast.LENGTH_LONG, MaterialToast.TYPE_WARNING).show();
    }

    @Override
    public void showPhoneNumberInvalidError() {
        MaterialToast.makeText(getActivity(), getString(R.string.phoneNumberInvalid),
                Toast.LENGTH_LONG, MaterialToast.TYPE_ERROR).show();
    }
}
