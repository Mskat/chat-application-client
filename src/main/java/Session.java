import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable{
    public Session(Socket clientSocket) throws IOException {
        System.out.println("Connected. To leave the chat type \"exit\".");
        new Output(clientSocket);
        new Input(clientSocket);
    }

    @Override
    public void run() {
        Output.userJoinTheChat(User.getName());
        try {
            printOutMessageToAll();
        } catch (IOException e) {
            System.out.println("You left chat.");
        }
    }

    private void printOutMessageToAll() throws IOException {
        String message;
        while ((message = Input.readInput()) != null) {
            if (!message.startsWith(User.getName()))
                System.out.println(message);
        }
    }
}