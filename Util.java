package com.Robert;

import com.Robert.model.Message;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {

    private static  final DateFormat DATE_FORMAT = new SimpleDateFormat("yyy.MM.dd. HH:mm:ss");

    public final String DB_NAME = "chat.db";
    public final String CONNECTION_STRING =
            "jdbc:sqlite:C:\\Users\\Robi\\IdeaProjects\\" +
                    "0001_Peter\\BH_Chat_Server\\" + DB_NAME;

    public List<Message> queryDBforMessages(int id) {

        List<Message> topic = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM messages WHERE (id > " + id + " )");

            while (rs.next()) {
                Message message = new Message(
                        rs.getString("client_name"),
                        rs.getString("content"));
                //message.setSentAt(rs.getDate("date"));
                topic.add(message);
            }
            return topic;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topic;
    }
}
