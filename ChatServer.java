package com.Robert.server;

import com.Robert.DaoPractice;
import com.Robert.model.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.Robert.config.Configuration.PORT;

public class ChatServer {

    private List<Socket> chatters = new CopyOnWriteArrayList<>();
    private Map<Socket, ObjectOutputStream> chatLines = new ConcurrentHashMap<>();

    private static final String DB_NAME = "chat.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Robi\\IdeaProjects\\" +
                    "0001_Peter\\BH_Chat_Server\\" + DB_NAME;

//    public ChatServer(Socket socket){
//
//    }

    public void chat() {

        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Chat server is running...");
            while (true) {
                Socket chatter = serverSocket.accept();
                System.out.println("Chatter "+" connected");
                //new Connecter().connecting();
                // sendTopics(chatter);

                chatters.add(chatter);
                OutputStream os = chatter.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                chatLines.put(chatter, oos);

//                new Thread(new Connector(chatter)).start();

                new Thread(new Receiver(chatter)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Network error: " + e.getMessage());
        }
    }

    private void sendTopics(ObjectOutputStream oos)  {
        try {
            DaoPractice topics = new DaoPractice();
            List<Message> topicsOfLastTwoHours = topics.getLastTwoHourMessages();
            for (Message message : topicsOfLastTwoHours) {
                oos.writeObject(message);
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

//    private final class Connector implements Runnable {
//        private Socket socket;
//        public Connector(Socket socket) {this.socket = socket;}
//
//        @Override
//        public void run() {
//            try {
//                sendTopics(socket);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("Network error: " + e.getMessage());
//            } finally {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private final class Receiver implements Runnable {
        private Socket socket;

        public Receiver(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            //sendTopics(socket);

            try {
                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);

                int i = 0;

                while (socket.isConnected()) {
                    Message message = (Message) ois.readObject();
                    System.out.println(message);
                    DaoPractice chatDB = new DaoPractice();

                    if (i == 0) {
                        sendTopics(chatLines.get(socket));
                       i++;
                    }

                    if (message.getMessage().equals("exit")) {
                        System.out.println("Chatter left the conversation");
                        break;
                    }

                    for (Socket client : chatters) {
                        if (!client.equals(socket)) {
                            ObjectOutputStream oos = chatLines.get(client);
                            oos.writeObject(message);
                        }
                    }

                    chatDB.insertMessage(message);

                    chatDB.insertClient(message);


                }
            } catch (IOException | ClassNotFoundException e) {

                    System.out.println("Client error: " + e.getMessage());

            } finally {
                chatters.remove(socket);
                chatLines.remove(socket);

                try {
                    socket.close();
                } catch (IOException ex) {
                    // oh dear oh dear
                }
            }
        }
    }
}
