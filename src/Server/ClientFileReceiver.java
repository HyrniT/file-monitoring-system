package Server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientFileReceiver {
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;

    public ClientFileReceiver(Socket socket) throws IOException {
        clientSocket = socket;
        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public File receiveFile() {
        try {
            return (File) objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
