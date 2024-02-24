package RykNet.Packet;

import RykNet.*;

import java.net.Socket;

public class PacketClientDisconnect extends RykNetPacket {

    RykNetServer rykNetServer;
    RykNet.DisconnectReason reason;

    public PacketClientDisconnect(RykNetServer rykNetServer, RykNet.DisconnectReason reason) {
        this.rykNetServer = rykNetServer;
        this.reason = reason;
    }

    @Override
    public String PacketID() { return "client_disconnect"; }

    @Override
    public String Encode() {
        return reason.name();
    }

    @Override
    public void Decode(String data) {
        reason = RykNet.DisconnectReason.valueOf(data);
    }

    public void HandlePacket(Socket socket) {
        rykNetServer.ClientDisconnected(socket, reason);
    }
}
