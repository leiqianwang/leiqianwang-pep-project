package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.MessageDao;
import Model.Message;
import Service.AccountService;

public class MessageService {

    MessageDao messageDao;
    AccountService accountService;

          public MessageService() {
          this.messageDao = new MessageDao();
          this.accountService = new AccountService();
          }

    public Message createMessage(Message message) {
       // Validate message_text is not empty or too long
       if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
        return null; // Empty message text
    }
    if (message.getMessage_text().length() > 255) {
        return null; // Message text too long
    }

    // Pass the message to the DAO for creation, which also validates the user existence
    try {
        return messageDao.createMessage(message);
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}


    public List<Message> getAllMessage() {
       return messageDao.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDao.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        return messageDao.deleteMessage(messageId);
    }

    public Message updateMessage(int messageId, Message updatedMessage) {
        // Validate message_text is not empty or too long
        if (updatedMessage.getMessage_text() == null || updatedMessage.getMessage_text().trim().isEmpty()) {
            return null; // Message text is empty
        }
        if (updatedMessage.getMessage_text().length() > 255) {
            return null; // Message text is too long
        }
    
        // Pass the updated message to the DAO for updating
        return messageDao.updateMessage(messageId, updatedMessage);
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDao.getMessagesByAccountId(accountId);
    }

    
    
}
