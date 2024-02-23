import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server.");

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner reader = new Scanner(socket.getInputStream());
            Scanner consoleScanner = new Scanner(System.in);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.nextLine()) != null && !message.equals("exit")) {
                        System.out.println("Server: " + message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            String userInput;
            while ((userInput = consoleScanner.nextLine()) != null) {
                writer.println(userInput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
