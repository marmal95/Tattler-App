package tattler.pro.tattler.main.chats;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.CreateChatResponse;
import tattler.pro.tattler.models.Chat;

public class ChatsPresenter extends MvpBasePresenter<ChatsView> {
    private ChatsAdapter chatsAdapter;
    private DatabaseManager databaseManager;
    private MainPresenter mainPresenter;

    ChatsPresenter(ChatsAdapter chatsAdapter, DatabaseManager databaseManager, MainPresenter mainPresenter) {
        this.chatsAdapter = chatsAdapter;
        this.databaseManager = databaseManager;
        this.mainPresenter = mainPresenter;
        this.mainPresenter.setChatsPresenter(this);
    }

    @Override
    public void attachView(ChatsView view) {
        super.attachView(view);
        view.setChatsAdapter(chatsAdapter);
        initChats();
    }

    public void onDestroy() {
        OpenHelperManager.releaseHelper();
    }

    public void handleCreateChatResponse(CreateChatResponse message) {
        // TODO: Extract from DB ? (should be already inserted
        Chat chat = new Chat(message.chatId, message.isGroupChat, message.chatName, false);
        chatsAdapter.addChat(chat);
    }

    private void initChats() {
        try {
            List<Chat> chats = databaseManager.selectInitializedChats();
            chatsAdapter.clearChats();
            chatsAdapter.addChats(chats);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
