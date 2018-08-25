package tattler.pro.tattler.main.settings;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

public class SettingsPresenter extends MvpBasePresenter<SettingsView> {
    private SettingsManager settingsManager;

    public SettingsPresenter(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Override
    public void attachView(SettingsView view) {
        super.attachView(view);
        view.setNotificationState(settingsManager.isNotificationEnabled());
        view.setNotificationSoundState(settingsManager.isNotificationSoundEnabled());
        view.setNotificationVibrationState(settingsManager.isNotificationVibrationEnabled());
        view.setNotificationLightState(settingsManager.isNotificationLightEnabled());
    }

    public void onNotificationStateChange(boolean isChecked) {
        settingsManager.setNotificationState(isChecked);
    }

    public void onNotificationSoundStateChange(boolean isChecked) {
        settingsManager.setNotificationSoundState(isChecked);
    }

    public void onNotificationVibrationStateChanged(boolean isChecked) {
        settingsManager.setNotificationVibrationState(isChecked);
    }

    public void onNotificationLightStateChanged(boolean isChecked) {
        settingsManager.setNotificationLightState(isChecked);
    }
}
