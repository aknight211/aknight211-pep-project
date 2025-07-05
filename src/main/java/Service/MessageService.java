package Service;

import DAO.MessageDAO;
import Model.Message;
import Model.Account;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountService accountService;

    public MessageService(MessageDAO messageDAO, AccountService accountService) {
        this.messageDAO = messageDAO;
        this.accountService = accountService;
    }

    public Message newMessage(Message message){
        if (message.getMessage_text() == null || message.getMessage_text().isBlank()) {
            return null;
        }

        if (message.getMessage_text().length() > 255) {
            return null;
        }

        Account account = accountService.getAccountById(message.getPosted_by());
        if (account == null) {
            return null;
        }

        return messageDAO.newMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    } 

    public Message getMessageById(int message_id){
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessage(int message_id){
        Message message = messageDAO.getMessageById(message_id);
        if(message != null && messageDAO.deleteMessageById(message_id)){
            return message;
        }
        return null;
    }
    
    public Message updateMessage(int message_id, String newText){
        if (newText == null || newText.trim().isEmpty()) {
            return null;
        }
        Message exist = messageDAO.getMessageById(message_id);
        if(exist == null){
            return null;
        }
        return messageDAO.updateMessage(message_id, newText);
    }

    public List<Message> getMessagesByAccountId(int account_id){
        return messageDAO.getMessageByAccountId(account_id);
    }
}