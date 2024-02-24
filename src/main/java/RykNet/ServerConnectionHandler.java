package RykNet;

import java.net.*;

public class ServerConnectionHandler extends Thread {

    RykNetServer rykNetServer;
    boolean isRunning = true;
    boolean isAcceptingConnections = false;

    public ServerConnectionHandler(RykNetServer rykNetServer) {
        this.rykNetServer = rykNetServer;
    }

    public void StartAcceptingConnections() { isAcceptingConnections = true; }
    public void StopAcceptingConnections() { isAcceptingConnections = false; }
    public boolean IsAcceptingConnections() { return isAcceptingConnections; }

    public void Stop() { isRunning = false; }

    public void run() {
        while(isRunning) {
            if (!isAcceptingConnections) continue;
            try {
                Socket clientSocket = rykNetServer.serverSocket.accept();
                rykNetServer.ClientConnected(clientSocket);
            } catch (Exception e) {  }
        }
    }

}
