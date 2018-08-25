package tattler.pro.tattler.main.settings;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface SettingsView extends MvpView {
    void setNotificationState(boolean isEnabled);
    void setNotificationSoundState(boolean isEnabled);
    void setNotificationVibrationState(boolean isEnabled);
    void setNotificationLightState(boolean isEnabled);
}
