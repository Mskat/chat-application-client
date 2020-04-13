import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Input {
    private static BufferedReader input = null;

    public Input(Socket socket) throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void closeInput() throws IOException {
        input.close();
    }

    public static void printMessage() throws IOException {
        String message;
        while ((message = readInput()) != null) {
            if (!checkIfNameIsTheSame(message)) {
                System.out.println(message);
            }
        }
    }

    private static String readInput() throws IOException {
        return input.readLine();
    }

    private static boolean checkIfNameIsTheSame(String message) {
        return message.startsWith(User.getName());
    }
}
