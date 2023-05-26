package Client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientMessageSender {
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;

    public ClientMessageSender(Socket socket) throws IOException {
        clientSocket = socket;
        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void sendMessage(String message) throws IOException {
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
    }
}