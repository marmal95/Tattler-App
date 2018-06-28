package tattler.pro.tattler.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import tattler.pro.tattler.R;

public class PhoneAuthenticationFragment extends MvpFragment<PhoneAuthenticationView, PhoneAuthenticationPresenter> {

    @BindView(R.id.phoneNumberEditText)
    EditText phoneNumberArea;

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
        getPresenter().verifyPhoneNumber(phoneNumber);
    }
}
