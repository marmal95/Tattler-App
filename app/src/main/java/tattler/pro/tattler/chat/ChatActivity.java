package tattler.pro.tattler.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.messages.MessageFactory;

public class ChatActivity extends MvpActivity<ChatView, ChatPresenter> implements ChatView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.messagesView)
    RecyclerView messagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupMessagesView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getPresenter().onCreate();
    }

    @NonNull
    @Override
    public ChatPresenter createPresenter() {
        return new ChatPresenter(
                new MessagesAdapter(this, new ArrayList<>()),
                OpenHelperManager.getHelper(this, DatabaseManager.class),
                new MessageFactory(this));
    }

    @Override
    public void setMessagesAdapter(MessagesAdapter adapter) {
        messagesView.setAdapter(adapter);
    }

    private void setupMessagesView() {
        messagesView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messagesView.setLayoutManager(layoutManager);
    }

}
