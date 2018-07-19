package tattler.pro.tattler.contact;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.models.Contact;

public class ContactActivity extends MvpActivity<ContactView, ContactPresenter> implements ContactView {
    @BindView(R.id.userAvatar)
    CircleImageView userAvatar;

    @BindView(R.id.userInitials)
    TextView userInitials;

    @BindView(R.id.userName)
    TextView userName;

    @BindView(R.id.userNumber)
    TextView userNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        Contact contact = (Contact) getIntent().getSerializableExtra(IntentKey.CONTACT.name());
        getPresenter().onCreate(contact);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }

    @NonNull
    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @Override
    public void loadContactData(Contact contact) {
        ColorDrawable userNameColorDrawable = new ColorDrawable(Color.parseColor(Util.pickHexColor(this, contact.contactName)));
        userAvatar.setImageDrawable(userNameColorDrawable);
        userInitials.setText(Util.extractUserInitials(contact.contactName));
        userName.setText(contact.contactName);
        userNumber.setText(String.valueOf(contact.contactNumber));
    }
}
