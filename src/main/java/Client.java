import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private String name = null;
    private PrintWriter output = null;
    private BufferedReader input = null;
    private BufferedReader inputLine = new BufferedReader(new InputStreamReader(System.in));
    private Socket clientSocket;
    private ExecutorService pool = null;

    public void startClient(String address, int port, int maxNumberOfClients) throws IOException {
        System.out.print("Type your name: ");
        name = getUserInput();

        try {
            clientSocket = new Socket(address, port);
            pool = Executors.newFixedThreadPool(maxNumberOfClients);
            pool.execute(new ClientHandler(clientSocket));
            typeMessageOrCloseChat();
        } catch (IOException e) {

        } finally {
            pool.shutdown();
        }
    }

    private void typeMessageOrCloseChat() throws IOException {
        while (true) {
            String message = getUserInput();
            if (!message.toLowerCase().equals("exit")) {
                output.println(name + ": " + message);
            } else {
                output.println(name + " left chat.");
                output.close();
                input.close();
                inputLine.close();
                clientSocket.close();
            }
        }
    }

    private String getUserInput() throws IOException {
        return inputLine.readLine();
    }

    class ClientHandler implements Runnable {
        Socket clientSocket;

        private ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            System.out.println("Connected. To leave the chat type \"exit\".");
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            output.println(name + " entered to conversation.");
            try {
                printMessage();
            } catch (IOException e) {
                System.out.println("You left chat.");
            }
        }

        private void printMessage() throws IOException {
            String message;
            while ((message = input.readLine()) != null) {
                if (!message.startsWith(name)) {
                    System.out.println(message);
                }
            }
        }
    }
}