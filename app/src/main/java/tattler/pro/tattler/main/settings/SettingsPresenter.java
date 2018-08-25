package tattler.pro.tattler.main.settings;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class SettingsPresenter extends MvpBasePresenter<SettingsView> {
    private SettingsManager settingsManager;

    SettingsPresenter(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Override
    public void attachView(SettingsView view) {
        super.attachView(view);
        view.setNotificationState(settingsManager.isNotificationEnabled());
    }

    public void onNotificationStateChange(boolean isChecked) {
        settingsManager.setNotificationState(isChecked);
    }
}
