import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<PrintWriter> chatParticipants = new ArrayList<>();

    public static void main(String[] args) {
        int portNumber = 5000;
        Server server = new Server();
        server.startServer(portNumber);
    }

    private void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port: " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                chatParticipants.add(output);
                Thread thread = new Thread(new ServerClient(clientSocket, input));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServerClient implements Runnable {
        private Socket clientSocket;
        private BufferedReader input;

        private ServerClient(Socket clientSocket, BufferedReader input) {
            this.clientSocket = clientSocket;
            this.input = input;
            System.out.println("Client accepted: " + clientSocket);
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = input.readLine()) != null) {
                    for (PrintWriter participant : chatParticipants) {
                        participant.println(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + clientSocket);
            }
        }
    }
}

