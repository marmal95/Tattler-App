package tattler.pro.tattler.main.settings;

import tattler.pro.tattler.common.AppPreferences;

public class SettingsManager {
    private AppPreferences appPreferences;

    public SettingsManager(AppPreferences appPreferences) {
        this.appPreferences = appPreferences;
    }

    public void setNotificationState(boolean isChecked) {
        appPreferences.put(AppPreferences.Key.IS_NOTIFICATION_ON, isChecked);
    }

    public void setNotificationSoundState(boolean isChecked) {
        appPreferences.put(AppPreferences.Key.IS_NOTIFICATION_SOUND_ON, isChecked);
    }

    public void setNotificationVibrationState(boolean isChecked) {
        appPreferences.put(AppPreferences.Key.IS_NOTIFICATION_VIBRATION_ON, isChecked);
    }

    public void setNotificationLightState(boolean isChecked) {
        appPreferences.put(AppPreferences.Key.IS_NOTIFICATION_LIGHT_ON, isChecked);
    }

    public boolean isNotificationEnabled() {
        return appPreferences.getBoolean(AppPreferences.Key.IS_NOTIFICATION_ON, true);
    }

    public boolean isNotificationSoundEnabled() {
        return appPreferences.getBoolean(AppPreferences.Key.IS_NOTIFICATION_SOUND_ON, true);
    }

    public boolean isNotificationVibrationEnabled() {
        return appPreferences.getBoolean(AppPreferences.Key.IS_NOTIFICATION_VIBRATION_ON, true);
    }

    public boolean isNotificationLightEnabled() {
        return appPreferences.getBoolean(AppPreferences.Key.IS_NOTIFICATION_LIGHT_ON, true);
    }
}
