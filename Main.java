package com.Robert;

import com.Robert.dao.ChatDao;

public class Main {

    public static void main(String[] args) {

        new ChatDao().createTables();

        new Chatter().start();


    }
}
