package com.Robert.util;

import com.Robert.dao.ChatDao;
import com.Robert.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Receiver implements Runnable {

    private Socket socket;
    private List<Socket> chatters;
    private Map<Socket, ObjectOutputStream> chatLines;

    public Receiver(Socket socket, List<Socket> chatters, Map<Socket, ObjectOutputStream> chatLines) {
        this.socket = socket;
        this.chatters = chatters;
        this.chatLines = chatLines;
    }

    @Override
    public void run() {

        try {
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            ChatDao chatDB = new ChatDao();

            while (socket.isConnected()) {
                Message message = (Message) ois.readObject();
                System.out.println(message);

                if (message.getMessage().equals("exit")) {
                    System.out.println("Chatter" + message.getSender() +  " left the conversation");
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
