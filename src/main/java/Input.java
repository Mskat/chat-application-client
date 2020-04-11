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

    public static String readInput() throws IOException {
        return input.readLine();
    }
}
