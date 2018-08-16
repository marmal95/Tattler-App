package tattler.pro.tattler.main.chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.chat.ChatActivity;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.OnItemClickListener;
import tattler.pro.tattler.custom_ui.MaterialToast;
import tattler.pro.tattler.main.MainActivity;
import tattler.pro.tattler.models.Chat;

public class ChatsFragment extends MvpFragment<ChatsView, ChatsPresenter>
        implements ChatsView, OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView chatsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        ButterKnife.bind(this, view);
        chatsView.setHasFixedSize(true);
        chatsView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @NonNull
    @Override
    public ChatsPresenter createPresenter() {
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        return new ChatsPresenter(
                new ChatsAdapter(activity, new ArrayList<>(), this),
                OpenHelperManager.getHelper(activity, DatabaseManager.class),
                activity.getPresenter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }


    @Override
    public void setChatsAdapter(ChatsAdapter adapter) {
        chatsView.setAdapter(adapter);
    }

    @Override
    public void showChatAddingError() {
        MaterialToast.makeText(getActivity(), getString(R.string.chatCreatingError),
                Toast.LENGTH_LONG, MaterialToast.TYPE_ERROR).show();
    }

    @Override
    public void startChat(Chat chat) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(IntentKey.CHAT.name(), chat);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        getPresenter().handleChatClicked(position);
    }

    @Override
    public boolean onItemLongClick(int position) {
        return false;
    }
}
