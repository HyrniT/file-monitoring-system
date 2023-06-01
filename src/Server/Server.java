package Server;

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;

public class Server {
    // Fields
    private ServerSocket serverSocket;
    private List<ClientMessageHandler> clientMessageHandlers;

    public List<ClientMessageHandler> getClientMessageHandlers() {
        return clientMessageHandlers;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    // Constructors
    public Server(String serverPort) throws IOException {
        this.serverSocket = new ServerSocket(Integer.parseInt(serverPort));
        this.clientMessageHandlers = new ArrayList<ClientMessageHandler>();
    }

    // Methods
    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                ClientMessageHandler clientMessageHandler = new ClientMessageHandler(clientSocket);
                clientMessageHandlers.add(clientMessageHandler);
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
        clientMessageHandlers.get(clientId).sendMessage(message);
    }

    public class ClientMessageHandler extends Thread {
        private Socket clientSocket;
        private File clientFile = null;
        private ClientMessageReceiver messageReceiver;
        private ClientMessageSender messageSender;
        private ClientFileReceiver fileReceiver;

        public File getClientFile() {
            return clientFile;
        }

        public ClientFileReceiver getFileReceiver() {
            return fileReceiver;
        }

        public ClientMessageReceiver getMessageReceiver() {
            return messageReceiver;
        }

        public ClientMessageSender getMessageSender() {
            return messageSender;
        }

        // Constructors
        public ClientMessageHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                this.messageReceiver = new ClientMessageReceiver(socket);
                this.messageSender = new ClientMessageSender(socket);
                this.fileReceiver = new ClientFileReceiver(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String clientName = messageReceiver.receiveMessage();
                clientFile = fileReceiver.receiveFile();
                DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(clientFile.getAbsolutePath());

                ServerGUI.createFileTree(clientFile, rootNode);

                String clientMessage = getTimestamp() + clientName + " (client): CONNECTED!" + "\n";
                ServerGUI.traceTextArea.append(clientMessage);

                ServerGUI.createClient(clientSocket, clientFile, true); // tạm thời cho luôn true

                String message;
                while ((message = messageReceiver.receiveMessage()) != null) {
                    // message = Helper.getTimestamp() + clientName + " (client) said: " + message;
                    message += "\n";
                    ServerGUI.traceTextArea.append(message);
                }

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            try {
                messageSender.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static String getTimestamp() {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return now.format(formatter) + " | ";
        }
    }
}