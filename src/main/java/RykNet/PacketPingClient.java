package RykNet;

import RykNet.RykNetClient;
import RykNet.RykNetPacket;

import java.net.Socket;

public class PacketPingClient extends RykNetPacket {

    RykNetClient rykNetClient;
    long timestamp;

    public PacketPingClient(RykNetClient rykNetClient) {
        this.rykNetClient = rykNetClient;
        timestamp = System.currentTimeMillis();
    }

    @Override
    public String PacketID() { return "ping_client"; }

    @Override
    public String Encode() {
        return Long.toString(timestamp);
    }

    @Override
    public void Decode(String data) {
        timestamp = Long.parseLong(data);
    }

    public void HandlePacket(Socket socket) {
        rykNetClient.ReceivePing(timestamp);
    }

}
