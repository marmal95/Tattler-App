package tattler.pro.tattler.chat;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.messages.MessageFactory;

public class ChatPresenter extends MvpBasePresenter<ChatView> {
    private MessagesAdapter messagesAdapter;
    private DatabaseManager databaseManager;
    private MessageFactory messageFactory;

    ChatPresenter(MessagesAdapter messagesAdapter, DatabaseManager databaseManager, MessageFactory messageFactory) {
        this.messagesAdapter = messagesAdapter;
        this.databaseManager = databaseManager;
        this.messageFactory = messageFactory;
    }

    @SuppressWarnings("ConstantConditions")
    public void onCreate() {
        if (isViewAttached()) {
            getView().setMessagesAdapter(messagesAdapter);
        }
    }

    void onDestroy() {
        OpenHelperManager.releaseHelper();
    }
}
