import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client {
    private String name = null;
    private PrintWriter output = null;
    private BufferedReader input = null;
    private Socket clientSocket;
    private ExecutorService pool = null;

    public void startClient(String address, int port, int maxNumberOfClients) throws IOException {
        name = getNameFromUser();
        pool = Executors.newFixedThreadPool(maxNumberOfClients);
        try {
            clientSocket = new Socket(address, port);
            pool.execute(new ClientHandler(clientSocket));
            typeMessageOrCloseChat();
        } catch (IOException e) {
            System.out.println("Server is not available.");
        } finally {
            shutDownConnection(pool);
        }
    }

    private String getNameFromUser() throws IOException {
        System.out.print("Type your name: ");
        return User.getUserInput().toUpperCase();
    }

    private void typeMessageOrCloseChat() throws IOException {
        while (true) {
            String message = User.getUserInput();
            if (!userWantsToLeaveChat(message)) {
                print(message);
            } else {
                userLeavesChat();
            }
        }
    }

    private void shutDownConnection(ExecutorService pool) {
        System.out.println("I'm shutting down the connection...");
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("The connection did not shut down. Trying again...");
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private boolean userWantsToLeaveChat(String message) {
        return message.toLowerCase().equals("exit");
    }

    private void print(String message) {
        output.println(name + ": " + message);
    }

    private void userLeavesChat() throws IOException {
        output.println(name + " left chat.");
        output.close();
        input.close();
        User.closeUserInput();
        clientSocket.close();
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
                printOutMessageToAll();
            } catch (IOException e) {
                System.out.println("You left chat.");
            }
        }

        private void printOutMessageToAll() throws IOException {
            String message;
            while ((message = input.readLine()) != null) {
                if (!message.startsWith(name))
                    System.out.println(message);
            }
        }
    }
}