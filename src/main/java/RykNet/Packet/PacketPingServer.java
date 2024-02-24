package RykNet.Packet;

import RykNet.RykNetPacket;
import RykNet.RykNetServer;

import java.net.Socket;

public class PacketPingServer extends RykNetPacket {

    RykNetServer rykNetServer;
    long timestamp;

    public PacketPingServer(RykNetServer rykNetServer) {
        this.rykNetServer = rykNetServer;
        timestamp = System.currentTimeMillis();
    }

    @Override
    public String PacketID() { return "ping_server"; }

    @Override
    public String Encode() {
        return Long.toString(timestamp);
    }

    @Override
    public void Decode(String data) {
        timestamp = Long.parseLong(data);
    }

    public void HandlePacket(Socket socket) {
        rykNetServer.ReceivePing(socket, timestamp);
    }

}
