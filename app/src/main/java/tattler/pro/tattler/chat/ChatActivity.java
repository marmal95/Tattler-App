package tattler.pro.tattler.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.ChatsManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.OnItemClickListener;
import tattler.pro.tattler.common.PickedImageView;
import tattler.pro.tattler.common.RequestCode;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Message;
import tattler.pro.tattler.tcp.MessageBroadcastReceiver;
import tattler.pro.tattler.tcp.TcpConnectionService;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

public class ChatActivity extends MvpActivity<ChatView, ChatPresenter>
        implements ChatView, OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.messagesView)
    RecyclerView messagesView;

    @BindView(R.id.messageInputArea)
    EditText messageInputArea;

    @BindView(R.id.sendMessageButton)
    ImageButton sendMessageButton;

    @BindView(R.id.addAttachmentButton)
    ImageButton addAttachmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpMessagesView();
        setUpToolbar();
    }

    @Override
    protected void onDestroy() {
        getPresenter().onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getPresenter().onCreate();
    }

    @NonNull
    @Override
    public ChatPresenter createPresenter() {
        Chat chat = (Chat) getIntent().getSerializableExtra(IntentKey.CHAT.name());
        return new ChatPresenter(
                new TcpServiceManager(),
                new TcpServiceConnectorFactory(),
                new ChatMessageHandler(),
                new MessageBroadcastReceiver(),
                new MessagesAdapter(this, new ArrayList<>(), this),
                new MessageFactory(this),
                chat,
                OpenHelperManager.getHelper(this, DatabaseManager.class),
                new ChatsManager());
    }

    @Override
    public void bindTcpConnectionService(TcpServiceConnector serviceConnector) {
        Intent intent = new Intent(this, TcpConnectionService.class);
        startService(intent);
        bindService(intent, serviceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindTcpConnectionService(TcpServiceConnector serviceConnector) {
        unbindService(serviceConnector);
    }

    @Override
    public void registerReceiver(MessageBroadcastReceiver receiver) {
        super.registerReceiver(receiver, receiver.createBroadcastMessageIntentFilter());
    }

    @Override
    public void unregisterReceiver(MessageBroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }

    @Override
    public void setMessagesAdapter(MessagesAdapter adapter) {
        messagesView.setAdapter(adapter);
    }

    @Override
    public void setTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    @Override
    public void scrollToPosition(int position) {
        messagesView.post(() -> messagesView.smoothScrollToPosition(position));
    }

    @Override
    public void pickImage() {
        Intent image = new Intent(Intent.ACTION_GET_CONTENT);
        image.setType("image/*");
        startActivityForResult(image, RequestCode.PICK_IMAGE);
    }

    @Override
    public void disableChat() {
        messageInputArea.setVisibility(View.GONE);
        sendMessageButton.setVisibility(View.GONE);
        addAttachmentButton.setVisibility(View.GONE);
    }

    @Override
    public void showImagePreview(Message message) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra(IntentKey.IMAGE_MESSAGE.name(), message);
        startActivity(intent);
    }

    @OnClick(R.id.sendMessageButton)
    public void handleSendMessageClick() {
        String messageText = messageInputArea.getText().toString();
        messageInputArea.getText().clear();
        getPresenter().handleSendMessage(messageText);
    }

    @OnClick(R.id.addAttachmentButton)
    public void handleAddAttachmentClick() {
        getPresenter().handleSendAttachment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.PICK_IMAGE) {
            if (data != null) {
                getPresenter().onImagePicked(new PickedImageView(this, data.getData()));
            }
        }
    }

    private void setUpMessagesView() {
        messagesView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messagesView.setLayoutManager(layoutManager);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onItemClick(int position) {
        getPresenter().handleMessageClick(position);
    }

    @Override
    public boolean onItemLongClick(int position) {
        return false;
    }
}
