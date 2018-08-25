package tattler.pro.tattler.main.settings;

import com.hannesdorfmann.mosby.mvp.MvpView;

public interface SettingsView extends MvpView {
    void setNotificationState(boolean isEnabled);
}
