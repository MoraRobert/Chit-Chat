package com.Robert.server;

import com.Robert.DaoPractice;
import com.Robert.model.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static com.Robert.config.Configuration.PORT;

public class Connecter extends Thread{

    public Connecter() {
    }

    public void connecting() {

        try (ServerSocket serverSocket = new ServerSocket(PORT)){

                Socket chatter = serverSocket.accept();
                System.out.println("Chatter "+" connected");
                sendTopics(chatter);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Network error: " + e.getMessage());
        }
    }

    private void sendTopics(Socket chatter)  {
        try {
            OutputStream os = chatter.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            DaoPractice topics = new DaoPractice();
            List<Message> topicsOfLastTwoHours = topics.getLastTwoHourMessages();
            for (Message message : topicsOfLastTwoHours) {
                oos.writeObject(message);
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
