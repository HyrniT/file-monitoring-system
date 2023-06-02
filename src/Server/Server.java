package Server;

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Component;
import java.io.*;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

public class Server {
    // Fields
    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;

    public List<ClientHandler> getClientHandlers() {
        return clientHandlers;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    // Constructors
    public Server(String serverPort) throws IOException {
        this.serverSocket = new ServerSocket(Integer.parseInt(serverPort));
        this.clientHandlers = new ArrayList<ClientHandler>();
    }

    // Methods
    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
                clientHandler.start();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }

    public ClientHandler findClientMessageHandlerByIP(String clientIP) {
        for (ClientHandler handler : clientHandlers) {
            InetAddress address = handler.clientSocket.getInetAddress();
            if (address.getHostAddress().equals(clientIP)) {
                return handler;
            }
        }
        return null;
    }

    public void sendMessage(String message, String clientIP) {
        ClientHandler client = findClientMessageHandlerByIP(clientIP);
        if (client != null) {
            client.sendMessage(message);
        } else {
            // Do nothing
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clientHandlers) {
            client.sendMessage(message);
        }
    }

    public class ClientHandler extends Thread {
        private Socket clientSocket;
        private File clientFile = null;
        private boolean clientStatus;
        private ClientMessageReceiver messageReceiver;
        private ClientMessageSender messageSender;
        private ClientFileReceiver fileReceiver;

        public boolean isClientStatus() {
            return clientStatus;
        }

        public void setClientStatus(boolean clientStatus) {
            this.clientStatus = clientStatus;
        }

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
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                this.messageReceiver = new ClientMessageReceiver(socket);
                this.messageSender = new ClientMessageSender(socket);
                this.fileReceiver = new ClientFileReceiver(socket);
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String clientName = messageReceiver.receiveMessage();
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                clientFile = fileReceiver.receiveFile();
                DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(clientFile.getAbsolutePath());

                ServerGUI.createFileTree(clientFile, rootNode);

                String clientMessage = getTimestamp() + clientName + " (" + clientIP + "): CONNECTED!" + "\n";
                ServerGUI.traceTextArea.append(clientMessage);
                clientStatus = true;

                ServerGUI.createClient(clientSocket, clientFile, clientStatus);

                String message;
                while ((message = messageReceiver.receiveMessage()) != null) {
                    if (message.equals("@disconnect")) {
                        message = getTimestamp() + clientName + " (" + clientIP + "): DISCONNECTED!" + "\n";
                        // Update GUI
                        Component[] components = ServerGUI.monitorsPanel.getComponents();
                        for (Component component : components) {
                            if (component instanceof JPanel) {
                                JPanel panel = (JPanel) component;
                                if (panel.getName() != null && panel.getName().equals(clientIP)) {
                                    ServerGUI.monitorsPanel.remove(panel);
                                    break;
                                }
                            }
                        }
                        ServerGUI.monitorsPanel.revalidate();
                        ServerGUI.monitorsPanel.repaint();
                        // Interrup thread
                        this.interrupt();
                    } else {
                        message = getTimestamp() + message + "\n";
                    }
                    ServerGUI.traceTextArea.append(message);
                }
                
                clientSocket.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            try {
                messageSender.sendMessage(message);
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }

        public static String getTimestamp() {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return now.format(formatter) + " | ";
        }
    }
}