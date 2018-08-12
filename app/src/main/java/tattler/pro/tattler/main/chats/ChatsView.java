package tattler.pro.tattler.main.chats;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tattler.pro.tattler.models.Chat;

interface ChatsView extends MvpView {
    void setChatsAdapter(ChatsAdapter adapter);
    void showChatAddingError();
    void startChat(Chat chat);
}
