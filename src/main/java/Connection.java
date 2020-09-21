import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Connection {
    private Socket socket = null;

    public void start(String address, int port, int maxNumberOfClients) throws IOException {
        User.askForName();
        ExecutorService pool = Executors.newFixedThreadPool(maxNumberOfClients);
        try {
            socket = new Socket(address, port);
            pool.execute(new Session(socket));
            typeMessageOrCloseChat();
        } catch (ConnectException e) {
            printServerIsOff();
        } catch (IOException ex) {
            userLeavesChat(socket);
        } finally {
            shutDown(pool);
        }
    }

    private void typeMessageOrCloseChat() throws IOException {
        while (true) {
            String message = User.getUserInput();
            if (!userWantsToLeaveChat(message)) {
                Output.printMessage(User.getName(), message);
            } else {
                userLeavesChat(socket);
            }
        }
    }
    private void printServerIsOff() {
        System.out.println("Server is not available.");
    }

    private void shutDown(ExecutorService pool) {
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

    private void userLeavesChat(Socket socket) throws IOException {
        Output.userLeftTheChat(User.getName());
        Output.closeOutput();
        Input.closeInput();
        User.closeUserInput();
        socket.close();
    }
}
