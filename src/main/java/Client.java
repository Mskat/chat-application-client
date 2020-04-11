import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client {
    private String name = null;
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
                Output.printMessage(name, message);
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

    private void userLeavesChat() throws IOException {
        Output.userLeftTheChat(name);
        Output.closeOutput();
        Input.closeInput();
        User.closeUserInput();
        clientSocket.close();
    }

    class ClientHandler implements Runnable {
        Socket clientSocket;

        private ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            System.out.println("Connected. To leave the chat type \"exit\".");
            new Output(clientSocket);
            new Input(clientSocket);
        }

        @Override
        public void run() {
            Output.userJoinTheChat(name);
            try {
                printOutMessageToAll();
            } catch (IOException e) {
                System.out.println("You left chat.");
            }
        }

        private void printOutMessageToAll() throws IOException {
            String message;
            while ((message = Input.readInput()) != null) {
                if (!message.startsWith(name))
                    System.out.println(message);
            }
        }
    }
}