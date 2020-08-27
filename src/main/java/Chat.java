import java.io.IOException;

public class Chat {
    private final static String address = "127.0.0.1";
    private final static int portNumber = 5000;
    private final static int maxNumberOfClients = 10;

    public static void main(String[] args) throws IOException {
        Connection connection = new Connection();
        connection.start(address, portNumber, maxNumberOfClients);
    }
}
