package tattler.pro.tattler.main.chats;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.chat.ChatActivity;
import tattler.pro.tattler.common.ChatsManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.OnItemClickListener;
import tattler.pro.tattler.main.MainActivity;
import tattler.pro.tattler.messages.MessageFactory;
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
        setUpChatsView();
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
                activity.getPresenter(),
                new MessageFactory(activity),
                new ChatsManager());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chats_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.removeChatsAction:
                getPresenter().handleChatsRemoveClick();
                break;
            case R.id.muteChatsAction:
                getPresenter().handleMuteChatsClick();
                break;
            case R.id.blockChatsAction:
                getPresenter().handleBlockChatsClick();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setChatsAdapter(ChatsAdapter adapter) {
        chatsView.setAdapter(adapter);
    }

    @Override
    public void startChatActivity(Chat chat) {
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
        getPresenter().handleChatLongClick(position);
        return true;
    }

    private void setUpChatsView() {
        Context context = getActivity();
        Drawable dividerDrawable = ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.recycler_view_divider);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(dividerDrawable));
        chatsView.setHasFixedSize(true);
        chatsView.addItemDecoration(dividerItemDecoration);
        chatsView.setLayoutManager(layoutManager);
    }
}
