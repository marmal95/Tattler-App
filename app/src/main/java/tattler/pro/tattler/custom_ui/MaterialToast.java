package tattler.pro.tattler.custom_ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tattler.pro.tattler.R;


public class MaterialToast extends Toast {

    public static final int TYPE_INFO = 0;
    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_WARNING = 2;
    public static final int TYPE_ERROR = 3;

    public static int LENGTH_LONG = Toast.LENGTH_LONG;
    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;

    private Context context;
    private View view;
    private int type;

    private MaterialToast(Context context) {
        super(context);
        this.context = context;
    }

    public static MaterialToast makeText(Context context, String message) {
        return makeText(context, message, LENGTH_SHORT, TYPE_INFO);
    }

    public static MaterialToast makeText(Context context, String message, int duration) {
        return makeText(context, message, duration, TYPE_INFO);
    }

    public static MaterialToast makeText(Context context, String message, int duration, int type) {
        MaterialToast MaterialToast = new MaterialToast(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.custom_toast_container, null);

        ImageView icon = view.findViewById(R.id.icon);
        TextView text = view.findViewById(R.id.text);

        switch (type) {
            case TYPE_SUCCESS:
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle));
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_success_background));
                break;
            case TYPE_WARNING:
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_warning_white_24dp));
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_warn_background));
                break;
            case TYPE_ERROR:
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_error_white_24dp));
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_error_background));
                break;
            default:
                icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_info_white_24dp));
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.custom_toast_info_background));
                break;
        }

        text.setText(message);
        MaterialToast.setDuration(duration);
        MaterialToast.setView(view);

        MaterialToast.view = view;
        MaterialToast.type = type;
        return MaterialToast;
    }

    @Override
    public void setText(@StringRes int resId) {
        setText(context.getString(resId));
    }

    @Override
    public void setText(CharSequence s) {
        if (view == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = view.findViewById(R.id.text);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    public void setIcon(@DrawableRes int iconId) {
        setIcon(ContextCompat.getDrawable(context, iconId));
    }

    public void setIcon(Drawable icon) {
        if (view == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        ImageView iv = view.findViewById(R.id.icon);
        if (iv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        iv.setImageDrawable(icon);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}