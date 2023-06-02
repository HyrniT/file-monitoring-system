package Client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class Client {
    private Socket clientSocket;
    private Thread clientMonitorThread;
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
    public Client(String serverIP, String serverPort, File selectedFile) {
        try {
            this.clientSocket = new Socket(serverIP, Integer.parseInt(serverPort));
            this.messageReceiver = new ClientMessageReceiver(clientSocket);
            this.messageSender = new ClientMessageSender(clientSocket);
            this.fileSender = new ClientFileSender(clientSocket);
            sendMessage(clientSocket.getInetAddress().getHostName());
            sendFile(selectedFile);
            startWatching(selectedFile);
            ClientConnectGUI.isConnected = 0;
        } catch (NumberFormatException e) {
            ClientConnectGUI.isConnected = 1;
        } catch (UnknownHostException e) {
            ClientConnectGUI.isConnected = 2;
        } catch (IOException e) {
            ClientConnectGUI.isConnected = 3;
        }
        new Thread(new StartClient()).start();
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

    public class StartClient implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = ClientConnectGUI.client.getMessageReceiver().receiveMessage()) != null) {
                    stopWatching();
                    File selectedFile = new File(message);
                    startWatching(selectedFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopWatching() {
        if(clientMonitorThread != null) {
            clientMonitorThread.interrupt();
        }
    }

    private void startWatching(File selectedFile) {
        String folderPath = selectedFile.getAbsolutePath();
        Path path = Paths.get(folderPath);
        String clientName = clientSocket.getInetAddress().getHostName();
        String clientIP = clientSocket.getInetAddress().getHostAddress();
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

            clientMonitorThread = new Thread(() -> {
                try {
                    WatchKey watchKey;
                    while ((watchKey = watchService.take()) != null) {
                        for (WatchEvent<?> event : watchKey.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();
                            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                String message = clientName + " (" + clientIP + ") created: "
                                        + selectedFile.toPath().resolve((Path) event.context());
                                sendMessage(message);
                            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                String message = clientName + " (" + clientIP + ") deleted: "
                                        + selectedFile.toPath().resolve((Path) event.context());
                                sendMessage(message);
                            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                String message = clientName + " (" + clientIP + ") modified: "
                                        + selectedFile.toPath().resolve((Path) event.context());
                                sendMessage(message);
                            }
                        }
                        watchKey.reset();
                    }
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
            });
            clientMonitorThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
