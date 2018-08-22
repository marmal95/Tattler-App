package tattler.pro.tattler.main.chats;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.orhanobut.logger.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.LeaveChatsRequest;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;

public class ChatsPresenter extends MvpBasePresenter<ChatsView> {
    private ChatsAdapter chatsAdapter;
    private DatabaseManager databaseManager;
    private MainPresenter mainPresenter;
    private MessageFactory messageFactory;

    ChatsPresenter(
            ChatsAdapter chatsAdapter,
            DatabaseManager databaseManager,
            MainPresenter mainPresenter,
            MessageFactory messageFactory) {
        this.chatsAdapter = chatsAdapter;
        this.databaseManager = databaseManager;
        this.mainPresenter = mainPresenter;
        this.mainPresenter.setChatsPresenter(this);
        this.messageFactory = messageFactory;
    }

    @Override
    public void attachView(ChatsView view) {
        super.attachView(view);
        view.setChatsAdapter(chatsAdapter);
        initChats();

        Chat chat1 = new Chat(100, true, "ZIOMY", true);
        Chat chat2 = new Chat(101, true, "NOOBY", true);
        Chat chat3 = new Chat(102, true, "LESZCZE", true);
        try {
            databaseManager.insertChat(chat1, new ArrayList<>());
            databaseManager.insertChat(chat2, new ArrayList<>());
            databaseManager.insertChat(chat3, new ArrayList<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void onDestroy() {
        OpenHelperManager.releaseHelper();
    }

    public void handleChatCreated(Chat chat) {
        chatsAdapter.addChat(chat);
    }

    public void handleChatRemoved(Chat chat) {
        chatsAdapter.removeChat(chat);
    }


    public void handleChatClicked(int position) {
        if (chatsAdapter.isInSelectMode()) {
            chatsAdapter.toggleSelection(position);
        } else {
            startChatActivity(position);
        }
    }

    public void handleChatLongClick(int position) {
        chatsAdapter.toggleSelection(position);
    }

    public void handleChatsRemoveClick() {
        sendLeaveChatsRequest(chatsAdapter.getSelectedItems());
        List<Integer> chatsIndexes = chatsAdapter.getSelectedPositions();
        removeChats(chatsIndexes);
        chatsAdapter.clearSelection();
    }

    private void initChats() {
        try {
            List<Chat> chats = databaseManager.selectInitializedChats();
            chatsAdapter.clearChats();
            chatsAdapter.addChats(chats);
        } catch (SQLException e) {
            e.printStackTrace();
            showChatAddingError();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showChatAddingError() {
        if (isViewAttached()) {
            getView().showChatAddingError();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void startChatActivity(int position) {
        if (isViewAttached()) {
            getView().startChatActivity(chatsAdapter.getChat(position));
        }
    }

    private void removeChats(List<Integer> chatsIndexes) {
        Collections.reverse(chatsIndexes);
        chatsIndexes.forEach(index -> {
            Chat chat = chatsAdapter.getChat(index);
            try {
                chatsAdapter.removeChat(index);
                databaseManager.deleteChat(chat);
            } catch (SQLException e) {
                e.printStackTrace();
                Logger.e("Could not delete chat: " + chat.toString());
            }
        });
    }

    private void sendLeaveChatsRequest(List<Chat> selectedItems) {
        LeaveChatsRequest leaveChatsRequest = messageFactory.createLeaveChatsRequest(selectedItems);
        mainPresenter.sendMessage(leaveChatsRequest);
    }
}
