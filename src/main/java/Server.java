import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ExecutorService pool = null;
    private ChatParticipant chatParticipant = new ChatParticipant();

    public void startServer(int portNumber, int maxNumberOfClients) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is listening on port: " + portNumber);
            pool = Executors.newFixedThreadPool(maxNumberOfClients);
            while (true) {
                Socket socket = serverSocket.accept();
                pool.execute(new ServerHandler(socket));
            }
        } finally {
            pool.shutdown();
        }
    }

    class ServerHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader input;
        private PrintWriter output;

        private ServerHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            System.out.println("Client accepted: " + clientSocket);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            chatParticipant.addChatParticipant(output);
        }

        @Override
        public void run() {
            try {
                sendMessageToAllChatParticipants(input);
            } catch (IOException e) {
                System.out.println("Client disconnected: " + clientSocket);
            }
        }

        private void sendMessageToAllChatParticipants(BufferedReader input) throws IOException {
            String message;
            while ((message = input.readLine()) != null) {
                for (PrintWriter participant : chatParticipant.getChatParticipants()) {
                    participant.println(message);
                }
            }
        }
    }
}

