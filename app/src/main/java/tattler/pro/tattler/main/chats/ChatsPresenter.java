package tattler.pro.tattler.main.chats;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.orhanobut.logger.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import tattler.pro.tattler.common.ChatsManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.LeaveChatsRequest;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Message;

public class ChatsPresenter extends MvpBasePresenter<ChatsView> {
    private ChatsAdapter chatsAdapter;
    private MainPresenter mainPresenter;
    private MessageFactory messageFactory;
    private ChatsManager chatsManager;

    ChatsPresenter(
            ChatsAdapter chatsAdapter,
            DatabaseManager databaseManager,
            MainPresenter mainPresenter,
            MessageFactory messageFactory,
            ChatsManager chatsManager) {
        this.chatsAdapter = chatsAdapter;
        this.mainPresenter = mainPresenter;
        this.mainPresenter.setChatsPresenter(this);
        this.messageFactory = messageFactory;
        this.chatsManager = chatsManager;
        this.chatsManager.setDatabaseManager(databaseManager);
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

    public void handleChatModified(Chat modifiedChat) {
        OptionalInt chatPositionOpt = chatsAdapter.getPosition(modifiedChat);
        if (chatPositionOpt.isPresent()) {
            chatsAdapter.replaceChat(modifiedChat, chatPositionOpt.getAsInt());
        }
    }

    public void handleChatRemoved(Chat chat) {
        chatsAdapter.removeChat(chat);
    }

    public void handleChatMessageReceived(Message message) {
        Optional<Chat> optionalChat = chatsAdapter.getChatById(message.chat.chatId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            // chat.messages.add(message); HALO
            chatsAdapter.notifyDataSetChanged();
        }
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
        List<Chat> selectedChats = chatsAdapter.getSelectedItems();
        List<Integer> chatsIndexes = chatsAdapter.getSelectedPositions();

        if (selectedChats.isEmpty()) {
            Logger.d("No chats selected to remove. ");
            return;
        }

        sendLeaveChatsRequest(selectedChats);
        removeChatsFromView(chatsIndexes);

        chatsManager.removeChats(selectedChats);
        chatsAdapter.clearSelection();
    }

    public void handleMuteChatsClick() {
        List<Chat> selectedChats = chatsAdapter.getSelectedItems();
        if (selectedChats.isEmpty()) {
            Logger.d("No chats selected to mute. ");
            return;
        }

        chatsManager.toggleMuteChats(selectedChats);
        chatsAdapter.clearSelection();
    }

    public void handleBlockChatsClick() {
        List<Chat> selectedChats = chatsAdapter.getSelectedItems();
        if (selectedChats.isEmpty()) {
            Logger.d("No chats selected to block. ");
            return;
        }

        chatsManager.toggleBlockChats(selectedChats);
        chatsAdapter.clearSelection();
    }

    private void initChats() {
        List<Chat> chats = chatsManager.retrieveInitializedChats();
        chatsAdapter.clearChats();
        chatsAdapter.addChats(chats);
    }

    @SuppressWarnings("ConstantConditions")
    private void startChatActivity(int position) {
        if (isViewAttached()) {
            getView().startChatActivity(chatsAdapter.getChat(position));
        }
    }

    private void removeChatsFromView(List<Integer> chatsIndexes) {
        Collections.reverse(chatsIndexes);
        chatsIndexes.forEach(index -> chatsAdapter.removeChat(index));
    }

    private void sendLeaveChatsRequest(List<Chat> selectedItems) {
        LeaveChatsRequest leaveChatsRequest = messageFactory.createLeaveChatsRequest(selectedItems);
        mainPresenter.sendMessage(leaveChatsRequest);
    }
}
