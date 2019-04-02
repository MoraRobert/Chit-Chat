package com.Robert.client;

import com.Robert.config.Configuration;
import com.Robert.model.Message;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    public void chat() {

        try (Socket socket = new Socket(Configuration.SERVER_ADDRESS, Configuration.PORT)){
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            new Thread(new Receiver(socket)).start();

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String text;
            while (!"exit".equals(text = br.readLine())) {
                Message message = new Message(Configuration.SENDER_NAME, text);
                oos.writeObject(message);
            }

        } catch (IOException e) {
            System.out.println("Network error: " + e.getMessage());
        }

    }

    private final class Receiver implements Runnable {

        private Socket socket;

        public Receiver(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);

                while (socket.isConnected()) {
                    Message message = (Message) ois.readObject();
                    System.out.println(message);
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Network error: " + ex.getMessage());
            }
        }
    }


}
