package com.Robert.dao;

import com.Robert.exeptions.ChatExeption;
import com.Robert.model.Message;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatDao {

    private static final String DB_NAME = "chat.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Robi\\IdeaProjects\\" +
            "0001_Peter\\ChatServer\\" + DB_NAME;
    private static  final DateFormat DATE_FORMAT = new SimpleDateFormat("yyy.MM.dd. HH:mm:ss");

    public ChatDao() {
    }

    public void createTables() {
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement statement = conn.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS clients" +
                    "(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL UNIQUE)");

            statement.execute("CREATE TABLE IF NOT EXISTS messages" +
                    "(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "client_name TEXT NOT NULL, " +
                    "date TEXT NOT NULL, " +
                    "content TEXT NOT NULL, " +
                    "FOREIGN KEY (client_name) REFERENCES clients (name) )");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertMessage(Message message){
        try (
                Connection connection = DriverManager.getConnection(CONNECTION_STRING);
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO messages " +
                        "(client_name, date, content) VALUES (?,?,?)")) {
            stmt.setString(1, message.getSender());
            stmt.setString(2, message.getSentAtForDB());
            stmt.setString(3, message.getMessage());
            stmt.execute();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public void insertClient(Message message) {
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO clients " +
                     "(name) VALUES (?)")) {
            stmt.setString(1, message.getSender());
            stmt.execute();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public List<Message> getLastTwoHourMessages() throws ChatExeption {

        Date dateH = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateH);
        cal.add(Calendar.HOUR, -2);
        Date dateG = cal.getTime();

        List<Message> topicList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement statement = conn.createStatement()) {

            statement.execute("SELECT * FROM messages WHERE date > '" + DATE_FORMAT.format(dateG) + "'");
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                Message message = new Message(
                        rs.getString("client_name"),
                        rs.getString("content")
                );
                message.setSentAtFromDB(rs.getString("date"));
                topicList.add(message);
            }
            return topicList;
        } catch (SQLException e) {//TODO propagate, rethrow
            e.getMessage();
            throw new ChatExeption("Problem with the list of messages" +
                    "of last 2 hours", e);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ChatExeption("Problem with the list of messages" +
                    "of last 2 hours", e);
        }
    }
}
