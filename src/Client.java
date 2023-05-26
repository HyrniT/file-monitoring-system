import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;

    private Socket clientSocket;
    private ClientMessageReceiver messageReceiver;
    private ClientMessageSender messageSender;

    private String serverIP;
    private int serverPort;

    private static final String CONFIG_FILE = "config.txt";

    public Client() {
        setTitle("Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        messageField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        messageField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void loadConfig() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE));
            serverIP = reader.readLine();
            serverPort = Integer.parseInt(reader.readLine());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(CONFIG_FILE));
            writer.println(serverIP);
            writer.println(serverPort);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer(String serverIP, int serverPort) {
        try {
            clientSocket = new Socket(serverIP, serverPort);
            messageReceiver = new ClientMessageReceiver(clientSocket);
            messageSender = new ClientMessageSender(clientSocket);

            sendButton.setEnabled(true);

            String clientName = JOptionPane.showInputDialog("Enter your name:");
            messageSender.sendMessage(clientName);

            new Thread(new Runnable() {
                public void run() {
                    String message;
                    try {
                        while ((message = messageReceiver.receiveMessage()) != null) {
                            messageArea.append(message + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // Save the server configuration
            this.serverIP = serverIP;
            this.serverPort = serverPort;
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() throws IOException {
        String message = messageField.getText();
        messageSender.sendMessage(message);
        messageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Client client = new Client();
                client.setVisible(true);
                client.loadConfig();

                if (client.serverIP != null && client.serverPort != 0) {
                    int choice = JOptionPane.showConfirmDialog(null, "Connect to previous server?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        client.connectToServer(client.serverIP, client.serverPort);
                    } else {
                        client.serverIP = null;
                        client.serverPort = 0;
                        client.saveConfig();
                    }
                }

                if (client.serverIP == null || client.serverPort == 0) {
                    String serverIP = JOptionPane.showInputDialog("Enter server IP:");
                    int serverPort = Integer.parseInt(JOptionPane.showInputDialog("Enter server port:"));
                    client.connectToServer(serverIP, serverPort);
                }
            }
        });
    }
}
