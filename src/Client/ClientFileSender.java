package Client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientFileSender {
    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;

    public ClientFileSender(Socket socket) throws IOException {
        clientSocket = socket;
        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    public void sendFile(File file) {
        try {
            objectOutputStream.writeObject(file);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
