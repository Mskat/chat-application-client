import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int portNumber = 5000;
        Client client = new Client(address, portNumber);
    }

    private Client(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            System.out.println("Connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
