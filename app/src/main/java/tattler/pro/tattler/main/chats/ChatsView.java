package tattler.pro.tattler.main.chats;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface ChatsView extends MvpView {
    void setChatsAdapter(ChatsAdapter adapter);
    void showChatAddingError();
}
