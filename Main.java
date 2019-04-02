package com.Robert;

import com.Robert.server.ChatServer;
import com.Robert.server.Connecter;


public class Main {

    public static void main(String[] args) {

        new DaoPractice().createTables();

        //new Connecter().connecting();

        new ChatServer().chat();


    }
}
