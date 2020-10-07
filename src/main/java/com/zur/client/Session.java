package com.zur.client;

import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {
    public Session(Socket clientSocket) throws IOException {
        new Output(clientSocket);
        new Input(clientSocket);
        printConnectedTheChat();
    }

    @Override
    public void run() {
        Output.userJoinTheChat(User.getName());
        try {
            Input.printMessage();
        } catch (IOException e) {
            printLeftTheChat();
        }
    }

    private void printConnectedTheChat() {
        System.out.println("Connected. To leave the chat type \"exit\".");
    }

    private void printLeftTheChat() {
        System.out.println("You left the chat.");
    }
}
