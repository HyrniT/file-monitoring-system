package Server;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class ServerGUI extends JFrame {
    // Server
    private Server server;

    private String serverIP, serverName, serverPort;
    private boolean isWatchingServer = false;
    private Thread serverMonitorThread;

    private DefaultMutableTreeNode selectedNode; /* có thể chỉnh sửa sau */

    public static Color PrimaryColor = Color.WHITE;
    public static Color OnPrimaryColor = Color.BLACK;
    public static String MyFont = "Dialog";

    private JTree fileTree;
    private JPanel rightPanel, toprightPanel, bottomrightPanel,
            leftPanel, topLeftPanel, bottomLeftPanel,
            topBottomLeftPanel, topTopLeftPanel;

    private static JPanel monitorsPanel;

    private JPanel buttonPanel;
    private JLabel directoryLabel, copyrightLabel, appLabel,
            traceLabel, monitorLabel;
    private static JLabel clientStatusLabel;
    private JButton traceButton, exportButton, connectButton, reloadButton, serverButton;

    private static JButton clientButton;
    private JScrollPane rightScrolPane, topLeftScrolPane, bottomLeftScrolPane;
    private JSplitPane horizonalSplitPane, verticalSplitPane;
    public static JTextArea traceTextArea;

    public ServerGUI() {
        setTitle("Server");
        setLayout(new BorderLayout());

        createPortInput();

        new Thread(new StartServer()).start(); // listen client

        rightScrolPane = new JScrollPane();
        rightScrolPane.setBackground(OnPrimaryColor);
        rightScrolPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rightScrolPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrolPane.setBorder(null);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(PrimaryColor);
        rightPanel.setPreferredSize(new Dimension(250, 0));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        rightPanel.add(rightScrolPane, BorderLayout.CENTER);

        directoryLabel = new JLabel();
        directoryLabel.setText("File Tree   ");
        directoryLabel.setFont(new Font(MyFont, Font.BOLD, 15));
        directoryLabel.setForeground(OnPrimaryColor);
        directoryLabel.setBackground(PrimaryColor);
        directoryLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(PrimaryColor);

        traceButton = new JButton();
        traceButton.setText("Trace");
        traceButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        traceButton.setFocusPainted(false);
        traceButton.setEnabled(false);
        addButtonActionListener(traceButton);

        reloadButton = new JButton();
        reloadButton.setText("Reload");
        reloadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        reloadButton.setFocusPainted(false);
        reloadButton.setEnabled(false);

        buttonPanel.add(reloadButton);
        buttonPanel.add(traceButton);

        toprightPanel = new JPanel(new BorderLayout());
        toprightPanel.setBackground(PrimaryColor);
        toprightPanel.add(directoryLabel, BorderLayout.CENTER);
        toprightPanel.add(buttonPanel, BorderLayout.EAST);

        rightPanel.add(toprightPanel, BorderLayout.NORTH);

        bottomrightPanel = new JPanel(new FlowLayout());
        bottomrightPanel.setBackground(PrimaryColor);

        copyrightLabel = new JLabel();
        copyrightLabel.setText("Copyright © by HyrniT");
        copyrightLabel.setFont(new Font(MyFont, Font.PLAIN, 12));
        copyrightLabel.setForeground(OnPrimaryColor);
        copyrightLabel.setBackground(PrimaryColor);

        bottomrightPanel.add(copyrightLabel);

        rightPanel.add(bottomrightPanel, BorderLayout.SOUTH);

        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(PrimaryColor);

        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPane.setBackground(PrimaryColor);

        bottomLeftPanel = new JPanel(new BorderLayout());
        bottomLeftPanel.setBackground(PrimaryColor);
        bottomLeftPanel.setPreferredSize(new Dimension(0, 500));
        bottomLeftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        topBottomLeftPanel = new JPanel(new BorderLayout());
        topBottomLeftPanel.setBackground(PrimaryColor);
        topBottomLeftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        traceLabel = new JLabel();
        traceLabel.setText("Tracing information");
        traceLabel.setFont(new Font(MyFont, Font.BOLD, 15));
        traceLabel.setForeground(OnPrimaryColor);
        traceLabel.setBackground(PrimaryColor);
        traceLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        exportButton = new JButton();
        exportButton.setText("Export");
        exportButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exportButton.setFocusPainted(false);

        topBottomLeftPanel.add(traceLabel, BorderLayout.CENTER);
        topBottomLeftPanel.add(exportButton, BorderLayout.EAST);

        traceTextArea = new JTextArea();
        traceTextArea.setForeground(PrimaryColor);
        traceTextArea.setBackground(OnPrimaryColor);
        traceTextArea.setCaretColor(OnPrimaryColor);
        traceTextArea.setMargin(new Insets(5, 5, 5, 5));
        traceTextArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        traceTextArea.setLineWrap(false);
        traceTextArea.setWrapStyleWord(false);
        traceTextArea.setEditable(false);

        bottomLeftScrolPane = new JScrollPane(traceTextArea);
        bottomLeftScrolPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bottomLeftScrolPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        bottomLeftScrolPane.setBorder(null);

        bottomLeftPanel.add(bottomLeftScrolPane, BorderLayout.CENTER);
        bottomLeftPanel.add(topBottomLeftPanel, BorderLayout.NORTH);

        topTopLeftPanel = new JPanel(new BorderLayout());
        topTopLeftPanel.setBackground(PrimaryColor);

        appLabel = new JLabel();
        appLabel.setText("HyrniT's Monitoring System");
        appLabel.setFont(new Font(MyFont, Font.BOLD, 20));
        appLabel.setOpaque(true);
        appLabel.setForeground(OnPrimaryColor);
        appLabel.setBackground(PrimaryColor);
        appLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        appLabel.setHorizontalAlignment(SwingConstants.CENTER);

        monitorLabel = new JLabel();
        monitorLabel.setText("Monitors Management");
        monitorLabel.setFont(new Font(MyFont, Font.BOLD, 15));
        monitorLabel.setForeground(OnPrimaryColor);
        monitorLabel.setBackground(PrimaryColor);
        monitorLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        connectButton.setFocusPainted(false);

        JPanel monitorPanel = new JPanel(new BorderLayout());
        monitorPanel.setBackground(PrimaryColor);
        monitorPanel.add(monitorLabel, BorderLayout.CENTER);
        monitorPanel.add(connectButton, BorderLayout.EAST);

        topTopLeftPanel.add(appLabel, BorderLayout.CENTER);
        topTopLeftPanel.add(monitorPanel, BorderLayout.SOUTH);

        monitorsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        monitorsPanel.setBackground(PrimaryColor);

        topLeftScrolPane = new JScrollPane(monitorsPanel);
        topLeftScrolPane.getViewport().setBackground(PrimaryColor);
        topLeftScrolPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        topLeftScrolPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        topLeftScrolPane.setBorder(null);

        topLeftPanel = new JPanel(new BorderLayout());
        topLeftPanel.setBackground(PrimaryColor);
        topLeftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topLeftPanel.add(topTopLeftPanel, BorderLayout.NORTH);
        topLeftPanel.add(topLeftScrolPane, BorderLayout.CENTER);

        verticalSplitPane.setTopComponent(topLeftPanel);
        verticalSplitPane.setBottomComponent(bottomLeftPanel);
        verticalSplitPane.setDividerSize(5);
        verticalSplitPane.setResizeWeight(1);
        leftPanel.add(verticalSplitPane, BorderLayout.CENTER);

        horizonalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        horizonalSplitPane.setLeftComponent(leftPanel);
        horizonalSplitPane.setRightComponent(rightPanel);
        horizonalSplitPane.setDividerSize(5);
        horizonalSplitPane.setResizeWeight(1.0);

        add(horizonalSplitPane, BorderLayout.CENTER);

        createServer();

        setSize(1200, 800);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createServer() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            serverIP = localhost.getHostAddress();
            serverName = localhost.getHostName();

            Image icon = new ImageIcon("Assets/user_image.JPG").getImage().getScaledInstance(50, 50,
                    Image.SCALE_SMOOTH);
            serverButton = new JButton();
            serverButton.setIcon(new ImageIcon(icon));
            serverButton.setBackground(PrimaryColor);
            serverButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            serverButton.setBorder(null);
            addButtonActionListener(serverButton);

            JLabel server = new JLabel("(Server)");
            JLabel name = new JLabel("<html><b>Name: " + "</b>" + serverName + "</html>");
            JLabel ip = new JLabel("<html><b>IP: " + "</b>" + serverIP + "</html>");
            JLabel port = new JLabel("<html><b>Port: " + "</b>" + serverPort + "</html>");

            server.setBackground(PrimaryColor);
            server.setForeground(OnPrimaryColor);
            server.setFont(new Font(MyFont, Font.BOLD, 13));
            server.setBorder(null);

            name.setBackground(PrimaryColor);
            name.setForeground(OnPrimaryColor);
            name.setFont(new Font(MyFont, Font.PLAIN, 12));
            name.setBorder(null);

            ip.setBackground(PrimaryColor);
            ip.setForeground(OnPrimaryColor);
            ip.setFont(new Font(MyFont, Font.PLAIN, 12));
            ip.setBorder(null);

            port.setBackground(PrimaryColor);
            port.setForeground(OnPrimaryColor);
            port.setFont(new Font(MyFont, Font.PLAIN, 12));
            port.setBorder(null);

            JPanel verticalStackPanel = new JPanel();
            verticalStackPanel.setLayout(new GridBagLayout());
            verticalStackPanel.setBackground(PrimaryColor);
            verticalStackPanel.setBorder(BorderFactory.createLineBorder(OnPrimaryColor, 2));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(2, 3, 2, 3);

            verticalStackPanel.add(serverButton, gbc);
            gbc.gridy++;
            verticalStackPanel.add(server, gbc);

            gbc.gridy++;
            verticalStackPanel.add(name, gbc);

            gbc.gridy++;
            verticalStackPanel.add(ip, gbc);

            gbc.gridy++;
            verticalStackPanel.add(port, gbc);

            monitorsPanel.add(verticalStackPanel);

            monitorsPanel.revalidate();
            monitorsPanel.repaint();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void createClient(String clientName, String clientIP, boolean clientStatus) {
        Image icon = new ImageIcon("Assets/user_image.JPG").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            clientButton = new JButton();
            clientButton.setIcon(new ImageIcon(icon));
            clientButton.setBackground(PrimaryColor);
            clientButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            clientButton.setBorder(null);
            clientButton.setActionCommand(clientIP); // phần này quan trọng để phân biệt các button client vs nhau
            clientButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String command = e.getActionCommand();
                    if (command.equals(clientIP)) {
                        if(clientStatus) {
                            JOptionPane.showConfirmDialog(clientButton, clientIP);
                        } else {

                        }
                    } 
                }
            });

            JLabel client = new JLabel("(Client)");
            JLabel name = new JLabel("<html><b>Name: " + "</b>" + clientName + "</html>");
            JLabel ip = new JLabel("<html><b>IP: " + "</b>" + clientIP + "</html>");
            clientStatusLabel = new JLabel(); 

            client.setBackground(PrimaryColor);
            client.setForeground(OnPrimaryColor);
            client.setFont(new Font(MyFont, Font.BOLD, 13));
            client.setBorder(null);

            name.setBackground(PrimaryColor);
            name.setForeground(OnPrimaryColor);
            name.setFont(new Font(MyFont, Font.PLAIN, 12));
            name.setBorder(null);

            ip.setBackground(PrimaryColor);
            ip.setForeground(OnPrimaryColor);
            ip.setFont(new Font(MyFont, Font.PLAIN, 12));
            ip.setBorder(null);

            clientStatusLabel.setBackground(PrimaryColor);
            clientStatusLabel.setFont(new Font(MyFont, Font.BOLD, 12));
            clientStatusLabel.setBorder(null);
            if(clientStatus) {
                showStatus("Connected", StatusType.CONNECTED);
            } else {
                showStatus("Disconnected", StatusType.DISCONNECTED);
            }

            JPanel verticalStackPanel = new JPanel();
            verticalStackPanel.setLayout(new GridBagLayout());
            verticalStackPanel.setBackground(PrimaryColor);
            verticalStackPanel.setBorder(BorderFactory.createLineBorder(OnPrimaryColor, 2));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(2, 3, 2, 3);

            verticalStackPanel.add(clientButton, gbc);
            gbc.gridy++;
            verticalStackPanel.add(client, gbc);

            gbc.gridy++;
            verticalStackPanel.add(name, gbc);

            gbc.gridy++;
            verticalStackPanel.add(ip, gbc);

            gbc.gridy++;
            verticalStackPanel.add(clientStatusLabel, gbc);

            monitorsPanel.add(verticalStackPanel);

            monitorsPanel.revalidate();
            monitorsPanel.repaint();
    }

    private void createPortInput() {
        while (true) {
            String portInput = JOptionPane.showInputDialog(this, "Please enter your server port (0-65535)",
                    "Input server port", JOptionPane.QUESTION_MESSAGE);

            if (portInput == null) {
                System.exit(0);
            }

            try {
                int port = Integer.parseInt(portInput);

                if (port >= 0 && port <= 65535) {
                    if (isPortAvailable(port)) {
                        serverPort = portInput;
                        try {
                            server = new Server(serverPort);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    } else {
                        JOptionPane.showMessageDialog(this, "Port is already in use! Please enter a different port.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid port number! Please enter a port between 0 and 65535.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid port number! Please enter a valid integer.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void addButtonActionListener(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == serverButton) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int result = fileChooser.showOpenDialog(ServerGUI.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        isWatchingServer = isWatchingServer && true;
                        reloadButton.setEnabled(true);
                        traceButton.setEnabled(false);
                        File selectedDirectory = fileChooser.getSelectedFile();
                        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(
                                selectedDirectory.getAbsolutePath());
                        createFileTree(selectedDirectory, rootNode);
                        fileTree = new JTree(rootNode);
                        fileTree.setBackground(PrimaryColor);
                        fileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
                            @Override
                            public void valueChanged(TreeSelectionEvent event) {
                                selectedNode = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
                                if (selectedNode != null) {
                                    traceButton.setEnabled(true);
                                    // String path = getPathFromNode(selectedNode);
                                    // traceTextArea.append(path + '\n');
                                } else {
                                    traceButton.setEnabled(false);
                                }
                            }
                        });

                        rightScrolPane.setViewportView(fileTree);
                    }
                }
                if (e.getSource() == traceButton) {
                    if (selectedNode != null) {
                        stopWatchingServer();
                        startWatchingServer(selectedNode);
                        traceButton.setEnabled(false);
                        isWatchingServer = true;
                    }
                }
            }
        });
    }

    private String getPathFromNode(DefaultMutableTreeNode node) {
        StringBuilder path = new StringBuilder();
        path.append(node.getUserObject());
        while (node.getParent() != null) {
            node = (DefaultMutableTreeNode) node.getParent();
            path.insert(0, node.getUserObject() + File.separator);
        }

        path.append(File.separator);

        return path.toString();
    }

    private void createFileTree(File directory, DefaultMutableTreeNode parentNode) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getName());
                parentNode.add(childNode);

                if (file.isDirectory()) {
                    createFileTree(file, childNode);
                }
            }
        }
    }

    private void startWatchingServer(DefaultMutableTreeNode selectedNode) {
        String folderPath = getPathFromNode(selectedNode).toString();
        Path path = Paths.get(folderPath);
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

            serverMonitorThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        WatchKey watchKey;
                        while ((watchKey = watchService.take()) != null) {
                            for (WatchEvent<?> event : watchKey.pollEvents()) {
                                WatchEvent.Kind<?> kind = event.kind();
                                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                    String message = Helper.getTimestamp() + serverName + " (server) created: "
                                            + getPathFromNode(selectedNode) + event.context();
                                    traceTextArea.append(message + "\n");
                                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                    String message = Helper.getTimestamp() + serverName + " (server) deleted: "
                                            + getPathFromNode(selectedNode) + event.context();
                                    traceTextArea.append(message + "\n");
                                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                    String message = Helper.getTimestamp() + serverName + " (server) modified: "
                                            + getPathFromNode(selectedNode) + event.context();
                                    traceTextArea.append(message + "\n");
                                }
                            }
                            watchKey.reset();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            serverMonitorThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopWatchingServer() {
        if (isWatchingServer) {
            serverMonitorThread.interrupt();
        }
    }

    public class StartServer implements Runnable {
        @Override
        public void run() {
            server.start();
        }
    }

    private enum StatusType {
        CONNECTED,
        DISCONNECTED;
    }

    private static void showStatus(String status, StatusType type) {
        clientStatusLabel.setText(status);
        switch (type) {
            case CONNECTED:
            clientStatusLabel.setForeground(new Color(33, 181, 49));
                break;
            case DISCONNECTED:
            clientStatusLabel.setForeground(new Color(187, 31, 29));
                break;
        }
    }

    public void sendMessage() {
        // xử lý giao diện
        // ..
        // String message = get từ textfield nào
        // int clientId = Integer.parseInt(get từ combobox nào)
        // server.sendMessage(message, clientId);

        // enter xong nhớ setText=""
        // sau khi viết xong phương thức này thì thêm vào event của nút send hoặc enter
        // (xem mẫu tại monitoring-system)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ServerGUI();
        });
        // SwingUtilities.invokeLater(new Runnable() {
        // public void run() {
        // new ServerGUI();
        // }
        // });
    }
}