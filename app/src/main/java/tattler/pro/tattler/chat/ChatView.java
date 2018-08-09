package tattler.pro.tattler.chat;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface ChatView extends MvpView {
    void setMessagesAdapter(MessagesAdapter adapter);
}
