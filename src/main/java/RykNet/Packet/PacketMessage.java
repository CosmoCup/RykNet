package RykNet.Packet;

import RykNet.*;

import java.net.Socket;

public class PacketMessage extends RykNetPacket {

    String message;

    public PacketMessage(String message) {
        this.message = message;
    }

    @Override
    public String PacketID() { return "message"; }

    @Override
    public String Encode() {
        return message;
    }

    @Override
    public void Decode(String data) {
        message = data;
    }

    public void HandlePacket(Socket socket) {
        RykNet.Print("[" + socket.getRemoteSocketAddress() + "] -> " + message);
    }
}
