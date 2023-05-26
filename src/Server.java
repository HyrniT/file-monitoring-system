import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server extends JFrame {
    private JTextArea messageArea;
    private List<ClientHandler> clients;

    public Server() {
        setTitle("Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        clients = new ArrayList<ClientHandler>();

        JButton startButton = new JButton("Start Server");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        getContentPane().add(startButton, BorderLayout.SOUTH);
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            messageArea.append("Server started on port 8888\n");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                messageArea.append("Client connected: " + clientSocket.getInetAddress().getHostAddress() + "\n");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Server server = new Server();
                server.setVisible(true);
            }
        });
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private String clientName;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            clientSocket = socket;
        }

        public void run() {
            try {
                InputStream inputStream = clientSocket.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                OutputStream outputStream = clientSocket.getOutputStream();
                writer = new PrintWriter(outputStream, true);

                clientName = scanner.nextLine();
                messageArea.append("Client name: " + clientName + "\n");

                String message;
                while (scanner.hasNextLine()) {
                    message = scanner.nextLine();
                    messageArea.append(clientName + ": " + message + "\n");
                    broadcastMessage(clientName + ": " + message);
                }

                clientSocket.close();
                messageArea.append(clientName + " disconnected\n");
                clients.remove(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            writer.println(message);
        }
    }
}
