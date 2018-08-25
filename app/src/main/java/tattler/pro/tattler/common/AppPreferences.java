package tattler.pro.tattler.common;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String SETTINGS_NAME = "app_settings";
    private static AppPreferences appPreferences;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static AppPreferences getInstance(Context context) {
        if (appPreferences == null) {
            appPreferences = new AppPreferences(context);
        }
        return appPreferences;
    }

    public void put(Key key, String val) {
        edit();
        editor.putString(key.name(), val);
        commit();
    }

    public void put(Key key, int val) {
        edit();
        editor.putInt(key.name(), val);
        commit();
    }

    public void put(Key key, boolean val) {
        edit();
        editor.putBoolean(key.name(), val);
        commit();
    }

    public void put(Key key, float val) {
        edit();
        editor.putFloat(key.name(), val);
        commit();
    }

    public void put(Key key, double val) {
        edit();
        editor.putString(key.name(), String.valueOf(val));
        commit();
    }

    public void put(Key key, long val) {
        edit();
        editor.putLong(key.name(), val);
        commit();
    }

    public String getString(Key key, String defaultValue) {
        return sharedPreferences.getString(key.name(), defaultValue);
    }

    public String getString(Key key) {
        return sharedPreferences.getString(key.name(), null);
    }

    public int getInt(Key key) {
        return sharedPreferences.getInt(key.name(), 0);
    }

    public int getInt(Key key, int defaultValue) {
        return sharedPreferences.getInt(key.name(), defaultValue);
    }

    public long getLong(Key key) {
        return sharedPreferences.getLong(key.name(), 0);
    }

    public long getLong(Key key, long defaultValue) {
        return sharedPreferences.getLong(key.name(), defaultValue);
    }

    public float getFloat(Key key) {
        return sharedPreferences.getFloat(key.name(), 0);
    }

    public float getFloat(Key key, float defaultValue) {
        return sharedPreferences.getFloat(key.name(), defaultValue);
    }

    public double getDouble(Key key) {
        return getDouble(key, 0);
    }

    public double getDouble(Key key, double defaultValue) {
        try {
            return Double.valueOf(sharedPreferences.getString(key.name(), String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public boolean getBoolean(Key key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key.name(), defaultValue);
    }

    public boolean getBoolean(Key key) {
        return sharedPreferences.getBoolean(key.name(), false);
    }

    public void remove(Key... keys) {
        edit();
        for (Key key : keys) {
            editor.remove(key.name());
        }
        commit();
    }

    public void clear() {
        edit();
        editor.clear();
        commit();
    }

    @SuppressWarnings("CommitPrefEdits")
    private void edit() {
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
    }

    private void commit() {
        if (editor != null) {
            editor.commit();
            editor = null;
        }
    }

    public enum Key {
        IS_FIRST_LAUNCH,
        USER_PHONE_NUMBER,
        USER_NAME,
        USER_NUMBER,

        IS_NOTIFICATION_ON
    }
}