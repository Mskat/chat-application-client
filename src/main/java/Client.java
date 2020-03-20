import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String name = null;
    private PrintWriter output = null;
    private BufferedReader input = null;
    private Scanner sc = new Scanner(System.in);

    public void startClient(String address, int port) throws IOException {
        BufferedReader inputLine = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Type your name: ");
        name = getUserInput();

        try {
            Socket clientSocket = new Socket(address, port);
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread thread = new Thread(new ClientHandler(clientSocket));
            thread.start();

            while (true) {
                String message = getUserInput();
                if (!message.toLowerCase().equals("exit")) {
                    output.println(name + ": " + message);
                    output.flush();
                } else {
                    output.println(name + " left chat.");
                    output.flush();
                    output.close();
                    inputLine.close();
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            
        }
    }

    private String getUserInput() throws IOException {
        BufferedReader inputLine = new BufferedReader(new InputStreamReader(System.in));
        return inputLine.readLine();
    }

    class ClientHandler implements Runnable {
        Socket clientSocket;

        private ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            System.out.println("Connected. To leave the chat type \"exit\".");

        }

        @Override
        public void run() {
            output.println(name + " entered to conversation.");
            output.flush();

            try {
                String message;
                while ((message = input.readLine()) != null) {
                    if (!message.startsWith(name)) {
                        System.out.println(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("You left chat.");
                output.flush();
                output.close();
            }
        }
    }
}