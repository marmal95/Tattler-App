package tattler.pro.tattler.main.contacts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tattler.pro.tattler.R;

public class AddContactDialog extends Dialog {
    @BindView(R.id.tattlerNumberArea)
    EditText tattlerNumberArea;

    @BindView(R.id.userNameArea)
    EditText userNameArea;

    @BindView(R.id.addContactButton)
    Button addContactButton;

    private ContactsPresenter presenter;

    AddContactDialog(@NonNull Context context, ContactsPresenter presenter) {
        super(context, R.style.PrimaryDialogStyle);
        this.presenter = presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_contact);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addContactButton)
    public void addContactButtonClick() {
        String userNumber = tattlerNumberArea.getText().toString().trim();
        String userName = userNameArea.getText().toString().trim();
        presenter.handleAddNewContact(userNumber, userName);
        dismiss();
    }
}
