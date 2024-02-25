package RykNet;

import java.net.Socket;

public class ClientSidePingThread extends Thread {

    RykNetClient rykNetClient;
    Socket serverSocket;

    boolean isWaitingForPong = false;
    boolean isRunning = true;

    public ClientSidePingThread(RykNetClient rykNetClient) {
        this.rykNetClient = rykNetClient;
    }

    public void Start(Socket serverSocket) {
        this.serverSocket = serverSocket;
        isWaitingForPong = false;
    }

    public void Stop() {
        this.serverSocket = null;

        isWaitingForPong = false;
        isRunning = false;
    }

    public void ReceivedPong() {
        isWaitingForPong = false;
    }

    public void run() {
        while (isRunning) {

            try {
                if (serverSocket == null) {
                    Thread.sleep(5000);
                    continue;
                }

                if (isWaitingForPong) {
                    rykNetClient.Disconnect();
                    Stop();
                }

                rykNetClient.SendPacket( new PacketPingServer(null) );
                isWaitingForPong = true;

                Thread.sleep(5000);
            }
            catch (Exception e) {
                RykNet.Print("Error pinging server, disconnecting");
                rykNetClient.Disconnect();
                Stop();
            }

        }
    }

}
