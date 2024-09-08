package DAO;

import Model.Message;
import Util.ConnectionUtil;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class MessageDao {

     // Initialize AccountDao instance
     private AccountDao accountDao;

     // Constructor to initialize AccountDao
     public MessageDao() {
         this.accountDao = new AccountDao(); // Initialize the accountDao here
     }

    //method to create a new message
    public Message createMessage(Message message) throws SQLException {
        Message createdMessage = null;

       // Validate if the user exists using AccountDao's new getAccountById method
    if (accountDao.getAccountById(message.getPosted_by()) == null) {
        return null; // User does not exist, return null to indicate failure
    }

    // Proceed with creating the message if the user exists
    try (Connection connection = ConnectionUtil.getConnection()) {
        String insertQuery = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement insertSt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
        
        // Set the values for the new message
        insertSt.setInt(1, message.getPosted_by());
        insertSt.setString(2, message.getMessage_text());
        insertSt.setLong(3, message.getTime_posted_epoch());

        // Execute the query and check if rows were affected
        int rowsAffected = insertSt.executeUpdate();

        // If a row was inserted, retrieve the generated message ID
        if (rowsAffected > 0) {
            ResultSet generatedKey = insertSt.getGeneratedKeys();
            if (generatedKey.next()) {
                message.setMessage_id(generatedKey.getInt(1));
                createdMessage = message;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return createdMessage;
    }

    //Method to get all messages
        public List<Message> getAllMessages() {
              List<Message> messages = new ArrayList<>();

              try (Connection connection = ConnectionUtil.getConnection()) {
                //SQL Query to retrieve all messages
                String sql = "SELECT * FROM message";
                PreparedStatement pst = connection.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while(rs.next()) {
                    Message message = new Message();
                    message.setMessage_id(rs.getInt("message_id"));
                    message.setPosted_by(rs.getInt("posted_by"));
                    message.setMessage_text(rs.getString("message_text"));
                    message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                    messages.add(message);
                }
                   
              }catch(SQLException e) {
                e.printStackTrace();
              }
              return messages;
        }

        // Method to get a message by ID
        public Message getMessageById(int messageId) {
            Message message = null;

            try(Connection connection = ConnectionUtil.getConnection()) {
            //SQL query to retrieve a message by ID
             String sql = "SELECT * FROM message WHERE message_id = ?";
             PreparedStatement st = connection.prepareStatement(sql);
             st.setInt(1, messageId);
             ResultSet rs = st.executeQuery();

             if (rs.next()) {
                message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));

             }


            } catch(SQLException e) {
                e.printStackTrace();
            }
            return message;
        }

        //Method to delete a message by ID
        public Message deleteMessage(int messageId) {
            Message deletedMessage = null;

            try (Connection connection = ConnectionUtil.getConnection()) {
                //Retrive the targeted message before deleting by calling getMessageById() method
                deletedMessage = getMessageById(messageId);
                if(deletedMessage != null) {
                    //Perform SQL query to delete the message
                    String deleteQuery = "DELETE FROM message WHERE message_id = ?";
                    PreparedStatement st = connection.prepareStatement(deleteQuery);
                    st.setInt(1, messageId);
                    st.executeUpdate();
                }

            } catch(SQLException e) {
                e.printStackTrace();
            }
            return deletedMessage;
        }
        

        //Method to update a message
        public Message updateMessage(int messageId, Message updatedMessage) {
            Message existingMessage = getMessageById(messageId);

           if(existingMessage != null) {
            try(Connection connection = ConnectionUtil.getConnection()) {
                String updateQuery = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement st = connection.prepareStatement(updateQuery);
                st.setString(1, updatedMessage.getMessage_text());
                st.setInt(2, messageId);

                int rowsAffected = st.executeUpdate();
                if(rowsAffected > 0) {
                    existingMessage.setMessage_text(updatedMessage.getMessage_text());
                }

            } catch(SQLException e) {
                e.printStackTrace();
            }
           }
           return existingMessage;
        }

        //Method to retrieve all messages posted by a specific account
        public List<Message> getMessagesByAccountId(int accountId) {
               List<Message> messages = new ArrayList<>();

               try (Connection connection = ConnectionUtil.getConnection()) {
                     //perform SQL query to retrieve all messageds posted by a specific user, tracked by message table's 
                     //foreign key posted_by, Which is the user's account_id
                String sql = "SELECT * FROM message WHERE posted_by = ?";
                PreparedStatement st = connection.prepareStatement(sql);
                st.setInt(1, accountId);
                ResultSet rs = st.executeQuery();

                while(rs.next()) {
                    Message message = new Message();
                    message.setMessage_id(rs.getInt("message_id"));
                    message.setPosted_by(rs.getInt("posted_by"));
                    message.setMessage_text(rs.getString("message_text"));
                    message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                    messages.add(message);
                }
               } catch(SQLException e) {
                e.printStackTrace();
               }
               return messages;
        }
    
}
