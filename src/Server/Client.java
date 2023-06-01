package Server;

import java.io.File;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private File clientFile;
    private boolean clientStatus = true;
    public File getClientFile() {
        return clientFile;
    }
    public void setClientFile(File clientFile) {
        this.clientFile = clientFile;
    }
    // private String clientName;
    public Socket getClientSocket() {
        return clientSocket;
    }
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    // public String getClientName() {
    //     return clientName;
    // }
    // public void setClientName(String clientName) {
    //     this.clientName = clientName;
    // }
    public boolean isClientStatus() {
        return clientStatus;
    }
    public void setClientStatus(boolean clientStatus) {
        this.clientStatus = clientStatus;
    }

}
