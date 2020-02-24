import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String name = null;
    private PrintWriter output = null;
    private BufferedReader input = null;

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int portNumber = 5000;
        Client client = new Client();
        client.Client(address, portNumber);
    }

    private void Client(String address, int port) {
        BufferedReader inputLine = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Type your name: ");
            name = inputLine.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Socket clientSocket = new Socket(address, port);
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread thread = new Thread(new ClientThread(clientSocket));
            thread.start();

            while (true) {
                String message = inputLine.readLine();
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

    class ClientThread implements Runnable {
        Socket clientSocket;

        private ClientThread(Socket clientSocket) {
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