package tattler.pro.tattler.main.contacts;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import tattler.pro.tattler.R;
import tattler.pro.tattler.main.MainActivity;

public class AddContactDialog extends Dialog {
    @BindView(R.id.tattlerNumberArea)
    EditText tattlerNumberArea;

    @BindView(R.id.userNameArea)
    EditText userNameArea;

    @BindView(R.id.addContactButton)
    Button addContactButton;

    private MainActivity activity;

    AddContactDialog(@NonNull MainActivity activity) {
        super(activity, R.style.PrimaryDialogStyle);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_contact);
    }

    @OnClick(R.id.addContactButton)
    public void addContactButtonClick() {

    }
}
