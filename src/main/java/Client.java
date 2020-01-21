import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader inputLine = new BufferedReader(new InputStreamReader(System.in));

            String inputMessage;
            while(!(inputMessage = inputLine.readLine().toLowerCase()).equals("bye")) {
                output.println(inputMessage);
                System.out.println("Server received a message: " + input.readLine());
            }
        } catch (IOException e) {
            System.out.println("Server is not available");
            //e.printStackTrace();
        }
    }
}
