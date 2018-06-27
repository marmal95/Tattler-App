package tattler.pro.tattler.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import tattler.pro.tattler.R;

public class PhoneAuthenticationFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_authentication, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.verifyPhoneButton)
    public void verifyPhoneNumber() {
        // TOD: Trigger phone number verification
        RegisterActivity registerActivity = ((RegisterActivity) getActivity());
        assert registerActivity != null;
        registerActivity.launchCodeVerificationFragment();
    }
}
