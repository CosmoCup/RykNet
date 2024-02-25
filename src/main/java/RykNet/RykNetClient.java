package RykNet;

import java.io.DataOutputStream;
import java.net.*;
import RykNet.*;

public abstract class RykNetClient {

    Socket serverSocket;
    IncomingPacketHandler incomingPacketHandler;
    ClientSidePingThread pingThread;
    int currentPing = 0;

    public RykNetClient() {
        SetShutdownHook();
        RykNetPacketManager.InitStdPackets(null, this);
        pingThread = new ClientSidePingThread(this);
        pingThread.start();
    }

    // -------- Public Methods --------

    public int GetCurrentPing() { return currentPing; }

    public boolean TryConnectToServer(String ip, int port) {
        RykNet.Print("Trying connection to server " + ip + ":" + port);
        try {
            serverSocket = new Socket(ip, port);
            incomingPacketHandler = new IncomingPacketHandler();
            incomingPacketHandler.start();
            incomingPacketHandler.AddSocket(serverSocket);
            pingThread.Start(serverSocket);
            RykNet.Print("Connection successful!");
            return true;
        } catch(Exception e) {
            RykNet.Print("Connection failed -> " + e);
            return false;
        }
    }

    public void Disconnect() {
        if (serverSocket == null) return;
        RykNet.Print("Disconnected from server");
        pingThread.Stop();
        incomingPacketHandler.Stop();
        serverSocket = null;
    }

    public void SendPacket(RykNetPacket packet) {
        if (serverSocket == null) return;
        String packetData = packet.PacketID() + RykNet.PacketDataBreak() + packet.Encode();
        try {
            DataOutputStream outputStream = new DataOutputStream(serverSocket.getOutputStream());
            outputStream.writeUTF(packetData);
        }
        catch (NullPointerException e) { Disconnect(); }
        catch(Exception e) { e.printStackTrace(); }
    }

    // ----------------------------------

    void ReceivePing(long timestamp) {
        SendPacket(new PacketPongServer(timestamp));
    }

    void ReceivePong(int ping) {
        pingThread.ReceivedPong();
        currentPing = ping;
    }

    void SetShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (serverSocket != null) {
                    SendPacket(new PacketClientDisconnect(null, RykNet.DisconnectReason.QUIT));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }));
    }

}
