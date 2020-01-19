import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int portNumber = 5000;
        Server server = new Server(portNumber);
    }

    private Server(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port: " + port);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client accepted " + clientSocket);
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            SocketActionSupport(clientSocket);
                        } catch (IOException e) {
                            System.out.println("Client disconnected: " + clientSocket);
                            //e.printStackTrace();
                        }
                    }
                };
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SocketActionSupport(Socket clientSocket) throws IOException {
        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message;
        while((message = input.readLine()) != null) {
            output.println(message);
        }
    }
}