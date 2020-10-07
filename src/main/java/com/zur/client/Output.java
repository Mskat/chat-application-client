package com.zur.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Output {
    private static PrintWriter output = null;

    public Output(Socket socket) throws IOException {
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public static void userJoinTheChat(String name) {
        output.println(name + " entered to conversation.");
    }

    public static void userLeftTheChat(String name) {
        output.println(name + " left chat.");
    }

    public static void closeOutput() {
        output.close();
    }

    public static void printMessage(String name, String message) {
        output.println(name + ": " + message);
    }
}
