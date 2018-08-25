package tattler.pro.tattler.main.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.AppPreferences;

public class SettingsFragment extends MvpFragment<SettingsView, SettingsPresenter>
        implements SettingsView {

    @BindView(R.id.notificationsOn)
    Switch notificationsSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        setListeners();
        return view;
    }

    @NonNull
    @Override
    public SettingsPresenter createPresenter() {
        return new SettingsPresenter(new SettingsManager(AppPreferences.getInstance(getActivity())));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setNotificationState(boolean isEnabled) {
        notificationsSwitch.setChecked(isEnabled);
    }

    private void setListeners() {
        notificationsSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> getPresenter().onNotificationStateChange(isChecked));
    }
}
