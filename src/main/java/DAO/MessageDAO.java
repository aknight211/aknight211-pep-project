package DAO;
import Model.Message;

import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    Connection connection = ConnectionUtil.getConnection();

    public Message newMessage(Message message){
        try{
            String sql = "insert into Message (posted_by, message_text, time_posted_epoch) values (?,?,?)";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                message.setMessage_id(rs.getInt(1));
                return message;
            }
            
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from Message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int id){
        try{
            String sql = "select * from Message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteMessageById(int message_id){
        try{
            String sql = "delete from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            return preparedStatement.executeUpdate() == 1;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Message updateMessage(int message_id, String text){
        try{
            String sql = "update message set message_text = ? where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,text);
            preparedStatement.setInt(2, message_id);

            int rows = preparedStatement.executeUpdate();
            if(rows == 1){
                return getMessageById(message_id);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getMessageByAccountId(int accountId){
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message where posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
               messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"))); 
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }
}
