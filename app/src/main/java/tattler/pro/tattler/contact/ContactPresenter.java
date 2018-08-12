package tattler.pro.tattler.contact;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.orhanobut.logger.Logger;

import java.util.Optional;

import tattler.pro.tattler.common.ChatsManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.messages.CreateChatRequest;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

public class ContactPresenter extends MvpBasePresenter<ContactView> {
    private TcpServiceManager tcpServiceManager;
    private TcpServiceConnector tcpServiceConnector;
    private ChatsManager chatsManager;
    private MessageFactory messageFactory;

    private Contact contact;

    ContactPresenter(TcpServiceManager tcpManager,
                     TcpServiceConnectorFactory serviceConnectorFactory,
                     DatabaseManager databaseManager,
                     ChatsManager chatsManager,
                     MessageFactory messageFactory) {
        this.tcpServiceManager = tcpManager;
        this.tcpServiceConnector = serviceConnectorFactory.create(tcpServiceManager);
        this.chatsManager = chatsManager;
        this.chatsManager.setDatabaseManager(databaseManager);
        this.messageFactory = messageFactory;
    }

    @SuppressWarnings("ConstantConditions")
    public void onCreate(Contact contact) {
        this.contact = contact;

        if (isViewAttached()) {
            getView().loadContactData(contact);
        }

        if (!tcpServiceManager.isServiceBound()) {
            bindTcpConnectionService();
        }
    }

    public void onDestroy() {
        OpenHelperManager.releaseHelper();
        if (tcpServiceManager.isServiceBound()) {
            unbindTcpConnectionService();
        }
    }

    public void handleStartChat() {
        Optional<Chat> optionalChat = chatsManager.retrieveIndividualChat(contact);
        if (optionalChat.isPresent()) {
            Logger.d("Retrieved existed individual chat for: " + contact.toString());
            startChat(optionalChat.get());
        } else {
            Logger.d("Individual chat does not exist for: " + contact.toString());
            CreateChatRequest chatRequest = messageFactory.createCreateChatRequest(contact);
            tcpServiceManager.getTcpService().sendMessage(chatRequest);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void bindTcpConnectionService() {
        if (isViewAttached()) {
            Logger.d("Binding TcpConnectionService.");
            getView().bindTcpConnectionService(tcpServiceConnector);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void unbindTcpConnectionService() {
        if (isViewAttached()) {
            Logger.d("Unbinding TcpConnectionService.");
            getView().unbindTcpConnectionService(tcpServiceConnector);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void startChat(Chat chat) {
        if (isViewAttached()) {
            getView().startChatActivity(chat);
        }
    }
}
