package com.Robert;

import com.Robert.client.ChatClient;

import java.util.Scanner;

import static com.Robert.config.Configuration.SENDER_NAME;
import static com.Robert.config.Configuration.SERVER_ADDRESS;

public class Main {

    public static void main(String[] args) {
        new ChatClient().chat();

//        System.out.println("Please enter the servername!");
//        Scanner scanner = new Scanner(System.in);
//        String serverName = scanner.nextLine();
//        System.out.println("Please enter your name!");
//        String chatterName = scanner.nextLine();
//        if (serverName.equals(SERVER_ADDRESS) && chatterName.equals(SENDER_NAME)) {
//            new ChatClient().chat();
//        } else {
//            System.out.println("Awfully sorry, but the login was incorrect!");
//        }
    }
}
