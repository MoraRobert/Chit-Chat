package com.Robert;

import com.Robert.dao.ChatDao;
import com.Robert.exeptions.ChatExeption;
import com.Robert.model.Message;
import com.Robert.util.Receiver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.Robert.config.Configuration.PORT;

public class Chatter extends Thread {

    private Socket socket;
    private List<Socket> chatters = new CopyOnWriteArrayList<>();
    private Map<Socket, ObjectOutputStream> chatLines = new ConcurrentHashMap<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Chat server is running...");
            while (true) {
                Socket chatter = serverSocket.accept();
                System.out.println("Chatter "+" connected");

                chatters.add(chatter);
                OutputStream os = chatter.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                sendTopics(oos);
                chatLines.put(chatter, oos);

                new Thread(new Receiver(chatter, chatters, chatLines)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Network error: " + e.getMessage());
        }
    }

    private void sendTopics(ObjectOutputStream oos) throws IOException {

        ChatDao topics = new ChatDao();
        try {
            List<Message> topicsOfLastTwoHours = topics.getLastTwoHourMessages();
            for (Message message : topicsOfLastTwoHours) {
                oos.writeObject(message);
            }
        } catch (ChatExeption e) {
            e.getMessage();
        }
    }

}
