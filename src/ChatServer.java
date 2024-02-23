import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Map<String, PrintWriter> clientWriters = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter writer;
        private BufferedReader reader;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                clientName = reader.readLine(); // Read client's name
                clientWriters.put(clientName, writer); // Add client's writer to the map
                broadcast(clientName + " has joined the chat.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.startsWith("@")) { // Direct message to a specific user
                        int spaceIndex = message.indexOf(' ');
                        if (spaceIndex != -1) {
                            String recipient = message.substring(1, spaceIndex); // Extract recipient's name
                            String directMessage = message.substring(spaceIndex + 1); // Extract the message
                            PrintWriter recipientWriter = clientWriters.get(recipient);
                            if (recipientWriter != null) {
                                recipientWriter.println("Direct message from " + clientName + ": " + directMessage);
                            } else {
                                writer.println("User " + recipient + " is not online.");
                            }
                        } else {
                            writer.println("Invalid direct message format.");
                        }
                    } else {
                        broadcast(clientName + ": " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (clientName != null) {
                    clientWriters.remove(clientName);
                    broadcast(clientName + " has left the chat.");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void broadcast(String message) {
        for (PrintWriter writer : clientWriters.values()) {
            writer.println(message);
        }
    }
}
