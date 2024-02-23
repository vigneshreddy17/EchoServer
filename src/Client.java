import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws UnknownHostException {
        try {
            Socket socket = new Socket("localhost", 8080);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            Scanner input = new Scanner(System.in);
            System.out.println("Welcome to chat application");
            System.out.println("Enter your name");
            String name = input.nextLine();
            outputStream.writeUTF(name);
            outputStream.flush();

            String clientMessage = "";
            String serverMessage;



            while (!clientMessage.equals("end")) {
                System.out.println("Enter message " + name +":");
                clientMessage = input.nextLine();

                outputStream.writeUTF(clientMessage);
                outputStream.flush();

                serverMessage = inputStream.readUTF();
                System.out.println(serverMessage);
            }
            socket.close();
            inputStream.close();
            outputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

}
