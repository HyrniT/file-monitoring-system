package Client;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientMessageReceiver {
    private Socket clientSocket;
    private DataInputStream dataInputStream;

    public ClientMessageReceiver(Socket socket) throws IOException {
        clientSocket = socket;
        dataInputStream = new DataInputStream(clientSocket.getInputStream());
    }

    public String receiveMessage() throws IOException {
        return dataInputStream.readUTF();
    }
}