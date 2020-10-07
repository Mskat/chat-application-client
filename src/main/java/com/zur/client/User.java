package com.zur.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class User {
    private static String name;
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static String getName() {
        return name;
    }

    public static String askForName() throws IOException {
        System.out.print("Type your name: ");
        return setName();
    }

    private static String setName() throws IOException {
        name = getUserInput().toUpperCase();
        return name;
    }

    public static String getUserInput() throws IOException {
        return input.readLine();
    }

    public static void closeUserInput() throws IOException {
        input.close();
    }
}
