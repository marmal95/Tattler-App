package tattler.pro.tattler.common;

import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;

import java.sql.SQLException;
import java.util.Optional;

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
            // TODO: Handle error -> Inform user
        }
    }
}
