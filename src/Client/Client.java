package Client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket clientSocket; 
    private ClientMessageReceiver messageReceiver;
    private ClientMessageSender messageSender;
    private ClientFileSender fileSender;
    
    public ClientFileSender getFileSender() {
        return fileSender;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ClientMessageReceiver getMessageReceiver() {
        return messageReceiver;
    }

    public ClientMessageSender getMessageSender() {
        return messageSender;
    }

    // Constructors
    public Client(String serverIP, String serverPort, File selectedFile) throws NumberFormatException, UnknownHostException, IOException {
        this.clientSocket = new Socket(serverIP, Integer.parseInt(serverPort));
        this.messageReceiver = new ClientMessageReceiver(clientSocket);
        this.messageSender = new ClientMessageSender(clientSocket);
        this.fileSender = new ClientFileSender(clientSocket);
        sendMessage(clientSocket.getInetAddress().getHostName());
        sendFile(selectedFile); 
    }

    // Methods
    public void sendMessage(String message) {
        try {
            messageSender.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(File file) {
        fileSender.sendFile(file);
    }
}
