package RykNet;

import java.io.*;
import java.net.*;
import java.util.HashSet;

public abstract class RykNetServer {

    int port;
    ServerSocket serverSocket;
    ServerConnectionHandler connectionHandler;
    IncomingPacketHandler incomingPacketHandler;
    HashSet<Socket> clients = new HashSet<>();

    public RykNetServer(int port) {
        this.port = port;
        RykNetPacketManager.InitStdPackets(this, null);
        RykNet.Print("Init");
        try {
            serverSocket = new ServerSocket(port);
            connectionHandler = new ServerConnectionHandler(this);
            connectionHandler.start();
            incomingPacketHandler = new IncomingPacketHandler();
            incomingPacketHandler.start();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    // -------- Public Methods --------

    public void StopServer() {
        RykNet.Print("Stopping Server");
        for (Socket client : clients) {
            DisconnectClient(client);
        }
        connectionHandler.Stop();
        incomingPacketHandler.Stop();
    }

    public void StartAcceptingConnections() {
        if (connectionHandler == null) return;
        connectionHandler.StartAcceptingConnections();
        RykNet.Print("Now listening to connections");
    }

    public void StopAcceptingConnections() {
        if (connectionHandler == null) return;
        connectionHandler.StopAcceptingConnections();
        RykNet.Print("Stopped listening to connections");
    }

    public void SendPacket(Socket clientSocket, RykNetPacket packet) {
        String packetData = packet.PacketID() + RykNet.PacketDataBreak() + packet.Encode();
        try {
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            outputStream.writeUTF(packetData);
        }
        catch(NullPointerException e) { ClientDisconnected(clientSocket, RykNet.DisconnectReason.TIMEOUT); }
        catch(Exception e) {  e.printStackTrace();  }
    }

    // ----- Abstract Event Methods -----

    protected abstract void OnClientConnected(Socket clientSocket);
    protected abstract void OnClientDisconnected(Socket clientSocket, RykNet.DisconnectReason reason);

    // ----------------------------------

    int Port() { return port; }

    void ClientConnected(Socket clientSocket) {
        RykNet.Print("Client connected : " + clientSocket.getRemoteSocketAddress());
        clients.add(clientSocket);
        incomingPacketHandler.AddSocket(clientSocket);
        OnClientConnected(clientSocket);
    }

    void ClientDisconnected(Socket clientSocket, RykNet.DisconnectReason reason) {
        RykNet.Print("Client disconnected : " + clientSocket.getRemoteSocketAddress() + " : " + reason.name());
        clients.remove(clientSocket);
        incomingPacketHandler.RemoveSocket(clientSocket);
        OnClientDisconnected(clientSocket, reason);
    }

    void DisconnectClient(Socket clientSocket) {
        SendPacket(clientSocket, new PacketDisconnectClient(null));
        clients.remove(clientSocket);
        incomingPacketHandler.RemoveSocket(clientSocket);
    }

    void ReceivePing(Socket socket, long timestamp) {
        SendPacket(socket, new PacketPongClient(null, timestamp));
    }



}
