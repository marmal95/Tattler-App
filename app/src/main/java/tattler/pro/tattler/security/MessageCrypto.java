package tattler.pro.tattler.security;


import com.orhanobut.logger.Logger;

import java.util.Optional;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.messages.ChatMessage;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.models.Chat;

public class MessageCrypto {
    private DatabaseManager databaseManager;

    public MessageCrypto(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Message encryptMessage(Message message) throws Exception {
        if (message.messageType == Message.Type.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) message;
            Optional<Chat> chat = databaseManager.selectChatById(chatMessage.chatId);
            if (chat.isPresent()) {
                AesCrypto aesCrypto = new AesCrypto();
                aesCrypto.init(chat.get().chatKey);
                chatMessage.content = aesCrypto.encrypt(chatMessage.content);
            }
            Logger.d("Encrypted ChatMessage to send: " + message.toString());
        }

        return message;
    }

    public Message decryptMessage(Message message) throws Exception {
        if (message.messageType == Message.Type.CHAT_MESSAGE) {
            ChatMessage chatMessage = (ChatMessage) message;
            Optional<Chat> chat = databaseManager.selectChatById(chatMessage.chatId);
            if (chat.isPresent()) {
                AesCrypto aesCrypto = new AesCrypto();
                aesCrypto.init(chat.get().chatKey);
                chatMessage.content = aesCrypto.decrypt(chatMessage.content);
            }
            Logger.d("Decrypted ChatMessage to handle: " + message.toString());
        }

        return message;
    }
}
