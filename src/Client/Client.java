package Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame {
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;

    private Socket clientSocket;
    private ClientMessageReceiver messageReceiver;
    private ClientMessageSender messageSender;

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
                String serverIP = JOptionPane.showInputDialog("Enter server IP:");
                int serverPort = Integer.parseInt(JOptionPane.showInputDialog("Enter server port:"));

                Client client = new Client();
                client.setVisible(true);
                client.connectToServer(serverIP, serverPort);
            }
        });
    }
}
