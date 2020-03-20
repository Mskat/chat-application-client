public class RunClient {
    private final static String address = "127.0.0.1";
    private final static int portNumber = 5000;

    public static void main(String[] args) {
        Client client = new Client();
        client.startClient(address, portNumber);
    }
}
