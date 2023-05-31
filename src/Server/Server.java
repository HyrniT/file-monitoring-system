package Server;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    // Fields
    private ServerSocket serverSocket; 
    private List<ClientMessageHandler> clients;

    public List<ClientMessageHandler> getClients() {
        return clients;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    // Constructors
    public Server(String serverPort) throws IOException {
        this.serverSocket = new ServerSocket(Integer.parseInt(serverPort));
        this.clients = new ArrayList<ClientMessageHandler>();
    }

    // Methods
    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                ClientMessageHandler clientMessageHandler = new ClientMessageHandler(clientSocket);
                clients.add(clientMessageHandler);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clientMessageHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message, int clientId) {
        clients.get(clientId).sendMessage(message);
    }

    public class ClientMessageHandler extends Thread {
        private Client client;
        private ClientMessageReceiver messageReceiver;
        private ClientMessageSender messageSender;

        public ClientMessageReceiver getMessageReceiver() {
            return messageReceiver;
        }

        public ClientMessageSender getMessageSender() {
            return messageSender;
        }

        // Constructors
        public ClientMessageHandler(Socket socket) throws IOException {
            this.client.setClientSocket(socket);
            this.messageReceiver = new ClientMessageReceiver(socket);
            this.messageSender = new ClientMessageSender(socket);
        }
        
        @Override
        public void run() {
            try {
                String clientName = messageReceiver.receiveMessage();
                client.setClientName(clientName);
                String clientIP = client.getClientSocket().getInetAddress().getHostAddress();

                String clientMessage = Helper.getTimestamp() + clientName + " (client): CONNECTED!" + "\n";
                ServerGUI.traceTextArea.append(clientMessage);

                ServerGUI.createClient(clientName, clientIP, true); // tạm thời cho luôn true

                String message;
                while((message = messageReceiver.receiveMessage()) != null) {
                    message = Helper.getTimestamp() + clientName + " (client) said: " + message;
                    ServerGUI.traceTextArea.append(message);
                }

                // clientSocket.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }

        // Gửi tin nhắn từ server đến client
        public void sendMessage(String message) {
            try {
                // ClientMessageSender messageSender = new ClientMessageSender(clientSocket);
                messageSender.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Gửi thông tin đường dẫn cần theo dõi từ server đến client
    }
}