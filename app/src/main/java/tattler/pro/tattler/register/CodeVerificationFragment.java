package tattler.pro.tattler.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tattler.pro.tattler.R;

public class CodeVerificationFragment extends MvpFragment<CodeVerificationView, CodeVerificationPresenter> {

    @BindView(R.id.verificationCodeArea)
    EditText verificationCodeArea;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_verification, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @NonNull
    @Override
    public CodeVerificationPresenter createPresenter() {
        RegisterActivity registerActivity = (RegisterActivity) getActivity();
        assert registerActivity != null;
        return new CodeVerificationPresenter(registerActivity.getPresenter());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.verifyCodeButton)
    public void checkVerificationCode() {
        String code = verificationCodeArea.getText().toString().trim();
        getPresenter().checkVerificationCode(code);
    }
}
