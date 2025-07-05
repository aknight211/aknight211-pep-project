package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import io.javalin.Javalin;
import io.javalin.http.Context;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(){
        AccountDAO accountDAO = new AccountDAO();
        MessageDAO messageDAO = new MessageDAO();

        this.accountService = new AccountService(accountDAO);
        this.messageService = new MessageService(messageDAO, accountService);
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::newAccount);
        app.post("/login", this::logged);
        app.post("/messages", this::newMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void newAccount(Context ctx){
        Account account = ctx.bodyAsClass(Account.class);
        Account register = accountService.register(account);
        if(register != null){
            ctx.json(register);
        }
        else{
            ctx.status(400);
        }
    }
    private void logged(Context ctx){
        
        Account account = ctx.bodyAsClass(Account.class);
        Account loggedIn = accountService.logged(account);
        if(loggedIn != null){
            ctx.json(loggedIn);
        }
        else{
            ctx.status(401);
        }
    }
    private void newMessage(Context ctx){
        Message message = ctx.bodyAsClass(Message.class);
        Message created = messageService.newMessage(message);

        if(created != null){
            ctx.json(created).status(200);
        }
        else{
            ctx.status(400);
        }
            
    }
    private void getAllMessages(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    private void getMessageById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if(message != null){
            ctx.json(message).status(200);
        }
        else{
            ctx.result("").status(200);
        }
    }
    private void deleteMessageById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted = messageService.getMessageById(id);
        if(deleted != null){
            ctx.json(deleted);
        }
        else{
            ctx.result("").status(200);
        }
    }
    private void updateMessage(Context ctx){
        try{
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message updateBody = ctx.bodyAsClass(Message.class);
            Message updated = messageService.updateMessage(id,  updateBody.getMessage_text());
            
            if(updated != null){
                ctx.json(updated);
            }
            else{
                ctx.status(400);
            }
        }
        catch(IllegalArgumentException e){
            ctx.status(400);
        }
    }
    private void getMessageByAccountId(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(id);
        ctx.json(messages);
    }

}