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

    public boolean isNotificationEnabled() {
        return appPreferences.getBoolean(AppPreferences.Key.IS_NOTIFICATION_ON, true);
    }
}
