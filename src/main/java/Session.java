import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable {
    public Session(Socket clientSocket) throws IOException {
        System.out.println("Connected. To leave the chat type \"exit\".");
        new Output(clientSocket);
        new Input(clientSocket);
    }

    @Override
    public void run() {
        Output.userJoinTheChat(User.getName());
        try {
            Input.printMessage();
        } catch (IOException e) {
            System.out.println("You left chat.");
        }
    }
}