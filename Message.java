package com.Robert.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable{

    private static  final DateFormat DATE_FORMAT = new SimpleDateFormat("yyy.MM.dd. HH:mm:ss");

    private String sender;
    private Date sentAt;
    private String message;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.sentAt = new Date();
    }

    public Date getSentAt() {
        return sentAt;
    }

    public String getSentAtForDB() {
        return DATE_FORMAT.format(sentAt);
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public void setSentAtFromDB(String sentAt) throws ParseException {
        this.sentAt = DATE_FORMAT.parse(sentAt);
    }

    @Override
    public String toString() {
        return DATE_FORMAT.format(sentAt) + " " + sender + ": " + message;
    }
}
