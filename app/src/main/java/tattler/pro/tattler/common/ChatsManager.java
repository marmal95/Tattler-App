package tattler.pro.tattler.common;

import com.orhanobut.logger.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;

public class ChatsManager {
    private DatabaseManager databaseManager;

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Optional<Chat> retrieveIndividualChat(Contact contact) {
        try {
            return databaseManager.getIndividualChat(contact);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void toggleMuteChats(List<Chat> chats) {
        chats.forEach(chat -> {
            try {
                chat.isMuted = !chat.isMuted;
                databaseManager.updateChat(chat);
            } catch (SQLException e) {
                e.printStackTrace();
                Logger.e("Error occurred while updating chat.");
            }
        });
    }

    public void toggleBlockChats(List<Chat> chats) {
        chats.forEach(chat -> {
            try {
                chat.isBlocked = !chat.isBlocked;
                databaseManager.updateChat(chat);
            } catch (SQLException e) {
                e.printStackTrace();
                Logger.e("Error occurred while updating chat.");
            }
        });
    }

    public void removeChats(List<Chat> chats) {
        chats.forEach(chat -> {
            try {
                databaseManager.deleteChat(chat);
            } catch (SQLException e) {
                e.printStackTrace();
                Logger.e("Error occurred while deleting chat.");
            }
        });
    }

    public List<Chat> retrieveInitializedChats() {
        try {
            return databaseManager.selectInitializedChats();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.e("Exception occurred while selecting chats.");
            return new ArrayList<>();
        }
    }
}
