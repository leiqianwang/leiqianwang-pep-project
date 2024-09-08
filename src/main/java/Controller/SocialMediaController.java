package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import DAO.AccountDao;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */


      AccountService accountService;
      MessageService messageService;

      public SocialMediaController() {

        this.accountService = new AccountService();
        this.messageService = new MessageService();
      }
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        //Define the endpoint
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws SQLException 
     */

     // Handler for registering a new account
    // Handler for registering a new account
   private void registerHandler(Context context) throws JsonProcessingException, SQLException {
  ObjectMapper mapper = new ObjectMapper();
  Account newAccount = mapper.readValue(context.body(), Account.class);

  // Call the AccountService to register the new account
  Account registeredAccount = accountService.registerAccount(newAccount);

  if (registeredAccount != null) {
      context.json(registeredAccount);
      context.status(200);
  } else {
      // // Here we handle the possible failures for validation
      // if (newAccount.getUsername() == null || newAccount.getUsername().trim().isEmpty()) {
      //     context.status(400).result("Registration failed: Username cannot be blank.");
      // } else if (newAccount.getPassword().length() < 4) {
      //     context.status(400).result("Registration failed: Password must be at least 4 characters.");
      // } else {
          context.status(400);
      
  }
            }

    


    // Handler for logging in
    private void loginHandler(Context context) throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      Account loginAccount = mapper.readValue(context.body(), Account.class);

      // Check login credentials
      Account loggedInAccount = accountService.loginAccount(loginAccount.getUsername(), loginAccount.getPassword());

      if (loggedInAccount != null) {
          // Successful login
          context.json(loggedInAccount);
          context.status(200);
      } else {
          // Failed login (invalid username or password)
          context.status(401);
      }
  }
    
     
    //Handler for creating a new message
    private void createMessageHandler(Context context) throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      Message newMessage = mapper.readValue(context.body(), Message.class);
  
      // Call the service to create a message
      Message createdMessage = messageService.createMessage(newMessage);
  
      if (createdMessage != null) {
          // Message created successfully
          context.json(createdMessage);
          context.status(200);
      } else {
        context.status(400);
      }
    }


    //Handler for retriving all messages 
     private void getAllMessagesHandler(Context context) {
           context.json(messageService.getAllMessage());
           context.status(200);
     }

     //Handler for getting a message by ID
     private void getMessageByIdHandler(Context context) {
      int messageId = Integer.parseInt(context.pathParam("message_id"));
      Message message = messageService.getMessageById(messageId);

      if(message != null) {
           context.json(message);
           context.status(200);
      }else {
         context.status(200);
      }
        
     }
   
     //Handler for deleting a message by ID
      private void deleteMessageHandler(Context context) {
             int messageId = Integer.parseInt(context.pathParam("message_id"));
             Message deletedMessage = messageService.deleteMessage(messageId);

             if(deletedMessage != null) {
                  context.json(deletedMessage);
                  context.status(200);
             }else {
              context.status(200);
             }
      }

      //Handler for updating a message text identified by a message ID
      private void updateMessageHandler(Context context) throws JsonProcessingException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message updatedMessage = mapper.readValue(context.body(), Message.class);
        
        // Call the service to update the message
        Message resultMessage = messageService.updateMessage(messageId, updatedMessage);

        if(resultMessage != null) {
          context.json(resultMessage);
          context.status(200);
        }else {
           context.status(400);
        }
      }

      //Handler for getting all messages posted by a specific account
      private void getMessagesByAccountHandler(Context context) {
              int accountId = Integer.parseInt(context.pathParam("account_id"));
              context.json(messageService.getMessagesByAccountId(accountId));
              context.status(200);
      }

}