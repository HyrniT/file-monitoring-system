package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientGUI extends JFrame {
    public static Color PrimaryColor = Color.WHITE;
    public static Color OnPrimaryColor = Color.BLACK;
    public static String MyFont = "Dialog";

    private JLabel appLabel, ipLabel, portLabel, pathLabel, chatLabel;
    private JPanel topPanel, ipPanel, portPanel, pathPanel, sendPanel, bottomPanel;
    private JTextField ipTextField, portTextField, pathTextField, chatTextField;
    private JButton sendButton, disconnectButton;
    private JTextArea chatTextArea;
    private JScrollPane chatJScrollPane;

    public ClientGUI() {
        setTitle("Client");
        setLayout(new BorderLayout());

        appLabel = new JLabel();
        appLabel.setText("HyrniT's Monitoring System");
        appLabel.setOpaque(true);
        appLabel.setBackground(PrimaryColor);
        appLabel.setForeground(OnPrimaryColor);
        appLabel.setFont(new Font(MyFont, Font.BOLD, 20));
        appLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        appLabel.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(PrimaryColor);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setPreferredSize(new Dimension(0, 180));

        // IP
        ipPanel = new JPanel();
        ipPanel.setLayout(new BorderLayout(10, 10));
        ipPanel.setBackground(PrimaryColor);

        ipLabel = new JLabel();
        ipLabel.setText("IP server: ");
        ipLabel.setFont(new Font(MyFont, Font.BOLD, 13));
        ipLabel.setForeground(OnPrimaryColor);
        ipLabel.setBackground(PrimaryColor);
        ipLabel.setPreferredSize(new Dimension(80, 24));

        ipTextField = new JTextField();
        ipTextField.setText(ClientConnectGUI.serverIP);
        ipTextField.setBackground(PrimaryColor);
        ipTextField.setForeground(OnPrimaryColor);
        ipTextField.setCaretColor(PrimaryColor);
        ipTextField.setPreferredSize(new Dimension(270, 24));
        ipTextField.setEditable(false);

        ipPanel.add(ipLabel, BorderLayout.WEST);
        ipPanel.add(ipTextField, BorderLayout.CENTER);

        // Port
        portPanel = new JPanel();
        portPanel.setLayout(new BorderLayout(10, 10));
        portPanel.setBackground(PrimaryColor);

        portLabel = new JLabel();
        portLabel.setText("Port server: ");
        portLabel.setFont(new Font(MyFont, Font.BOLD, 13));
        portLabel.setForeground(OnPrimaryColor);
        portLabel.setBackground(PrimaryColor);
        portLabel.setPreferredSize(new Dimension(80, 24));

        portTextField = new JTextField();
        portTextField.setText(ClientConnectGUI.serverPort);
        portTextField.setBackground(PrimaryColor);
        portTextField.setForeground(OnPrimaryColor);
        portTextField.setCaretColor(PrimaryColor);
        portTextField.setPreferredSize(new Dimension(270, 24));
        portTextField.setEditable(false);

        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(portTextField, BorderLayout.CENTER);

        // Path
        pathPanel = new JPanel();
        pathPanel.setLayout(new BorderLayout(10, 10));
        pathPanel.setBackground(PrimaryColor);

        pathLabel = new JLabel();
        pathLabel.setText("Path: ");
        pathLabel.setFont(new Font(MyFont, Font.BOLD, 13));
        pathLabel.setForeground(OnPrimaryColor);
        pathLabel.setBackground(PrimaryColor);
        pathLabel.setPreferredSize(new Dimension(80, 24));

        pathTextField = new JTextField();
        pathTextField.setText(ClientConnectGUI.selectedFile.getAbsolutePath());
        pathTextField.setBackground(PrimaryColor);
        pathTextField.setForeground(OnPrimaryColor);
        pathTextField.setCaretColor(PrimaryColor);
        pathTextField.setPreferredSize(new Dimension(270, 24));
        pathTextField.setEditable(false);

        pathPanel.add(pathLabel, BorderLayout.WEST);
        pathPanel.add(pathTextField, BorderLayout.CENTER);

        // Chat
        chatLabel = new JLabel();
        chatLabel.setText("Chat to server:");
        chatLabel.setFont(new Font(MyFont, Font.BOLD, 13));
        chatLabel.setForeground(OnPrimaryColor);
        chatLabel.setBackground(PrimaryColor);
        chatLabel.setPreferredSize(new Dimension(360, 24));

        chatTextArea = new JTextArea();
        chatTextArea.setBackground(OnPrimaryColor);
        chatTextArea.setForeground(PrimaryColor);
        chatTextArea.setCaretColor(OnPrimaryColor);
        chatTextArea.setMargin(new Insets(5, 10, 5, 10));
        chatTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(false);
        chatTextArea.setEditable(false);

        chatJScrollPane = new JScrollPane(chatTextArea);
        chatJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatJScrollPane.setBorder(BorderFactory.createLineBorder(OnPrimaryColor, 2));
        chatJScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chatJScrollPane.setBackground(PrimaryColor);

        sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout(5, 0));
        sendPanel.setBackground(PrimaryColor);

        chatTextField = new JTextField();
        chatTextField.setBackground(PrimaryColor);
        chatTextField.setForeground(OnPrimaryColor);
        chatTextField.setCaretColor(OnPrimaryColor);
        chatTextField.setPreferredSize(new Dimension(280, 24));

        sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendButton.setFocusPainted(false);
        sendButton.setPreferredSize(new Dimension(80, 24));
        addButtonActionListener(sendButton);

        sendPanel.add(chatTextField, BorderLayout.CENTER);
        sendPanel.add(sendButton, BorderLayout.EAST);

        topPanel.add(appLabel);
        topPanel.add(ipPanel);
        topPanel.add(portPanel);
        topPanel.add(pathPanel);
        topPanel.add(chatLabel);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(10, 10));
        bottomPanel.setBackground(PrimaryColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        disconnectButton = new JButton();
        disconnectButton.setText("Disconnect");
        disconnectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        disconnectButton.setFocusPainted(false);
        disconnectButton.setPreferredSize(new Dimension(90, 24));
        addButtonActionListener(disconnectButton);

        bottomPanel.add(sendPanel, BorderLayout.CENTER);
        bottomPanel.add(disconnectButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(chatJScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(ClientGUI.this, "Please click \"Disconnect\" button to exit!",
                        "Tips", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void addButtonActionListener(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == sendButton) {
                    String message = chatTextField.getText();
                    if (!message.isEmpty()) {
                        chatTextArea.append(getTimestamp() + "You: " + message + "\n");
                        String clientName = ClientConnectGUI.client.getClientSocket().getInetAddress().getHostName();
                        String clientIP = ClientConnectGUI.client.getClientSocket().getInetAddress().getHostAddress();
                        message = clientName + " (" + clientIP + ") said: " + message;
                        ClientConnectGUI.client.sendMessage(message);
                        chatTextField.setText("");
                    }
                }
                if (e.getSource() == disconnectButton) {
                    int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Are you sure to disconnect?",
                            "Confirm", JOptionPane.YES_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // Gửi thông báo disconnect tới server
                        ClientConnectGUI.client.sendMessage("@disconnect");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                        }
                        System.exit(0);
                    }
                }
            }
        });
    }

    private String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter) + " | ";
    }
}
