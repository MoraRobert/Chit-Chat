package com.Robert;

import com.Robert.model.Message;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DaoPractice {

    private static  final DateFormat DATE_FORMAT = new SimpleDateFormat("yyy.MM.dd. HH:mm:ss");

    private static final String DB_NAME = "chat.db";
    private static final String CONNECTION_STRING =
            "jdbc:sqlite:C:\\Users\\Robi\\IdeaProjects\\" +
                    "0001_Peter\\BH_Chat_Server\\" + DB_NAME;

    public DaoPractice() {

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
        try (Connection connection = DriverManager.getConnection(CONNECTION_STRING);
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

    public List<Message> getLastTwoHourMessages() {

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

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return topicList;
    }
}
