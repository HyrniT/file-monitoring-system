package Client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientConnectGUI extends JFrame {
    public static Color PrimaryColor = Color.WHITE;
    public static Color OnPrimaryColor = Color.BLACK;
    public static String MyFont = "Dialog";

    public static String serverIP, serverPort, directoryPath;

    private JLabel appLabel, ipLabel, portLabel, browserLabel, messsageLabel;
    private JPanel centerPanel, ipPanel, portPanel, browserPanel, buttonPanel;
    private JTextField ipTextField, portTextField, pathTextField;
    private JButton browserButton, connectButton, testButton;

    public ClientConnectGUI() {
        setTitle("Client");
        setLayout(new BorderLayout());

        appLabel = new JLabel();
        appLabel.setText("Connect to HyrniT's Server");
        appLabel.setOpaque(true);
        appLabel.setBackground(PrimaryColor);
        appLabel.setForeground(OnPrimaryColor);
        appLabel.setFont(new Font(MyFont, Font.BOLD, 20));
        appLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        appLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //
        centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(PrimaryColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // IP
        ipPanel = new JPanel();
        ipPanel.setLayout(new BorderLayout(10, 10));
        ipPanel.setBackground(PrimaryColor);

        ipLabel = new JLabel();
        ipLabel.setText("Enter IP Server: ");
        ipLabel.setFont(new Font(MyFont, Font.BOLD, 13));
        ipLabel.setForeground(OnPrimaryColor);
        ipLabel.setBackground(PrimaryColor);
        ipLabel.setPreferredSize(new Dimension(100, 24));

        ipTextField = new JTextField();
        ipTextField.setText("192.168.1.94");
        ipTextField.setBackground(PrimaryColor);
        ipTextField.setForeground(OnPrimaryColor);
        ipTextField.setCaretColor(OnPrimaryColor);
        ipTextField.setPreferredSize(new Dimension(250, 24));
        addTextFieldActionListener(ipTextField);

        ipPanel.add(ipLabel, BorderLayout.WEST);
        ipPanel.add(ipTextField, BorderLayout.CENTER);

        // Port
        portPanel = new JPanel();
        portPanel.setLayout(new BorderLayout(10, 10));
        portPanel.setBackground(PrimaryColor);

        portLabel = new JLabel();
        portLabel.setText("Enter Port: ");
        portLabel.setFont(new Font(MyFont, Font.BOLD, 13));
        portLabel.setForeground(OnPrimaryColor);
        portLabel.setBackground(PrimaryColor);
        portLabel.setPreferredSize(new Dimension(100, 24));

        portTextField = new JTextField();
        portTextField.setText("123");
        portTextField.setBackground(PrimaryColor);
        portTextField.setForeground(OnPrimaryColor);
        portTextField.setCaretColor(OnPrimaryColor);
        portTextField.setPreferredSize(new Dimension(250, 24));
        addTextFieldActionListener(portTextField);

        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(portTextField, BorderLayout.CENTER);

        // Browser
        browserPanel = new JPanel();
        browserPanel.setLayout(new BorderLayout(10, 10));
        browserPanel.setBackground(PrimaryColor);

        browserLabel = new JLabel();
        browserLabel.setText("Choose the directory allow to be traced: ");
        browserLabel.setFont(new Font(MyFont, Font.BOLD, 13));
        browserLabel.setForeground(OnPrimaryColor);
        browserLabel.setBackground(PrimaryColor);
        browserLabel.setPreferredSize(new Dimension(260, 24));

        browserButton = new JButton();
        browserButton.setText("Browser...");
        browserButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        browserButton.setFocusPainted(false);
        browserButton.setPreferredSize(new Dimension(90, 24));
        addButtonActionListener(browserButton);

        browserPanel.add(browserLabel, BorderLayout.CENTER);
        browserPanel.add(browserButton, BorderLayout.EAST);

        // Path
        pathTextField = new JTextField();
        pathTextField.setBackground(PrimaryColor);
        pathTextField.setForeground(OnPrimaryColor);
        pathTextField.setCaretColor(OnPrimaryColor);
        pathTextField.setPreferredSize(new Dimension(360, 24));
        addTextFieldActionListener(pathTextField);

        // Button
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(PrimaryColor);

        testButton = new JButton();
        testButton.setText("Test");
        testButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        testButton.setFocusPainted(false);
        testButton.setPreferredSize(new Dimension(90, 24));
        addButtonActionListener(testButton);

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        connectButton.setFocusPainted(false);
        connectButton.setPreferredSize(new Dimension(90, 24));
        connectButton.setEnabled(false);
        addButtonActionListener(connectButton);

        // Message
        messsageLabel = new JLabel();
        messsageLabel.setFont(new Font(MyFont, Font.ITALIC, 12));
        // messsageLabel.setForeground(MessageColor);
        messsageLabel.setBackground(PrimaryColor);
        messsageLabel.setPreferredSize(new Dimension(360, 24));

        buttonPanel.add(testButton);
        buttonPanel.add(connectButton);
        //
        centerPanel.add(ipPanel);
        centerPanel.add(portPanel);
        centerPanel.add(browserPanel);
        centerPanel.add(pathTextField);
        centerPanel.add(messsageLabel);

        add(appLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        setLocationRelativeTo(null);
    }

    private void addButtonActionListener(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == browserButton) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int result = fileChooser.showOpenDialog(ClientConnectGUI.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedDirectory = fileChooser.getSelectedFile();
                        pathTextField.setText(selectedDirectory.getAbsolutePath());
                    }
                }
                if (e.getSource() == testButton) {
                    serverIP = ipTextField.getText();
                    serverPort = portTextField.getText();
                    directoryPath = pathTextField.getText();

                    boolean isValidPort = checkValidPort(serverPort);
                    boolean isValidIp = checkValidIp(serverIP);
                    boolean isConnected = checkConnection(serverIP, serverPort);
                    boolean isDirectoryExist = checkDirectoryExist(directoryPath);

                    if (isConnected && isDirectoryExist && isValidIp && isValidPort) {
                        connectButton.setEnabled(true);
                        showMessage("Valid connection!", MessageType.SUCCESS);
                    } else if (!isValidIp) {
                        showMessage("Invalid IP address!", MessageType.ERROR);
                    } else if (!isValidPort) {
                        showMessage("Invalid port number! Valid port in range 0 to 65535!", MessageType.ERROR);
                    } else if (!isDirectoryExist) {
                        showMessage("Directory path does not exist!", MessageType.ERROR);
                    } else {
                        showMessage("Invalid connection! Please check again!", MessageType.ERROR);
                    }
                }
                if (e.getSource() == connectButton) {
                    dispose();

                    SwingUtilities.invokeLater(() -> {
                        new ClientGUI();
                    });
                }
            }
        });
    }

    public void addTextFieldActionListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                connectButton.setEnabled(false);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                connectButton.setEnabled(false);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                connectButton.setEnabled(false);
            }
        });
    }

    /*
     * ^ và $ đại diện cho đầu và cuối của chuỗi, đảm bảo rằng biểu thức chính quy
     * phải khớp
     * với toàn bộ chuỗi địa chỉ IP, không được phép có ký tự thừa.
     * 
     * [01]?\\d\\d? kiểm tra một số trong khoảng từ 0 đến 199. [01]? đại diện cho 0
     * hoặc 1 (không bắt buộc),
     * \\d đại diện cho một chữ số (0-9), và \\d? đại diện cho một hoặc không có chữ
     * số (0-9) (không bắt buộc).
     * 
     * 2[0-4]\\d kiểm tra một số trong khoảng từ 200 đến 249. 2 đại diện cho chữ số
     * 2,
     * [0-4] đại diện cho một chữ số trong khoảng từ 0 đến 4, và \\d đại diện cho
     * một chữ số (0-9).
     * 
     * 25[0-5] kiểm tra một số trong khoảng từ 250 đến 255.
     * 25 đại diện cho số 25 và [0-5] đại diện cho một chữ số trong khoảng từ 0 đến
     * 5.
     * 
     * Dấu \\. được sử dụng để kiểm tra dấu chấm (.), vì trong biểu thức chính quy
     * các ký tự đặc biệt
     * như dấu chấm phải được đặt trong dấu gạch chéo ngược (backslash).
     */

    public boolean checkValidIp(String ipAddress) {
        String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        return ipAddress.matches(ipRegex);
    }

    public static boolean checkValidPort(String port) {
        try {
            int portNumber = Integer.parseInt(port);
            return portNumber >= 0 && portNumber <= 65525;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkConnection(String ipAddress, String port) {
        try {
            if (checkValidPort(port) && checkValidIp(ipAddress)) {
                int portNumber = Integer.parseInt(port);
                Socket clientSocket = new Socket(ipAddress, portNumber); // throw exc
                clientSocket.close();
                return true;
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean checkDirectoryExist(String path) {
        File directory = new File(path);
        if (directory.exists()) {
            if (directory.isDirectory()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private enum MessageType {
        SUCCESS,
        ERROR
    }

    private void showMessage(String message, MessageType type) {
        messsageLabel.setText(message);
        switch (type) {
            case SUCCESS:
                messsageLabel.setForeground(new Color(33, 181, 49));
                break;
            case ERROR:
                messsageLabel.setForeground(new Color(187, 31, 29));
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientConnectGUI();
        });
    }
}
