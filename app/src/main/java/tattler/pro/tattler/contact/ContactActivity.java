package tattler.pro.tattler.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import tattler.pro.tattler.R;
import tattler.pro.tattler.chat.ChatActivity;
import tattler.pro.tattler.common.ChatsManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.custom_ui.MaterialToast;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.tcp.TcpConnectionService;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

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

    @BindView(R.id.startChat)
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

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy();
        super.onDestroy();
    }

    @NonNull
    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter(
                new TcpServiceManager(),
                new TcpServiceConnectorFactory(),
                OpenHelperManager.getHelper(this, DatabaseManager.class),
                new ChatsManager(),
                new MessageFactory(this));
    }

    @Override
    public void bindTcpConnectionService(TcpServiceConnector serviceConnector) {
        Intent intent = new Intent(this, TcpConnectionService.class);
        // startService(intent);
        bindService(intent, serviceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindTcpConnectionService(TcpServiceConnector serviceConnector) {
        unbindService(serviceConnector);
    }

    @Override
    public void loadContactData(Contact contact) {
        ColorDrawable userNameColorDrawable = new ColorDrawable(Color.parseColor(Util.pickHexColor(this, contact.contactName)));
        userAvatar.setImageDrawable(userNameColorDrawable);
        userInitials.setText(Util.extractUserInitials(contact.contactName));
        userName.setText(contact.contactName);
        userNumber.setText(String.valueOf(contact.contactNumber));
    }

    @Override
    public void startChatActivity(Chat chat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(IntentKey.CHAT.name(), chat);
        startActivity(intent);
    }

    @OnClick(R.id.startChat)
    public void startChat() {
        getPresenter().handleStartChat();
    }

    @OnClick(R.id.muteContact)
    public void muteContact() {}

    @OnClick(R.id.blockContact)
    public void blockContact() {}

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }
}
