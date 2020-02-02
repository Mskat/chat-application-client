import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int portNumber = 5000;
        new Server(portNumber);
    }

    private Server(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port: " + port);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                Thread t = new Thread(new ServerClient(clientSocket));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServerClient implements Runnable{

        Socket socket;

        private ServerClient(Socket clientSocket) {
            socket = clientSocket;
            System.out.println("Client accepted: " + socket);
        }

        @Override
        public void run() {
            try {
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while((message = input.readLine()) != null) {
                    output.println(message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket);
            }
        }
    }

}

