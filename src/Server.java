import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Server {
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket server = new ServerSocket(8080);
            Socket serverClientSocker = server.accept();

            DataInputStream inputStream = new DataInputStream(serverClientSocker.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(serverClientSocker.getOutputStream());

            String clientMessage = "";

            while (!clientMessage.equals("end")) {
                clientMessage = inputStream.readUTF();
                outputStream.writeUTF("Echo from server :" +clientMessage);
                outputStream.flush();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}