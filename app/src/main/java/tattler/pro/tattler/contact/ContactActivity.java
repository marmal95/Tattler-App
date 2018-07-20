package tattler.pro.tattler.contact;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.custom_ui.MaterialToast;
import tattler.pro.tattler.models.Contact;

public class ContactActivity extends MvpActivity<ContactView, ContactPresenter> implements ContactView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.userAvatar)
    CircleImageView userAvatar;

    @BindView(R.id.userInitials)
    TextView userInitials;

    @BindView(R.id.userName)
    TextView userName;

    @BindView(R.id.userNumber)
    TextView userNumber;

    @BindView(R.id.sendMessage)
    TextView userSendMessage;

    @BindView(R.id.muteContact)
    TextView userMute;

    @BindView(R.id.blockContact)
    TextView userBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        setUpToolbar();

        Contact contact = (Contact) getIntent().getSerializableExtra(IntentKey.CONTACT.name());
        getPresenter().onCreate(contact);
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

    @OnClick(R.id.sendMessage)
    public void startChat() {
        // TODO: Implement
        MaterialToast.makeText(this, "START_CHAT", Toast.LENGTH_LONG, MaterialToast.TYPE_INFO).show();
    }

    @OnClick(R.id.muteContact)
    public void muteContact() {
        // TODO: Implement
        MaterialToast.makeText(this, "MUTE_CONTACT", Toast.LENGTH_LONG, MaterialToast.TYPE_WARNING).show();
    }

    @OnClick(R.id.blockContact)
    public void blockContact() {
        // TODO: Implement
        MaterialToast.makeText(this, "BLOCK_CONTACT", Toast.LENGTH_LONG, MaterialToast.TYPE_ERROR).show();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

}
