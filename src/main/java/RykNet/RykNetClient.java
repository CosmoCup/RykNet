package RykNet;

import java.io.DataOutputStream;
import java.net.*;

public abstract class RykNetClient {

    Socket serverSocket;
    IncomingPacketHandler incomingPacketHandler;

    public RykNetClient() {
        SetShutdownHook();
        PacketManager.InitStdPackets(null, this);
    }

    // -------- Public Methods --------

    public boolean TryConnectToServer(String ip, int port) {
        RykNet.Print("Trying connection to server " + ip + ":" + port);
        try {
            serverSocket = new Socket(ip, port);
            incomingPacketHandler = new IncomingPacketHandler();
            incomingPacketHandler.start();
            incomingPacketHandler.AddSocket(serverSocket);
            RykNet.Print("Connection successful!");
            return true;
        } catch(Exception e) {
            RykNet.Print("Connection failed -> " + e);
            return false;
        }
    }

    public void Disconnect() {
        RykNet.Print("Disconnected from server");
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
        catch (SocketException e) { Disconnect(); }
        catch(Exception e) { e.printStackTrace(); }
    }

    // ----------------------------------

    void ReceivePing(long timestamp) {
        SendPacket(new PacketPongServer(timestamp));
    }


    void SetShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (serverSocket != null) {
                    SendPacket(new PacketClientDisconnect(null, RykNet.DisconnectReason.QUIT));
                }
            } catch (Exception e) { }
        }));
    }

}
